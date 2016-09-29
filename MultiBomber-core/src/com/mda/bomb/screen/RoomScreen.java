package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.ReadyRoomComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.entity.animation.AnimationFactory;
import com.mda.bomb.entity.animation.SimpleAnimation;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.sync.ReadyRoomListenerSync;
import com.mda.bomb.screen.actor.BackgroundActor;
import com.mda.bomb.screen.event.RoomListener;

public class RoomScreen implements Screen, RoomListener {

	@SuppressWarnings("unused")
	private MultiBomberMain main;
	private MyClient clientSide;

	private SpriteBatch batch;
	private Stage stage;
	private Label lblName;
	private TextField txtName;
	private CheckBox btnSprite1, btnSprite2;
	private Label lblPlayers;
	private TextArea txtPlayers;
	private CheckBox btnReady;
	
	private SimpleAnimation anim1, anim2;

	public RoomScreen(MultiBomberMain m, MyClient client) {
		main = m;
		clientSide = client;
		clientSide.setRoomListener(this);
		batch = new SpriteBatch();

		initStage();
		initAnims();
	}
	
	private void initAnims() {
		anim1 = AnimationFactory.getSimpleAnimationOfSpriteNb(1);
		anim1.getAnimation().setPlayMode(PlayMode.LOOP);
		anim2 = AnimationFactory.getSimpleAnimationOfSpriteNb(2);
		anim2.getAnimation().setPlayMode(PlayMode.LOOP);
	}

	private void initStage() {
		Skin skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));
		stage = new Stage();

		stage.addActor(new BackgroundActor());
		
		lblName = new Label("Name: ", skin);
		lblName.setCenterPosition(Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 1.1f);
		stage.addActor(lblName);

		txtName = new TextField("Undefined", skin);
		txtName.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 1.1f);
		stage.addActor(txtName);

		btnSprite1 = new CheckBox("Sprite 1", skin);
		btnSprite1.setCenterPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 1.4f);
		stage.addActor(btnSprite1);

		btnSprite2 = new CheckBox("Sprite 2", skin);
		btnSprite2.setCenterPosition((float) (Gdx.graphics.getWidth() / 4) * 3, Gdx.graphics.getHeight() / 1.4f);
		stage.addActor(btnSprite2);

		btnSprite1.setChecked(true);
		new ButtonGroup(btnSprite1, btnSprite2);

		lblPlayers = new Label("Current players in the room", skin);
		lblPlayers.setCenterPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
		stage.addActor(lblPlayers);

		txtPlayers = new TextArea("", skin);
		txtPlayers.setHeight(100);
		txtPlayers.setBounds(50, 20, 330, 130);
		txtPlayers.setDisabled(true);
		stage.addActor(txtPlayers);

		btnReady = new CheckBox("Ready", skin);
		btnReady.setPosition(Gdx.graphics.getWidth() - 100, 50);
		btnReady.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(txtName.getText().equals("")) {
					btnReady.setChecked(false);
					return;
				}
				
				ReadyRoomListenerSync sync = new ReadyRoomListenerSync();
				sync.entityID = clientSide.getEntityID();
				sync.isReady = btnReady.isChecked();
				sync.name = txtName.getText();
				sync.selectedSprite = btnSprite1.isChecked() ? 1 : 2;
				clientSide.getMyEntity().getAs(NameComponent.class).name = sync.name;
				clientSide.sendTCP(sync);
			}
		});
		stage.addActor(btnReady);
	}

	@Override
	public void updateReadyPlayers() {
		String str = "";
		for (Entity e : clientSide.getEngine().getSystem(EntitySystem.class).getEntities().values()) {
			if (e.getID() == clientSide.getEntityID())
				continue;
		
			NameComponent nc = e.getAs(NameComponent.class);
			ReadyRoomComponent rrc = e.getAs(ReadyRoomComponent.class);
			SpriteComponent sc = e.getAs(SpriteComponent.class);

			String name = nc == null ? "Undefined" : nc.name;
			boolean isReady = rrc == null ? false : rrc.isReady;
			String spriteIndex = sc == null ? ", no SpriteComp," : (", sprite n°" + sc.ID + ",");
			
			str += "-" + name + "(" + e.getID() + ")" + spriteIndex + " is " + (isReady ? "" : "not") + " ready\n";
		}
		txtPlayers.setText(str);
	}

	private void update(float dt) {
		txtName.setDisabled(btnReady.isChecked());
		btnSprite1.setDisabled(btnReady.isChecked());
		btnSprite2.setDisabled(btnReady.isChecked());
		
		anim1.update(dt);
		anim2.update(dt);
	}

	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//What the f*ck ? Why
		try {
			batch.begin();
			stage.draw();
			batch.end();
	
			float x = Gdx.graphics.getWidth() / 4 - 30, y = Gdx.graphics.getHeight() / 1.4f - 120;
			batch.begin();
			batch.draw(anim1.getCurrentFrame(), x, y);
			x = (float) (Gdx.graphics.getWidth() / 4) * 3 - 30;
			batch.draw(anim2.getCurrentFrame(), x, y);
			batch.end();
			
		} catch(Exception ex) {
			ex.printStackTrace();
			if(batch.isDrawing())
				batch.end();
		}
	}

	public void dispose() {
		batch.dispose();
		stage.dispose();
		anim1.dispose();
		anim2.dispose();
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
