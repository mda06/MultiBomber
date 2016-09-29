package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;

public class DirectionSync extends BaseSync {

	public int entityID;
	public DirectionComponent directionComp;
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		Entity e = server.getEntityWithID(entityID);
		e.getAs(DirectionComponent.class).direction = directionComp.direction;
		server.getServer().sendToAllTCP(this);
	}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		Entity e = client.getEngine().getSystem(EntitySystem.class).getEntity(entityID);
		e.getAs(DirectionComponent.class).direction = directionComp.direction;
	}

}