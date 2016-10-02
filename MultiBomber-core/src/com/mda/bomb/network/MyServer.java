package com.mda.bomb.network;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mda.bomb.ecs.components.BombAIComponent;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.HealthComponent;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.PowerupComponent;
import com.mda.bomb.ecs.core.Engine;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.entity.BombQueue;
import com.mda.bomb.entity.EntityQueue;
import com.mda.bomb.map.Map;
import com.mda.bomb.network.sync.BaseSync;
import com.mda.bomb.network.sync.DisconnectSync;
import com.mda.bomb.network.sync.game.BombExplodeSync;
import com.mda.bomb.network.sync.game.DeadSync;
import com.mda.bomb.network.sync.game.DropBombSync;
import com.mda.bomb.network.sync.game.EntitySync;
import com.mda.bomb.network.sync.game.HealthSync;
import com.mda.bomb.network.sync.game.PowerupSpawnSync;
import com.mda.bomb.screen.event.BombExplodeListener;
import com.mda.bomb.screen.event.PowerupListener;
import com.mda.bomb.util.Constants;
import com.mda.bomb.util.IPUtils;

public class MyServer extends Listener implements BombExplodeListener, PowerupListener {
	public final int MAX_MESSAGES = 100;

	private Kryo kryo;
	private Server server;

	private Engine engine;
	private Map map;

	public MyServer() {
	}

	public void initEngine() {
		engine = new Engine();
		engine.addSystem(new EntitySystem());

		map = new Map();

		server = new Server();
		server.addListener(this);

		kryo = server.getKryo();
		KryoRegisters.registerKryo(kryo);

		try {
			server.bind(Constants.PORT_TCP, Constants.PORT_UDP);
			server.start();// Reduce size of arrays
			ServerMessages.serverInfo
					.add("Server launched on port TCP: " + Constants.PORT_TCP + ", UDP: " + Constants.PORT_UDP + " on IP: " + IPUtils.getMyIP());
		} catch (IOException e) {
			ServerMessages.serverInfo.add("Exception when start server");
		}
	}

	public void updateEngine(float dt) {
		engine.update(dt);
		if (engine.isGameStarted()) {
			updateEntitiesInGame(dt);
			updateBombQueue();
			updateExplodedBombs();
			updateEntitiesToRemove();
		}
	}

	private void updateEntitiesToRemove() {
		Integer id = null;
		while ((id = EntityQueue.pollEntityToRemove()) != null) {
			engine.getSystem(EntitySystem.class).removeEntity(id);
			ServerMessages.serverInfo.add("There are actually " + engine.getSystem(EntitySystem.class).getEntities().size() + " players in the room.");
			//BOMB = NO ENTITY !!
			if(engine.getSystem(EntitySystem.class).getEntities().size() == 0) {
				engine.setGameStarted(false);
				ServerMessages.serverInfo.add("No players anymore in the game. The game is finished and people can again connect to the room.");
			}
		}

		// Check server state: Room - Game - Finish 
		// Add player comp for the state and to easy know how many players are connected
		// Because pwoerups/bombs are also entity in entitySystem
		// Check size of array 1 entity = WIN
		// Check size of array 0 entity = NEW GAME
	}

