package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.entity.Direction;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;

public class EntitySync extends BaseSync{
	public int id;
	public Direction direction;

	@Override
	public void handleServer(MyServer server, Connection connection) {
		
	}
	@Override
	public void handleClient(MyClient client, Connection connection) {
		
	}
}
