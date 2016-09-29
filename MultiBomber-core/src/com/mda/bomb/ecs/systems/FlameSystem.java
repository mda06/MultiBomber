package com.mda.bomb.ecs.systems;

import com.mda.bomb.ecs.components.FlameComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;

public class FlameSystem extends BaseSystem{
	private FlameComponent fc;
	
	public void update(float dt, Entity e) {
		fc = e.getAs(FlameComponent.class);
		if(fc == null) return;
		
		fc.renderTime -= dt;
		if(fc.renderTime < 0)
			fc.isRenderTimeFinish = true;
	}
}
