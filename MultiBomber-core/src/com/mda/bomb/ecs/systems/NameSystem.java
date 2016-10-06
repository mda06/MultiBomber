package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.HealthComponent;
import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.RenderSystem;

public class NameSystem extends RenderSystem {
	private PositionComponent pc;
	private NameComponent name;
	private CollisionComponent cc;
	private HealthComponent hc;
	private static BitmapFont font = new BitmapFont();
	
	@Override
	public void update(float dt, Entity e) {}

	@Override
	public void render(Entity e, SpriteBatch batch) {
		pc = e.getAs(PositionComponent.class);
		name = e.getAs(NameComponent.class);
		cc = e.getAs(CollisionComponent.class);
		hc = e.getAs(HealthComponent.class);
		if(pc == null || name == null || cc == null) return;
		
		font.setColor(Color.WHITE);
		drawCentred(batch, name.name, 0);
		if(hc == null) return;
		font.setColor(Color.RED);
		String txt = "Health: " + hc.health;
		drawCentred(batch, txt, font.getBounds(txt).height);
	}
	
	private void drawCentred(SpriteBatch batch, String txt, float offY) {
		float w = font.getBounds(txt).width;
		font.drawMultiLine(batch, txt, pc.x - w / 2, pc.y - cc.size.y / 2 + cc.offset.y - offY);
	}
	
	public void dispose() {
		//font.dispose();
	}
}