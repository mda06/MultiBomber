package com.mda.bomb.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.ecs.components.BombAIComponent;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.FlameComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.components.SpriteFeature;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.ecs.systems.FlameSystem;
import com.mda.bomb.ecs.systems.InputSystem;
import com.mda.bomb.ecs.systems.MovementSystem;
import com.mda.bomb.ecs.systems.NameSystem;
import com.mda.bomb.ecs.systems.SpriteFeatureSystem;
import com.mda.bomb.ecs.systems.SpriteSystem;
import com.mda.bomb.entity.BombQueue;
import com.mda.bomb.entity.EntityQueue;
import com.mda.bomb.entity.FlameFactory;
import com.mda.bomb.entity.PowerupQueue;
import com.mda.bomb.entity.PowerupQueue.Powerup;
import com.mda.bomb.entity.animation.AnimationFactory;
import com.mda.bomb.map.Map;
import com.mda.bomb.network.sync.game.DirectionSync;
import com.mda.bomb.network.sync.game.DropBombSync;
import com.mda.bomb.screen.event.GameListener;

public class GameScreen implements Screen, GameListener {

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
		map = main.getClientSide().getMap();
	}

	private void update(float dt) {
		// Need to do this on this way because in sync we are in another thread
		// and we can't use opengl features...
		if (!hasInitGameOnFirstUpdate) {
			main.getClientSide().getEngine().addSystem(new FlameSystem());
			main.getClientSide().getEngine().addSystem(new SpriteSystem());
			main.getClientSide().getEngine().addSystem(new NameSystem());
			main.getClientSide().getEngine().addSystem(new SpriteFeatureSystem());
			main.getClientSide().getEngine().addSystem(new InputSystem(this));
			// Test for less latency
			main.getClientSide().getEngine().addSystem(new MovementSystem(map));

			// init sprites
			for (Entity entity : main.getClientSide().getEngine().getSystem(EntitySystem.class).getEntities().values()) {
				entity.getAs(SpriteComponent.class).initAnimation();
			}

			hasInitGameOnFirstUpdate = true;
		}

		main.getClientSide().getEngine().update(dt);
		updateCam(dt);
		updateBombQueue();
		updateExplodedBombs();
		updateFinishFlames();
		updatePowerupQueue();
		updateEntitiesToRemove();
	}
	
	private void updateEntitiesToRemove() {
		Integer id = null;
		while ((id = EntityQueue.pollEntityToRemove()) != null) {
			main.getClientSide().getEngine().getSystem(EntitySystem.class).removeEntity(id);
		}
	}

	private void updatePowerupQueue() {
		Powerup pow = null;
		while ((pow = PowerupQueue.poll()) != null) {
			Entity e = new Entity(pow.ID);
			e.addComponent(pow.pc);
			// Switch type...
			e.addComponent(new SpriteComponent(AnimationFactory.getPowerupBomb()));
		}
	}

	private void updateFinishFlames() {
		Iterator<Entry<Integer, Entity>> it = main.getClientSide().getEngine().getSystem(EntitySystem.class).getEntities().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Entity> item = it.next();
			FlameComponent fc = item.getValue().getAs(FlameComponent.class);
			if (fc != null && fc.isRenderTimeFinish) {
				it.remove();
			}
		}
	}

	private void updateExplodedBombs() {
		List<Vector2> lstPos = new ArrayList<Vector2>();
		Iterator<Entry<Integer, Entity>> it = main.getClientSide().getEngine().getSystem(EntitySystem.class).getEntities().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Entity> item = it.next();
			BombAIComponent ai = item.getValue().getAs(BombAIComponent.class);
			if (ai != null && ai.isExploded) {
				PositionComponent pc = item.getValue().getAs(PositionComponent.class);
				Vector2 tilePos = map.getTilePositionWithAbsolutePosition(pc.x, pc.y);
				int tx = (int) tilePos.x, ty = (int) tilePos.y;
				int ts = map.getTileSize();
				for (int x = tx - ai.explodeSize + 1; x < tx + ai.explodeSize; x++) {
					lstPos.add(new Vector2(x * ts + (pc.x % ts), pc.y));
				}
				for (int y = ty - ai.explodeSize + 1; y < ty + ai.explodeSize; y++) {
					lstPos.add(new Vector2(pc.x, y * ts + (pc.y % ts)));
				}
				it.remove();
			}
		}

		for (Vector2 v2 : lstPos)
			FlameFactory.createFlame(v2.x, v2.y);
	}

	private void updateBombQueue() {
		BombQueue.Bomb bomb = null;
		while ((bomb = BombQueue.poll()) != null) {
			Entity e = new Entity(bomb.ID);
			e.addComponent(bomb.pc);
			e.addComponent(new BombAIComponent());
			e.addComponent(new SpriteFeature());
			e.addComponent(new SpriteComponent(AnimationFactory.getBombAnimation()));
		}
	}

	private void updateCam(float dt) {
		Entity e = main.getClientSide().getMyEntity();
		PositionComponent pc = e.getAs(PositionComponent.class);

		float camSpeed = 3.5f;
		float x = MathUtils.lerp(cam.position.x, pc.x, camSpeed * dt);
		float y = MathUtils.lerp(cam.position.y, pc.y, camSpeed * dt);

		if (x < Gdx.graphics.getWidth() / 2) x = Gdx.graphics.getWidth() / 2;
		else if (x > map.getAbsoluteWidth() - Gdx.graphics.getWidth() / 2) x = map.getAbsoluteWidth() - Gdx.graphics.getWidth() / 2;
		if (y < Gdx.graphics.getHeight() / 2) y = Gdx.graphics.getHeight() / 2;
		else if (y > map.getAbsoluteHeight() - Gdx.graphics.getHeight() / 2) y = map.getAbsoluteHeight() - Gdx.graphics.getHeight() / 2;
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

	public void resize(int width, int height) {
	}

	public void show() {
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

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

	@Override
	public void dropBomb(Entity e) {
		DropBombSync sync = new DropBombSync();
		sync.entityID = e.getID();
		PositionComponent pc = e.getAs(PositionComponent.class);
		sync.bombPos = new Vector2(pc.x, pc.y);
		main.getClientSide().sendTCP(sync);
	}
}
