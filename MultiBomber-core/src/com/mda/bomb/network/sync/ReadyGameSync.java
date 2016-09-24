package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.InputComponent;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.ecs.systems.MovementSystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;

public class ReadyGameSync extends BaseSync {
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		ServerMessages.serverInfo.add("A new Game is starting !");
		
		server.getEngine().setGameStarted(true);
		server.getEngine().addSystem(new MovementSystem());
		server.getServer().sendToAllTCP(this);

		//Init maps and everything
		
		for (Entity entity : server.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			//Calculate the correct position for the entities...
			entity.addComponent(new PositionComponent((float)Math.random() * 600, (float)Math.random() * 440));
			entity.addComponent(new DirectionComponent());
			entity.addComponent(new MovementComponent());
			
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
		
		if(client.getGameListener() != null)
			client.getGameListener().startGame();
		else
			throw new Error("No GameListener in the client. Can't play the game.");
	}

}
