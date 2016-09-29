package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.RenderSystem;

public class NameSystem extends RenderSystem {
	private PositionComponent pc;
	private NameComponent name;
	private CollisionComponent cc;
	private static BitmapFont font = new BitmapFont();
	
	@Override
	public void update(float dt, Entity e) {}

	@Override
	public void render(Entity e, SpriteBatch batch) {
		pc = e.getAs(PositionComponent.class);
		name = e.getAs(NameComponent.class);
		cc = e.getAs(CollisionComponent.class);
		if(pc == null || name == null) return;
		
		float w = font.getBounds(name.name).width;
		font.draw(batch, name.name, pc.x - w / 2, pc.y - cc.size.y / 2 + cc.offset.y);
	}
	
	public void dispose() {
		font.dispose();
	}
}