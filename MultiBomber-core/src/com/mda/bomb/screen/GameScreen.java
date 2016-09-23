package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.ecs.systems.SpriteSystem;
import com.mda.bomb.screen.event.GameListener;

public class GameScreen implements Screen, GameListener {

	private MultiBomberMain main;
	private SpriteBatch batch;
	private boolean hasInitGameOnFirstUpdate;
	
	public GameScreen(MultiBomberMain m) {
		main = m;
		batch = new SpriteBatch();
		hasInitGameOnFirstUpdate = false;
	}
	
	private void update(float dt) {
		//Need to do this on this way because in sync we are in another thread and we can't use opengl features...
		if(!hasInitGameOnFirstUpdate) {
			main.getClientSide().getEngine().addSystem(new SpriteSystem());
			
			//init sprites and basics game system
			for (Entity entity : main.getClientSide().getEngine().getSystem(EntitySystem.class).getEntities().values()) {
				entity.getAs(SpriteComponent.class).initAnimation();
				entity.addComponent(new PositionComponent((float)Math.random() * Gdx.graphics.getWidth(), (float)Math.random() * Gdx.graphics.getHeight()));
				entity.addComponent(new MovementComponent());
			}
			
			//Need the correct input system for our entity
			main.getClientSide().getMyEntity().getAs(PositionComponent.class).x = 50;
			main.getClientSide().getMyEntity().getAs(PositionComponent.class).y = 50;
			
			hasInitGameOnFirstUpdate = true;
		}
		main.getClientSide().getEngine().update(dt);
	}
	
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(.8f, .2f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		main.getClientSide().getEngine().render(batch);
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
