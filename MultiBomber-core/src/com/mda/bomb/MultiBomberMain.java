package com.mda.bomb;

import com.badlogic.gdx.Game;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.screen.ConnectScreen;
import com.mda.bomb.screen.DeadScreen;
import com.mda.bomb.screen.DisconnectedScreen;
import com.mda.bomb.screen.GameScreen;
import com.mda.bomb.screen.RoomScreen;
import com.mda.bomb.screen.ServerScreen;
import com.mda.bomb.screen.event.OpenServerListener;

public class MultiBomberMain extends Game implements OpenServerListener {

	private ConnectScreen connectScreen;
	private GameScreen gameScreen;
	private RoomScreen roomScreen;
	private MyClient clientSide;

	private ServerScreen serverScreen;
	private boolean needToDisposeClient;

	@Override
	public void create() {
		needToDisposeClient = true;
		clientSide = new MyClient();
		clientSide.setDisconnectedListener(new DisconnectedScreen(this));
		clientSide.setDeadListener(new DeadScreen(this));
		connectScreen = new ConnectScreen(this, clientSide);
		roomScreen = new RoomScreen(this, clientSide);
		connectScreen.setOpenServerListener(this);
		serverScreen = new ServerScreen(this);
		gameScreen = new GameScreen(this);
		clientSide.setGameListener(gameScreen);

		setScreen(getConnectScreen());
	}

	@Override
	public void dispose() {
		if (needToDisposeClient && clientSide != null)
			clientSide.dispose();
		if (connectScreen != null)
			connectScreen.dispose();
		if (gameScreen != null)
			gameScreen.dispose();
		if (roomScreen != null)
			roomScreen.dispose();

		if (serverScreen != null)
			serverScreen.dispose();
	}

	@Override
	public void openServer() {
		needToDisposeClient = false;
		if (clientSide != null)
			clientSide.dispose();
		getServerScreen().initEngine();
		setScreen(getServerScreen());
	}

	public MyClient getClientSide() {
		return clientSide;
	}

	public ServerScreen getServerScreen() {
		return serverScreen;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public RoomScreen getRoomScreen() {
		return roomScreen;
	}

	public ConnectScreen getConnectScreen() {
		return connectScreen;
	}
}
