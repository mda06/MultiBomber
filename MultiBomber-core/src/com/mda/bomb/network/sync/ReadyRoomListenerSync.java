package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.ReadyRoomComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;

public class ReadyRoomListenerSync extends BaseSync {
	public int entityID;
	public boolean isReady;
	public String name;

	@Override
	public void handleServer(MyServer server, Connection connection) {
		Entity e = server.getEntityWithID(entityID);
		e.getAs(ReadyRoomComponent.class).isReady = isReady;
		ServerMessages.serverIncomming.add(name + "(" + entityID + ") is" + (isReady ? "" : " not") + " ready.");
		ServerMessages.serverInfo.add("There are actually " + server.getEngine().getSystem(EntitySystem.class).getEntities().size() + " players in the room.");
		server.getServer().sendToAllExceptTCP(connection.getID(), this);
	}
	@Override
	public void handleClient(MyClient client, Connection connection) {
		Entity e = client.getEngine().getSystem(EntitySystem.class).getEntity(entityID);
		//If the entity not exist
		if(e == null) e = new Entity(entityID);
		
		if (e.getAs(ReadyRoomComponent.class) != null)
			e.getAs(ReadyRoomComponent.class).isReady = isReady;
		else 
			e.addComponent(new ReadyRoomComponent(isReady));
		
		if (e.getAs(NameComponent.class) != null)
			e.getAs(NameComponent.class).name = name;
		else {
			e.addComponent(new NameComponent(name));
		}
		
		if(client.getRoomListener() != null) 
			client.getRoomListener().updateReadyPlayers();
	}
}
