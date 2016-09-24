package com.mda.bomb.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Engine;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.sync.BaseSync;
import com.mda.bomb.network.sync.DirectionSync;
import com.mda.bomb.network.sync.EnterRoomSync;
import com.mda.bomb.network.sync.EntitySync;
import com.mda.bomb.network.sync.ReadyGameSync;
import com.mda.bomb.network.sync.ReadyRoomDisconnectSync;
import com.mda.bomb.network.sync.ReadyRoomListenerSync;
import com.mda.bomb.util.Constants;

public class MyServer extends Listener{
	public final int MAX_MESSAGES = 100;
	
	private Kryo kryo;
	private Server server;
	
	private Engine engine;
	
	public MyServer() {}
	
	public void initEngine() {
		engine = new Engine();
		engine.addSystem(new EntitySystem());
		
		server = new Server();
		server.addListener(this);
		
		kryo = server.getKryo();
		kryo.register(EntitySync.class);
		kryo.register(EnterRoomSync.class);
		kryo.register(ReadyRoomListenerSync.class);
		kryo.register(ReadyRoomDisconnectSync.class);
		kryo.register(ReadyGameSync.class);
		kryo.register(DirectionComponent.class);
		kryo.register(DirectionComponent.Direction.class);
		kryo.register(DirectionSync.class);
		
		try {
			server.bind(Constants.PORT_TCP, Constants.PORT_UDP);
			server.start();//Reduce size of arrays
			ServerMessages.serverInfo.add("Server launched on port TCP: " + Constants.PORT_TCP + ", UDP: " + Constants.PORT_UDP + " on IP: " + getIP());
		} catch(IOException e) {
			ServerMessages.serverInfo.add("Exception when start server");
		}
	}
	
	public void updateEngine(float dt) {
		engine.update(dt);
		if(engine.isGameStarted())
			updateEntitiesInGame(dt);
	}
	
	private void updateEntitiesInGame(float dt) {
		for (Entity entity : engine.getSystem(EntitySystem.class).getEntities().values()) {
			EntitySync sync = new EntitySync();
			sync.entityID = entity.getID();
			sync.posX = entity.getAs(PositionComponent.class).x;
			sync.posY = entity.getAs(PositionComponent.class).y;
			server.sendToAllTCP(sync);
		}
	}
	
	@Override
	public void received(Connection connection, Object o) {
		if(o instanceof BaseSync) {
			BaseSync sync = (BaseSync)o;
			sync.handleServer(this, connection);
		}
	}
	
	public Entity getEntityWithID(int id) {
		return ((EntitySystem) engine.getSystem(EntitySystem.class)).getEntity(id);
	}
	
	@Override
	public void connected(Connection connection) {
		ServerMessages.serverInfo.add("New client connected, ID: " + connection.getID() + ", Info: " + connection.getRemoteAddressTCP() + 
				". Total connected : " + server.getConnections().length);
		if(ServerMessages.serverInfo.size > MAX_MESSAGES) 
			ServerMessages.serverInfo.removeIndex(0);
	}
	
	@Override
	public void disconnected(Connection connection) {
		ServerMessages.serverInfo.add("Client disconnected, ID: " + connection.getID() + ", Info: " + connection.getRemoteAddressTCP() + 
				". Total connected : " + server.getConnections().length);
		ReadyRoomDisconnectSync sync = new ReadyRoomDisconnectSync();
		sync.entityID = connection.getID();
		sync.handleServer(this, connection);
		if(ServerMessages.serverInfo.size > MAX_MESSAGES) 
			ServerMessages.serverInfo.removeIndex(0);
	}
	
	public String getIP() {
		try {
			for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				for (InetAddress address : Collections.list(iface.getInetAddresses())) {
					if(!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
						return address.getHostAddress().trim();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
			
		return "Unknown";
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public Server getServer() {
		return server;
	}
	
	public void dispose() {
		if(server != null)
			server.stop();
	}
}
