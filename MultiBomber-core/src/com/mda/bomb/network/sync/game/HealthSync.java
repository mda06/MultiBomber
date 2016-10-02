package com.mda.bomb.network.sync.game;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.HealthComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.sync.BaseSync;

public class HealthSync extends BaseSync {

	public int entityID;
	public int health;
	
	@Override
	public void handleServer(MyServer server, Connection connection) {}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		Entity e = client.getEntityWithID(entityID);
		HealthComponent hc = e.getAs(HealthComponent.class);
		if(hc == null) {
			e.addComponent(new HealthComponent(health));
		} else {
			hc.health = health;
		}
	}

}
