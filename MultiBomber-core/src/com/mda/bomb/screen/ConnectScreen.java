package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.screen.actor.BackgroundActor;
import com.mda.bomb.screen.event.EnterRoomListener;
import com.mda.bomb.screen.event.OpenServerListener;
import com.mda.bomb.screen.filter.DigitFilter;
import com.mda.bomb.util.Constants;
import com.mda.bomb.util.IPUtils;

public class ConnectScreen implements Screen, EnterRoomListener {

	private MultiBomberMain main;
	private MyClient clientSide;
	private OpenServerListener openServerListener;
	private boolean waitEnterRoom;
	private float waitEnterRoomCooldown, waitEnterRoomCurrentCooldown;
	
	private SpriteBatch batch;
	private Stage stage;
	private TextField txtIP;
	private Label lblName;
	private TextButton btnConnect, btnOpenServer;

	public ConnectScreen(MultiBomberMain m, MyClient client) {
		main = m;
		clientSide = client;
		clientSide.setEnterRoomListener(this);
		openServerListener = null;
		waitEnterRoom = false;
		waitEnterRoomCooldown = 5;
		waitEnterRoomCurrentCooldown = waitEnterRoomCooldown;

		initStage();
		batch = new SpriteBatch();
	}

	private void initStage() {
		Skin skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));
		stage = new Stage();
		
		stage.addActor(new BackgroundActor());

		lblName = new Label(Constants.GAME_NAME, skin);
		lblName.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 1.5f);
		lblName.setColor(Color.RED);
		stage.addActor(lblName);
		
		txtIP = new TextField("127.0.0.1", skin);
		txtIP.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		txtIP.setTextFieldFilter(new DigitFilter());
		txtIP.addListener(new EventListener() {
			public boolean handle(Event event) {
				if(IPUtils.isValidIP(txtIP.getText())) txtIP.setColor(Color.WHITE);
				else txtIP.setColor(Color.RED);
				return false;
			}			
		});
		stage.addActor(txtIP);

		btnConnect = new TextButton("Connect", skin);
		btnConnect.setCenterPosition(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 3);
		btnConnect.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String ip = txtIP.getText();
				if(!IPUtils.isValidIP(ip)) return;

				clientSide.connect(ip);
				waitEnterRoom = true;
			}
		});
		stage.addActor(btnConnect);
		
		btnOpenServer = new TextButton("Host a server", skin);
		btnOpenServer.setCenterPosition(Gdx.graphics.getWidth() / 2 + 50, Gdx.graphics.getHeight() / 3);
		btnOpenServer.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(openServerListener != null)
					openServerListener.openServer();
			}
		});
		stage.addActor(btnOpenServer);

		Gdx.input.setInputProcessor(stage);
	}

	private void update(float dt) {
		if(waitEnterRoom) {
			waitEnterRoomCurrentCooldown -= dt;
			if(waitEnterRoomCurrentCooldown < 0) {
				waitEnterRoomCurrentCooldown = waitEnterRoomCooldown;
				waitEnterRoom = false;
			}
		}
		
		btnConnect.setDisabled(waitEnterRoom);
		btnOpenServer.setDisabled(waitEnterRoom);
	}
	
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		stage.draw();
		batch.end();
	}

	public void dispose() {
		batch.dispose();
		stage.dispose();
	}
	
	public void setOpenServerListener(OpenServerListener list) {
		openServerListener = list;
	}
	
	@Override
	public void connectedToServer() {
		main.setScreen(main.getRoomScreen());
	}
	
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	public void resume() {
		Gdx.input.setInputProcessor(stage);
	}
	
	public void resize(int width, int height) {
	}

	public void hide() {
	}

	public void pause() {
	}
}
