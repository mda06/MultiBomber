package com.mda.bomb.network.sync.game;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.DropBombComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.entity.BombQueue;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.sync.BaseSync;

public class DropBombSync extends BaseSync {

	public int entityID, bombID;
	public Vector2 bombPos;

	@Override
	public void handleServer(MyServer server, Connection connection) {
		Entity e = server.getEntityWithID(entityID);
		DropBombComponent dropBombComp = e.getAs(DropBombComponent.class);
		if (dropBombComp != null && dropBombComp.nbOfBombs > 0) {
			dropBombComp.nbOfBombs--;
			CollisionComponent cc = e.getAs(CollisionComponent.class);
			Vector2 off = new Vector2();
			if (cc != null) off = cc.offset;
			BombQueue.Bomb b = new BombQueue.Bomb(new PositionComponent(bombPos.x + off.x, bombPos.y + off.y), entityID);
			BombQueue.addToQueue(b);
		}
	}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		if (entityID == client.getEntityID()) {
			DropBombComponent dropBombComp = client.getMyEntity().getAs(DropBombComponent.class);
			dropBombComp.nbOfBombs--;
		}
		// Add a queue for adding bombs...
		// Can't use OpenGL in this context
		BombQueue.Bomb b = new BombQueue.Bomb(new PositionComponent(bombPos.x, bombPos.y), bombID);
		BombQueue.addToQueue(b);
	}

}
