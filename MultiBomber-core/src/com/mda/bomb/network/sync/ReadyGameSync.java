package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;

public class ReadyGameSync extends BaseSync {
	
	@Override
	public void handleServer(MyServer server, Connection connection) {
		//Init maps and everything
		ServerMessages.serverInfo.add("A new Game is starting !");
		server.getEngine().setGameStarted(true);
		server.getServer().sendToAllTCP(this);
	}

	@Override
	public void handleClient(MyClient client, Connection connection) {
		client.getEngine().setGameStarted(true);
		
		if(client.getGameListener() != null)
			client.getGameListener().startGame();
		else
			throw new Error("No GameListener in the client. Can't play the game.");
	}

}
