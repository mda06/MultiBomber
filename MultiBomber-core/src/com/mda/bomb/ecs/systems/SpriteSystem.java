package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.DirectionComponent.HorizontalDirection;
import com.mda.bomb.ecs.components.DirectionComponent.VerticalDirection;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.components.SpriteComponent.AnimationHash;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.RenderSystem;
import com.mda.bomb.entity.animation.SimpleAnimation;

public class SpriteSystem extends RenderSystem {
	private SpriteComponent sc;
	private PositionComponent pc;
	private DirectionComponent dc;
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
		dc = e.getAs(DirectionComponent.class);
		if (sc == null)
			return;

		SimpleAnimation anim = sc.getAnimation(AnimationHash.ANIM_SIMPLE);
		if(anim != null) {
			anim.update(dt);
		} else if(dc != null) {
			anim = getAnimOfDirection();
			
			if (dc.horizontalDirection != HorizontalDirection.STOP || dc.verticalDirection != VerticalDirection.STOP) {
				anim.update(dt);
			} else {
				MovementComponent mc = e.getAs(MovementComponent.class);
				if (mc != null) {
					if (mc.vel.x != 0 || mc.vel.y != 0) {
						anim.update(dt);
					} else {
						anim.resetAnim();
					}
				}
			}
			
			if (dc.horizontalDirection == HorizontalDirection.LEFT)
				sc.oldAnim = (AnimationHash.ANIM_LEFT);
			else if (dc.horizontalDirection == HorizontalDirection.RIGHT)
				sc.oldAnim = (AnimationHash.ANIM_RIGHT);
			else if (dc.verticalDirection == VerticalDirection.UP)
				sc.oldAnim = (AnimationHash.ANIM_BACK);
			else if (dc.verticalDirection == VerticalDirection.DOWN)
				sc.oldAnim = (AnimationHash.ANIM_FRONT);
		}
	}

	private SimpleAnimation getAnimOfDirection() {
		if (dc.horizontalDirection == HorizontalDirection.LEFT)
			return sc.getAnimation(AnimationHash.ANIM_LEFT);
		else if (dc.horizontalDirection == HorizontalDirection.RIGHT)
			return sc.getAnimation(AnimationHash.ANIM_RIGHT);
		else if (dc.verticalDirection == VerticalDirection.UP)
			return sc.getAnimation(AnimationHash.ANIM_BACK);
		else if (dc.verticalDirection == VerticalDirection.DOWN)
			return sc.getAnimation(AnimationHash.ANIM_FRONT);
		else if(sc.oldAnim == null)
			return sc.getAnimation(AnimationHash.ANIM_FRONT);
		else 
			return sc.getAnimation(sc.oldAnim);
	}

	@Override
	public void render(Entity e, SpriteBatch batch) {
		sc = e.getAs(SpriteComponent.class);
		pc = e.getAs(PositionComponent.class);
		dc = e.getAs(DirectionComponent.class);
		if (sc == null || pc == null)
			return;

		CollisionComponent cc = e.getAs(CollisionComponent.class);
		if (cc != null) {
			batch.draw(collisionViewer, pc.x - cc.size.x / 2 + cc.offset.x, pc.y - cc.size.y / 2 + cc.offset.y,
					cc.size.x, cc.size.y);
		}

		SimpleAnimation anim = sc.getAnimation(AnimationHash.ANIM_SIMPLE);
		if(dc != null)
			anim = getAnimOfDirection();
		
		TextureRegion tr = anim.getCurrentFrame();
		int w = tr.getRegionWidth(), h = tr.getRegionHeight();
		if (dc != null && (dc.horizontalDirection == HorizontalDirection.LEFT || sc.oldAnim == AnimationHash.ANIM_LEFT))
			batch.draw(anim.getCurrentFrame().getTexture(), pc.x - w / 2, pc.y - h / 2, tr.getRegionWidth(),
					tr.getRegionHeight(), tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight(),
					true, false);
		else
			batch.draw(anim.getCurrentFrame(), pc.x - w / 2, pc.y - h / 2);
	}
}
