package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.RenderSystem;

public class SpriteSystem extends RenderSystem {
	private SpriteComponent sc;
	private PositionComponent pc;
	
	@Override
	public void update(float dt, Entity e) {
		sc = e.getAs(SpriteComponent.class);
		if(sc == null) return;
		
		sc.testAnimation.update(dt);
	}

	@Override
	public void render(Entity e, SpriteBatch batch) {
		sc = e.getAs(SpriteComponent.class);
		pc = e.getAs(PositionComponent.class);
		if(sc == null || pc == null) return;
		
		batch.draw(sc.testAnimation.getCurrentFrame(), pc.x, pc.y);
	}
}
