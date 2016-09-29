package com.mda.bomb.network.sync;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.DropBombComponent;
import com.mda.bomb.ecs.components.HealthComponent;
import com.mda.bomb.ecs.components.InputComponent;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SizeComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.ecs.systems.BombAISystem;
import com.mda.bomb.ecs.systems.FlameSystem;
import com.mda.bomb.ecs.systems.MovementSystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;

public class ReadyGameSync extends BaseSync {
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		ServerMessages.serverInfo.add("A new Game is starting !");
		
		server.getServer().sendToAllTCP(this);

		//Init maps and everything
		new InitMapSync().handleServer(server, connection);
		server.getEngine().addSystem(new MovementSystem(server.getMap()));
		server.getEngine().addSystem(new BombAISystem(server));
		
		int i = 1;
		for (Entity entity : server.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			//Calculate the correct position for the entities...
			entity.addComponent(new DirectionComponent());
			entity.addComponent(new MovementComponent());
			entity.addComponent(new CollisionComponent());
			entity.addComponent(new SizeComponent(new Vector2(50, 50)));
			entity.addComponent(new PositionComponent(i++ * 64, 64));
			//TODO: Add a sync for this, because the server and client have the same code for the moment
			entity.addComponent(new DropBombComponent(5));
			entity.addComponent(new HealthComponent(3));
			
			HealthSync syncHealth = new HealthSync();
			syncHealth.entityID = entity.getID();
			syncHealth.health = entity.getAs(HealthComponent.class).health;
			server.getServer().sendToAllTCP(syncHealth);
			
			EntitySync sync = new EntitySync();
			sync.entityID = entity.getID();
			sync.posX = entity.getAs(PositionComponent.class).x;
			sync.posY = entity.getAs(PositionComponent.class).y;
			server.getServer().sendToAllTCP(sync);
			
			DirectionSync syncDir = new DirectionSync();
			syncDir.entityID = entity.getID();
			syncDir.directionComp = entity.getAs(DirectionComponent.class);
			server.getServer().sendToAllTCP(syncDir);
		}

		server.getEngine().setGameStarted(true);
	}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		client.getEngine().setGameStarted(true);
		
		//Test for less latency, if we handle also collision add size and collision comp
		//client.getEngine().addSystem(new MovementSystem(null));
		client.getEngine().addSystem(new FlameSystem());

		for (Entity entity : client.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			entity.addComponent(new PositionComponent(0, 0));
			entity.addComponent(new DirectionComponent());
			entity.addComponent(new MovementComponent());
		}

		client.getMyEntity().addComponent(new InputComponent());
		client.getMyEntity().addComponent(new DropBombComponent(5));
		
		if(client.getGameListener() != null)
			client.getGameListener().startGame();
		else
			throw new Error("No GameListener in the client. Can't play the game.");
	}

}
