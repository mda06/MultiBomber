package com.mda.bomb.network.sync.game;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.PowerupComponent;
import com.mda.bomb.entity.PowerupQueue;
import com.mda.bomb.entity.PowerupQueue.Powerup;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.sync.BaseSync;

public class PowerupSpawnSync extends BaseSync {

	public int entityID;
	public float x, y;
	public PowerupComponent powComp;
	
	@Override
	public void handleServer(MyServer server, Connection connection) {}

	@Override
	public void handleClient(MyClient client, Connection connection) {		
		PowerupQueue.addToQueue(new Powerup(new PositionComponent(x, y), powComp, entityID));
	}
	
}
