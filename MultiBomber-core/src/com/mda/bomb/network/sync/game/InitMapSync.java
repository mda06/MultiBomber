package com.mda.bomb.network.sync.game;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.sync.BaseSync;

public class InitMapSync extends BaseSync {
	
	public String mapFile;

	public void handleServer(MyServer server, Connection connection) {
		mapFile = "maps/map1.txt";
		server.getMap().initMap(mapFile);
		server.getServer().sendToAllTCP(this);
	}

	public void handleClient(MyClient client, Connection connection) {
		client.getMap().initMap(mapFile);
	}

}
