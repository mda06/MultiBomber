package com.mda.bomb.network.sync.room;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.ReadyRoomComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;
import com.mda.bomb.network.sync.BaseSync;

public class EnterRoomSync extends BaseSync {
	public int connectionID;
	public int entityID;
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		//Check if the game has begun, if true refuse the connection
		if(server.getEngine().isGameStarted()) {
			ServerMessages.serverInfo.add("Connection " + connectionID + " try to connect the room but the game is already launched. Connection rejected");
			entityID = -1;
			server.getServer().sendToTCP(connection.getID(), this);
			return;
		}
		
		Entity e = new Entity(connection.getID());
		e.addComponent(new ReadyRoomComponent(false));
		e.addComponent(new NameComponent("Undefined"));
		e.addComponent(new SpriteComponent(1));
		entityID = e.getID();
		ServerMessages.serverIncomming.add("Client with ConnectionID " + connectionID + " has now EntityID " + entityID);		
		ServerMessages.serverInfo.add("There are actually " + server.getEngine().getSystem(EntitySystem.class).getEntities().size() + " players in the room.");
		server.getServer().sendToAllTCP(this);
		
		//send the ready status to the new connection
		for (Entity entity : server.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			ReadyRoomListenerSync readySync = new ReadyRoomListenerSync();
			readySync.entityID = entity.getID();
			readySync.isReady = entity.getAs(ReadyRoomComponent.class).isReady;
			readySync.name = entity.getAs(NameComponent.class).name;
			readySync.selectedSprite = entity.getAs(SpriteComponent.class).ID;
			server.getServer().sendToTCP(connection.getID(), readySync);
		}
	}
	
	@Override
	public void handleClient(MyClient client, Connection connection) {
		//TODO: Create a wait-room for the next round
		//The game is already begin, disconnect the current player
		if(entityID == -1) {
			client.getDisconnectedListener().disconnectedFromServer();
			return;
		}
		
		Entity e = new Entity(entityID);
		e.addComponent(new ReadyRoomComponent(false));
		e.addComponent(new NameComponent("Undefined"));
		e.addComponent(new SpriteComponent(1));
		
		if(connectionID == client.getClient().getID())
			client.setEntityID(entityID);
		
		if(client.getEnterRoomListener() != null)
			client.getEnterRoomListener().connectedToServer();
		
		if(client.getRoomListener() != null)
			client.getRoomListener().updateReadyPlayers();
	}
}