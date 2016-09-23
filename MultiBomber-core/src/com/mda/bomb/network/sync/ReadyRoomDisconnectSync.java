package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;

public class ReadyRoomDisconnectSync extends BaseSync{
	public int entityID;
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		Entity e = server.getEntityWithID(entityID);
		if(e != null) {
			NameComponent nameComp = e.getAs(NameComponent.class);
			ServerMessages.serverIncomming.add((nameComp == null ? ("ID("+entityID+")") : nameComp.name) + " is now disconnected.");
		} else {
			ServerMessages.serverIncomming.add("ID(" + entityID + ") is now disconnected.");
		}
		
		((EntitySystem) server.getEngine().getSystem(EntitySystem.class)).removeEntity(entityID);	
		ServerMessages.serverInfo.add("There are actually " + server.getEngine().getSystem(EntitySystem.class).getEntities().size() + " players in the room.");
		if(server.getEngine().getSystem(EntitySystem.class).getEntities().size() == 0) {
			server.getEngine().setGameStarted(false);
			ServerMessages.serverInfo.add("No players anymore in the game. The game is finished and people can again connect to the room.");
		}
		server.getServer().sendToAllExceptTCP(connection.getID(), this);
	}
	
	@Override
	public void handleClient(MyClient client, Connection connection) {
		client.getEngine().getSystem(EntitySystem.class).removeEntity(entityID);
		if(client.getRoomListener() != null)
			client.getRoomListener().updateReadyPlayers();
	}
}