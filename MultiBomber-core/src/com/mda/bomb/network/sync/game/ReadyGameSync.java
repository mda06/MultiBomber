package com.mda.bomb.network.sync.game;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.ClientStateComponent;
import com.mda.bomb.ecs.components.ClientStateComponent.ClientState;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.DropBombComponent;
import com.mda.bomb.ecs.components.HealthComponent;
import com.mda.bomb.ecs.components.InputComponent;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.ecs.systems.BombAISystem;
import com.mda.bomb.ecs.systems.MovementSystem;
import com.mda.bomb.ecs.systems.PowerupSystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;
import com.mda.bomb.network.sync.BaseSync;

public class ReadyGameSync extends BaseSync {
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		ServerMessages.serverInfo.add("A new Game is starting !");
		
		server.sendToAll(this, ClientState.ROOM, true);
		
		//Init maps and everything
		server.getEngine().addSystem(new MovementSystem(server.getMap()));
		server.getEngine().addSystem(new BombAISystem(server));
		server.getEngine().addExternSystem(new PowerupSystem(server.getMap(), server));
		
		int i = 1;
		for (Entity entity : server.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			//Calculate the correct position for the entities...
			entity.addComponent(new DirectionComponent());
			entity.addComponent(new MovementComponent());
			entity.addComponent(new CollisionComponent(new Vector2(50, 50)));
			entity.addComponent(new PositionComponent(i++ * 96, 96));
			if(entity.getAs(SpriteComponent.class).ID == 1) {
				entity.getAs(CollisionComponent.class).offset = new Vector2(0, -33);
				entity.getAs(PositionComponent.class).y += 33;
			}
			//TODO: Add a sync for this, because the server and client have the same code for the moment
			entity.addComponent(new DropBombComponent(10));
			entity.addComponent(new HealthComponent(3));
			entity.getAs(ClientStateComponent.class).clientState = ClientState.GAME;
			
			CollisionCompSync ccSync = new CollisionCompSync();
			ccSync.entityID = entity.getID();
			ccSync.size = entity.getAs(CollisionComponent.class).size;
			ccSync.offset = entity.getAs(CollisionComponent.class).offset;
			server.sendToAll(ccSync, ClientState.GAME, true);
			
			HealthSync syncHealth = new HealthSync();
			syncHealth.entityID = entity.getID();
			syncHealth.health = entity.getAs(HealthComponent.class).health;
			server.sendToAll(syncHealth, ClientState.GAME, true);
			
			EntitySync sync = new EntitySync();
			sync.entityID = entity.getID();
			sync.posX = entity.getAs(PositionComponent.class).x;
			sync.posY = entity.getAs(PositionComponent.class).y;
			server.sendToAll(sync, ClientState.GAME, true);
			
			DirectionSync syncDir = new DirectionSync();
			syncDir.entityID = entity.getID();
			syncDir.directionComp = entity.getAs(DirectionComponent.class);
			server.sendToAll(syncDir, ClientState.GAME, true);
		}

		new InitMapSync().handleServer(server, connection);
		server.getEngine().setGameStarted(true);
	}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		client.getEngine().setGameStarted(true);

		for (Entity entity : client.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			entity.addComponent(new PositionComponent(0, 0));
			entity.addComponent(new DirectionComponent());
			entity.addComponent(new MovementComponent());
		}

		client.getMyEntity().addComponent(new InputComponent());
		client.getMyEntity().addComponent(new DropBombComponent(10));
		
		if(client.getGameListener() != null)
			client.getGameListener().startGame();
		else
			throw new Error("No GameListener in the client. Can't play the game.");
	}

}
