package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.RenderSystem;

public class SpriteSystem extends RenderSystem {
	private SpriteComponent sc;
	private PositionComponent pc;
	private Texture collisionViewer;
	
	public SpriteSystem() {
		Pixmap pix = new Pixmap(64, 64, Format.RGBA8888);
		pix.setColor(1, .2f, .2f, .4f);
		pix.fill();
		collisionViewer = new Texture(pix);
	}
	
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

		CollisionComponent cc = e.getAs(CollisionComponent.class);
		if(cc != null) {
			batch.draw(collisionViewer, pc.x - cc.size.x / 2 + cc.offset.x, pc.y - cc.size.y / 2 + cc.offset.y, cc.size.x, cc.size.y);
		}
		
		TextureRegion tr = sc.testAnimation.getCurrentFrame();
		int w = tr.getRegionWidth(), h = tr.getRegionHeight();
		batch.draw(sc.testAnimation.getCurrentFrame(), pc.x - w / 2, pc.y - h / 2);
		
	}
}
