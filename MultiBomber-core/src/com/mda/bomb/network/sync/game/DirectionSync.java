package com.mda.bomb.network.sync.game;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.ClientStateComponent.ClientState;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.sync.BaseSync;

public class DirectionSync extends BaseSync {

	public int entityID;
	public DirectionComponent directionComp;
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		Entity e = server.getEntityWithID(entityID);
		e.getAs(DirectionComponent.class).horizontalDirection = directionComp.horizontalDirection;
		e.getAs(DirectionComponent.class).verticalDirection = directionComp.verticalDirection;
		server.sendToAll(this, ClientState.GAME, true);
	}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		Entity e = client.getEngine().getSystem(EntitySystem.class).getEntity(entityID);
		e.getAs(DirectionComponent.class).horizontalDirection = directionComp.horizontalDirection;
		e.getAs(DirectionComponent.class).verticalDirection = directionComp.verticalDirection;
	}

}
