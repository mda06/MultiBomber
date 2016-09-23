package com.mda.bomb.ecs.systems;

import com.mda.bomb.ecs.components.NameComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;

public class NameSystem extends BaseSystem {
	private PositionComponent pc;
	private NameComponent name;
	
	public void update(float dt, Entity e) {
		pc = e.getAs(PositionComponent.class);
		name = e.getAs(NameComponent.class);
		if(pc == null || name == null) return;
		
		System.out.println(name.name + " is at " + pc.x + "/" + pc.y);
	}
}