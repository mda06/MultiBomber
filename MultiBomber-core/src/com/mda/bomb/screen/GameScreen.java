package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.ecs.systems.InputSystem;
import com.mda.bomb.ecs.systems.SpriteSystem;
import com.mda.bomb.map.Map;
import com.mda.bomb.network.sync.DirectionSync;
import com.mda.bomb.screen.event.ChangeDirectionListener;
import com.mda.bomb.screen.event.GameListener;

public class GameScreen implements Screen, GameListener, ChangeDirectionListener {

	private MultiBomberMain main;
	
	private OrthographicCamera cam;
	private SpriteBatch batch;
	private boolean hasInitGameOnFirstUpdate;
	private Map map;
	
	public GameScreen(MultiBomberMain m) {
		main = m;
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.update();
		hasInitGameOnFirstUpdate = false;
		map = new Map();
		map.initMap();
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
		updateCam();
	}
	
	public void updateCam() {
		Entity e = main.getClientSide().getMyEntity();
		PositionComponent pc = e.getAs(PositionComponent.class);
		float x = pc.x, y = pc.y;
		cam.position.set(x, y, 0);
		cam.update();
	}
	
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(.8f, .2f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		map.render(batch);
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
		map.dispose();
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
