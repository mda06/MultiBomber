package com.mda.bomb.ecs.systems;

import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;

public class MovementSystem extends BaseSystem {
	private PositionComponent pc;
	private MovementComponent mc;
	
	public void update(float dt, Entity e) {
		pc = e.getAs(PositionComponent.class);
		mc = e.getAs(MovementComponent.class);
		if(pc == null || mc == null) return;
		
		pc.x += mc.velX * dt;
		pc.y += mc.velY * dt;
	}
}
