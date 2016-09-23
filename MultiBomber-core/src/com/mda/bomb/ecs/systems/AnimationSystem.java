package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.ecs.components.AnimationComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.RenderSystem;
import com.mda.bomb.ecs.core.SimpleAnimationEntity;

public class AnimationSystem extends RenderSystem {
	private AnimationComponent anim;
	private PositionComponent pc;
	
	public void update(float dt, SimpleAnimationEntity a) {
		a.ac.stateTime += dt;
		a.ac.currentFrame = a.ac.animation.getKeyFrame(a.ac.stateTime);
	}
	
	public void update(float dt, Entity e) {
		anim = e.getAs(AnimationComponent.class);
		if(anim == null) return;
		
		anim.stateTime += dt;
		anim.currentFrame = anim.animation.getKeyFrame(anim.stateTime);
	}

	public void render(Entity e, SpriteBatch batch) {
		anim = e.getAs(AnimationComponent.class);
		pc = e.getAs(PositionComponent.class);
		if(pc == null || anim == null) return;
		
		batch.draw(anim.currentFrame, pc.x, pc.y);
	}
	
	public void render(SimpleAnimationEntity a, SpriteBatch batch) {
		batch.draw(a.ac.currentFrame, a.pc.x, a.pc.y);
	}
}
