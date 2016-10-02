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
import com.mda.bomb.network.sync.game.ReadyGameSync;

public class ReadyRoomListenerSync extends BaseSync {
	public int entityID;
	public int selectedSprite;
	public boolean isReady;
	public String name;

	@Override
	public void handleServer(MyServer server, Connection connection) {
		Entity e = server.getEntityWithID(entityID);
		e.getAs(NameComponent.class).name = name;
		e.getAs(ReadyRoomComponent.class).isReady = isReady;
		e.getAs(SpriteComponent.class).ID = selectedSprite;
		ServerMessages.serverIncomming.add(name + "(" + entityID + "), sprite n°" + selectedSprite + ", is" + (isReady ? "" : " not") + " ready.");
		ServerMessages.serverInfo.add("There are actually " + server.getEngine().getSystem(EntitySystem.class).getEntities().size() + " players in the room.");
		server.getServer().sendToAllTCP(this);
		
		//Check if everybody is ready for the game
		boolean readyForGame = true;
		for(Entity entity : server.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			if(!entity.getAs(ReadyRoomComponent.class).isReady) {
				readyForGame = false;
				break;	
			}
		}
		
		//If everybody is ready launch the game
		if(readyForGame) 
			new ReadyGameSync().handleServer(server, connection);
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
		
		if (e.getAs(SpriteComponent.class) != null)
			e.getAs(SpriteComponent.class).ID = selectedSprite;
		else {
			e.addComponent(new SpriteComponent(selectedSprite));
		}
		
		if(client.getRoomListener() != null) 
			client.getRoomListener().updateReadyPlayers();
	}
}
