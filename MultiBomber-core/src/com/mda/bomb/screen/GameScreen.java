package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.ecs.systems.InputSystem;
import com.mda.bomb.ecs.systems.SpriteSystem;
import com.mda.bomb.network.sync.DirectionSync;
import com.mda.bomb.screen.event.ChangeDirectionListener;
import com.mda.bomb.screen.event.GameListener;

public class GameScreen implements Screen, GameListener, ChangeDirectionListener {

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
			main.getClientSide().getEngine().addSystem(new InputSystem(this));
			
			//init sprites 
			for (Entity entity : main.getClientSide().getEngine().getSystem(EntitySystem.class).getEntities().values()) {
				entity.getAs(SpriteComponent.class).initAnimation();
			}
			
			//Need the correct input system for our entity
			
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

	@Override
	public void directionChanged(Entity e) {
		DirectionSync sync = new DirectionSync();
		sync.entityID = e.getID();
		sync.directionComp = e.getAs(DirectionComponent.class);
		main.getClientSide().getClient().sendTCP(sync);
	}
}
