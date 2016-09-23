package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.screen.event.GameListener;

public class GameScreen implements Screen, GameListener {

	private MultiBomberMain main;
	private SpriteBatch batch;
	
	public GameScreen(MultiBomberMain m) {
		main = m;
		batch = new SpriteBatch();
	}
	
	private void update(float dt) {
		main.getClientSide().getEngine().update(dt);
	}
	
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(.8f, .2f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.end();
	}

	public void resize(int width, int height) {}
	public void show() {}
	public void hide() {}
	public void pause() {}
	public void resume() {}
	
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void startGame() {
		main.setScreen(this);
	}
}
