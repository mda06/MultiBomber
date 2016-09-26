package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.RenderSystem;

public class NameSystem extends RenderSystem {
	private PositionComponent pc;
	private NameComponent name;
	private static BitmapFont font = new BitmapFont();
	
	@Override
	public void update(float dt, Entity e) {}

	@Override
	public void render(Entity e, SpriteBatch batch) {
		pc = e.getAs(PositionComponent.class);
		name = e.getAs(NameComponent.class);
		if(pc == null || name == null) return;
		
		font.draw(batch, name.name, pc.x + 15, pc.y - 10);
	}
	
	public void dispose() {
		font.dispose();
	}
}