	private void updateExplodedBombs() {
		Iterator<Entry<Integer, Entity>> it = engine.getSystem(EntitySystem.class).getEntities().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Entity> item = it.next();
			BombAIComponent ai = item.getValue().getAs(BombAIComponent.class);
			if (ai != null && ai.isExploded) it.remove();
		}
	}

	private void updateBombQueue() {
		BombQueue.Bomb bomb = null;
		while ((bomb = BombQueue.poll()) != null) {
			Entity e = new Entity();
			e.addComponent(bomb.pc);
			e.addComponent(new BombAIComponent());
			DropBombSync sync = new DropBombSync();
			ServerMessages.serverIncomming.add("New Bomb created with ID: " + e.getID());
			sync.bombID = e.getID();
			sync.bombPos = new Vector2(bomb.pc.x, bomb.pc.y);
			sync.entityID = bomb.ID;
			server.sendToAllTCP(sync);
		}
	}

	private void updateEntitiesInGame(float dt) {
		for (Entity entity : engine.getSystem(EntitySystem.class).getEntities().values()) {
			EntitySync sync = new EntitySync();
			sync.entityID = entity.getID();
			sync.posX = entity.getAs(PositionComponent.class).x;
			sync.posY = entity.getAs(PositionComponent.class).y;
			server.sendToAllUDP(sync);
		}
	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof BaseSync) {
			BaseSync sync = (BaseSync) o;
			sync.handleServer(this, connection);
		}
	}

	public Entity getEntityWithID(int id) {
		return engine.getSystem(EntitySystem.class).getEntity(id);
	}

	@Override
	public void connected(Connection connection) {
		ServerMessages.serverInfo.add("New client connected, ID: " + connection.getID() + ", Info: " + connection.getRemoteAddressTCP()
				+ ". Total connected : " + server.getConnections().length);
		if (ServerMessages.serverInfo.size > MAX_MESSAGES) ServerMessages.serverInfo.removeIndex(0);
	}

	@Override
	public void disconnected(Connection connection) {
		ServerMessages.serverInfo.add("Client disconnected, ID: " + connection.getID() + ", Info: " + connection.getRemoteAddressTCP()
				+ ". Total connected : " + server.getConnections().length);
		DisconnectSync sync = new DisconnectSync();
		sync.entityID = connection.getID();
		sync.handleServer(this, connection);
		if (ServerMessages.serverInfo.size > MAX_MESSAGES) ServerMessages.serverInfo.removeIndex(0);
	}

	public Map getMap() {
		return map;
	}

	public Engine getEngine() {
		return engine;
	}

	public Server getServer() {
		return server;
	}

	public void dispose() {
		if (server != null) server.stop();
	}

	private void handleEntityExplosions(Entity e) {
		BombAIComponent ai = e.getAs(BombAIComponent.class);
		PositionComponent pc = e.getAs(PositionComponent.class);
		if (ai == null || pc == null) return;

		Vector2 tilePos = map.getTilePositionWithAbsolutePosition(pc.x, pc.y);
		int tx = (int) tilePos.x, ty = (int) tilePos.y;
		int minX = tx - ai.explodeSize, maxX = tx + ai.explodeSize;
		int minY = ty - ai.explodeSize, maxY = ty + ai.explodeSize;
		for (Entity entity : engine.getSystem(EntitySystem.class).getEntities().values()) {
			HealthComponent hc = entity.getAs(HealthComponent.class);
			PositionComponent entityPos = entity.getAs(PositionComponent.class);
			if (hc == null || entityPos == null) continue;
			Vector2 entityTilePos = map.getTilePositionWithAbsolutePosition(entityPos.x, entityPos.y);
			
			CollisionComponent cc = entity.getAs(CollisionComponent.class);
			if(cc != null) {
				entityTilePos = map.getTilePositionWithAbsolutePosition(entityPos.x + cc.offset.x, entityPos.y + cc.offset.y);
			}
			
			if (entityTilePos.x > minX && entityTilePos.x < maxX && entityTilePos.y == ty) {
				if (hitEntityWithBomb(entity)) {
					int entityID = entity.getID();
					sendDeadSync(entityID);
					EntityQueue.addToQueueToRemove(entityID);
				}
				// When touch 1, don't need to test the other pos
				continue;
			}
			if (entityTilePos.y > minY && entityTilePos.y < maxY && entityTilePos.x == tx) {
				if (hitEntityWithBomb(entity)) {
					int entityID = entity.getID();
					sendDeadSync(entityID);
					EntityQueue.addToQueueToRemove(entityID);
				}
			}
		}
	}
	
	private void sendDeadSync(int entityID) {
		DeadSync sync = new DeadSync();
		sync.entityID = entityID;
		server.sendToAllTCP(sync);
	}

	private boolean hitEntityWithBomb(Entity e) {
		boolean isDead = false;
		HealthComponent hc = e.getAs(HealthComponent.class);
		hc.health--;
		ServerMessages.serverIncomming.add(e.getAs(NameComponent.class).name + " hitted now has " + hc.health + " health.");
		if (hc.health <= 0) {
			hc.health = 0;
			isDead = true;
			ServerMessages.serverIncomming.add(e.getAs(NameComponent.class).name + " is dead !");
		}
		HealthSync sync = new HealthSync();
		sync.entityID = e.getID();
		sync.health = hc.health;
		server.sendToAllTCP(sync);
		return isDead;
	}

	@Override
	public void explode(Entity e) {
		map.explode(e);
		handleEntityExplosions(e);
		ServerMessages.serverIncomming.add("Bomb n°" + e.getID() + " just explosed.");
		BombExplodeSync sync = new BombExplodeSync();
		sync.bombID = e.getID();
		server.sendToAllTCP(sync);
	}

	@Override
	public void PowerupGenerated(Entity e) {
		PositionComponent pc = e.getAs(PositionComponent.class);
		ServerMessages.serverIncomming.add("Powerup generated at " + pc.x + "/" + pc.y);
		PowerupSpawnSync sync = new PowerupSpawnSync();
		sync.entityID = e.getID();
		sync.x = pc.x;
		sync.y = pc.y;
		sync.powComp = e.getAs(PowerupComponent.class);
		server.sendToAllTCP(sync);
	}
}
