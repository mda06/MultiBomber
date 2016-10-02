package com.mda.bomb.network.sync.room;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.ClientStateComponent;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.ReadyRoomComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.components.ClientStateComponent.ClientState;
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
		// Check if the game has begun, if true send a sync to notify clients that a game is running

		Entity e = new Entity(connection.getID());
		e.addComponent(new ReadyRoomComponent(false));
		e.addComponent(new NameComponent("Undefined"));
		e.addComponent(new SpriteComponent(1));
		e.addComponent(new ClientStateComponent(ClientState.ROOM));
		entityID = e.getID();
		ServerMessages.serverIncomming.add("Client with ConnectionID " + connectionID + " has now EntityID " + entityID);
		ServerMessages.serverInfo
				.add("There are actually " + server.getEngine().getSystem(EntitySystem.class).getEntities().size() + " players in the room.");
		server.sendToAll(this, ClientState.ROOM, true);

		// send the ready status to the new connection
		for (Entity entity : server.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			if (entity.getAs(ClientStateComponent.class).clientState != ClientState.ROOM) continue;

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
		Entity e = new Entity(entityID);
		e.addComponent(new ReadyRoomComponent(false));
		e.addComponent(new NameComponent("Undefined"));
		e.addComponent(new SpriteComponent(1));

		if (connectionID == client.getClient().getID()) client.setEntityID(entityID);
		if (client.getEnterRoomListener() != null) client.getEnterRoomListener().connectedToServer();
		if (client.getRoomListener() != null) client.getRoomListener().updateReadyPlayers();
	}
}