package com.mda.bomb.network.sync;

import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;

public abstract class BaseSync {
	public abstract void handleServer(MyServer server, Connection connection);
	public abstract void handleClient(MyClient client, Connection connection);
}
