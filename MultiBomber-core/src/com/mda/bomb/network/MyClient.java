package com.mda.bomb.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mda.bomb.ecs.core.Engine;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.sync.BaseSync;
import com.mda.bomb.network.sync.EnterRoomSync;
import com.mda.bomb.network.sync.EntitySync;
import com.mda.bomb.network.sync.ReadyGameSync;
import com.mda.bomb.network.sync.ReadyRoomDisconnectSync;
import com.mda.bomb.network.sync.ReadyRoomListenerSync;
import com.mda.bomb.network.sync.ReadyRoomSync;
import com.mda.bomb.screen.event.DisconnectedListener;
import com.mda.bomb.screen.event.EnterRoomListener;
import com.mda.bomb.screen.event.GameListener;
import com.mda.bomb.screen.event.RoomListener;
import com.mda.bomb.util.Constants;

public class MyClient extends Listener {
	private Kryo kryo;
	private Client client;
	private EnterRoomListener enterRoomListener;
	private RoomListener roomListener;
	private DisconnectedListener disconnectedListener;
	private GameListener gameListener;

	private int entityID;
	private Engine engine;

	public MyClient() {
		client = new Client();
		client.start();
		client.addListener(this);

		enterRoomListener = null;
		roomListener = null;
		disconnectedListener = null;
		entityID = -1;

		kryo = client.getKryo();
		kryo.register(EntitySync.class);
		kryo.register(EnterRoomSync.class);
		kryo.register(ReadyRoomSync.class);
		kryo.register(ReadyRoomListenerSync.class);
		kryo.register(ReadyRoomDisconnectSync.class);
		kryo.register(ReadyGameSync.class);

		initEngine();
	}

	private void initEngine() {
		engine = new Engine();
		engine.addSystem(new EntitySystem());
	}

	public void setEnterRoomListener(EnterRoomListener list) {
		enterRoomListener = list;
	}

	public EnterRoomListener getEnterRoomListener() {
		return enterRoomListener;
	}

	public void setRoomListener(RoomListener list) {
		roomListener = list;
	}

	public void setDisconnectedListener(DisconnectedListener list) {
		disconnectedListener = list;
	}
	
	public DisconnectedListener getDisconnectedListener() {
		return disconnectedListener;
	}
	
	public RoomListener getRoomListener() {
		return roomListener;
	}
	
	public void setGameListener(GameListener list) {
		gameListener = list;
	}
	
	public GameListener getGameListener() {
		return gameListener;
	}

	public void connect(final String ipAdress) {
		new Thread() {
			@Override
			public void run() {
				try {
					client.connect(5000, ipAdress, Constants.PORT_TCP, Constants.PORT_UDP);
					EnterRoomSync sync = new EnterRoomSync();
					sync.connectionID = client.getID();
					sync.entityID = -1;
					client.sendTCP(sync);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	public void sendTCP(Object o) {
		client.sendTCP(o);
	}

	public Client getClient() {
		return client;
	}

	@Override
	public void connected(Connection connection) {
		System.out.println("Client connected");
	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof BaseSync) {
			BaseSync sync = (BaseSync) o;
			sync.handleClient(this, connection);
		}
	}

	@Override
	public void disconnected(Connection connection) {
		System.out.println("Client disconnected");
		if(disconnectedListener != null)
			disconnectedListener.disconnectedFromServer();
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEntityID(int id) {
		entityID = id;
	}

	public int getEntityID() {
		return entityID;
	}

	public Entity getMyEntity() {
		return ((EntitySystem) engine.getSystem(EntitySystem.class)).getEntity(entityID);
	}

	public String getIP() {
		try {
			for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				for (InetAddress address : Collections.list(iface.getInetAddresses())) {
					if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
						return address.getHostAddress().trim();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return "unknown";
	}

	public void dispose() {
		if (client != null) {
			ReadyRoomDisconnectSync sync = new ReadyRoomDisconnectSync();
			sync.entityID = entityID;
			sendTCP(sync);
			client.stop();
		}
	}
}
