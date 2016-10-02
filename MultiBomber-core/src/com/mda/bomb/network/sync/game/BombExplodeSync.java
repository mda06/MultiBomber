package com.mda.bomb.network.sync.game;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.BombAIComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.sync.BaseSync;

public class BombExplodeSync extends BaseSync  {

	public int bombID;
	
	@Override
	public void handleServer(MyServer server, Connection connection) {}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		Entity e = client.getEntityWithID(bombID);
		if(e != null) {
			client.getMap().explode(e);
			e.getAs(BombAIComponent.class).isExploded = true;
		}
	}

}
