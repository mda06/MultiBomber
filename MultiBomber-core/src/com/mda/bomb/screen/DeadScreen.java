package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.screen.event.DeadListener;

public class DeadScreen implements Screen, DeadListener{

	private MultiBomberMain main;
	
	private SpriteBatch batch;
	private Stage stage;
	
	private float cooldownToRoomScreen;
	
	public DeadScreen(MultiBomberMain m) {
		main = m;
		batch = new SpriteBatch();
		
		Skin skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));
		stage = new Stage();
		
		Dialog dia = new Dialog("You are dead", skin);
		dia.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(dia);
		
		cooldownToRoomScreen = 3f;
	}
	
	public void render(float delta) {
		cooldownToRoomScreen -= delta;
		if(cooldownToRoomScreen < 0) {
			cooldownToRoomScreen = 3;
			//main.setScreen(main.getRoomScreen());
		}
		
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

	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	public void resume() {
		Gdx.input.setInputProcessor(stage);
	}

	public void resize(int width, int height) {}
	public void hide() {}
	public void pause() {}

	@Override
	public void entityIsDead() {
		main.setScreen(this);
	}
}
