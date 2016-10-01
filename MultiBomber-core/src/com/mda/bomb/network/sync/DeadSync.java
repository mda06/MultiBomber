package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.entity.EntityQueue;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;

public class DeadSync extends BaseSync {
	public int entityID;

	@Override
	public void handleServer(MyServer server, Connection connection) {}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		//Entity e = client.getEntityWithID(entityID);
		
		EntityQueue.addToQueueToRemove(entityID);
		//client.getEngine().getSystem(EntitySystem.class).removeEntity(entityID);
		if(entityID == client.getEntityID()) {
			client.getDeadListener().entityIsDead();
		}
		
		//Show a text or something else for the funny thing
	}
}
