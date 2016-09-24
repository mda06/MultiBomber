package com.mda.bomb.ecs.systems;

import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;

public class MovementSystem extends BaseSystem {
	private PositionComponent pc;
	private MovementComponent mc;
	private DirectionComponent dc;
	
	public void update(float dt, Entity e) {
		pc = e.getAs(PositionComponent.class);
		mc = e.getAs(MovementComponent.class);
		dc = e.getAs(DirectionComponent.class);
		if(pc == null || mc == null || dc == null) return;
		
		switch(dc.direction) {
		case STOP : break;
		case UP : pc.y += mc.velY * dt; break;
		case DOWN : pc.y -= mc.velY * dt; break;
		case LEFT : pc.x -= mc.velX * dt; break;
		case RIGHT : pc.x += mc.velX * dt; break;
		}
	}
}
