package com.mda.bomb.network.sync.game;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.sync.BaseSync;

public class CollisionCompSync extends BaseSync {
	public int entityID;
	public Vector2 size, offset;

	@Override
	public void handleServer(MyServer server, Connection connection) {

	}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		Entity e = client.getEntityWithID(entityID);
		e.addComponent(new CollisionComponent(size, offset));
	}

}
