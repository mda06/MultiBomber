package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.ReadyRoomComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;

public class ReadyRoomSync extends BaseSync {
	public int entityID;
	public int selectedSprite;
	public String name;

	@Override
	public void handleServer(MyServer server, Connection connection) {
		Entity e = server.getEntityWithID(entityID);
		e.addComponent(new NameComponent(name));
		e.getAs(ReadyRoomComponent.class).isReady = true;
		//Sprite...
		ReadyRoomListenerSync list = new ReadyRoomListenerSync();
		list.entityID = entityID;
		list.isReady = true;
		list.name = name;
		ServerMessages.serverIncomming.add(name + "(" + list.entityID + ") is" + (list.isReady ? "" : " not") + " ready.");	
		ServerMessages.serverInfo.add("There are actually " + server.getEngine().getSystem(EntitySystem.class).getEntities().size() + " players in the room.");
		server.getServer().sendToAllTCP(list);
	}
	@Override
	public void handleClient(MyClient client, Connection connection) {}
}
