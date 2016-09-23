package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.screen.event.DisconnectedListener;

public class DisconnectedScreen implements Screen, DisconnectedListener {

	private MultiBomberMain main;
	
	private SpriteBatch batch;
	private Stage stage;
	
	public DisconnectedScreen(MultiBomberMain m) {
		main = m;
		batch = new SpriteBatch();
		
		Skin skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));
		stage = new Stage();
		
		Dialog dia = new Dialog("You've been disconnected from the server", skin);
		dia.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(dia);
	}
	
	public void render(float delta) {
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
	public void disconnectedFromServer() {
		main.setScreen(this);
	}
}
