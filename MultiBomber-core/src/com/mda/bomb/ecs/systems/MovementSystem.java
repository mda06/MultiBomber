package com.mda.bomb.ecs.systems;

import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SizeComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.map.Map;

public class MovementSystem extends BaseSystem {
	private PositionComponent pc;
	private MovementComponent mc;
	private DirectionComponent dc;
	private SizeComponent sc;
	private CollisionComponent cc;
	private Map map;
	
	public MovementSystem(Map map) {
		this.map = map;
	}
	
	public void update(float dt, Entity e) {
		pc = e.getAs(PositionComponent.class);
		mc = e.getAs(MovementComponent.class);
		dc = e.getAs(DirectionComponent.class);
		sc = e.getAs(SizeComponent.class);
		cc = e.getAs(CollisionComponent.class);
		if(pc == null || mc == null || dc == null) return;
		
		float newX = pc.x, newY = pc.y;
		switch(dc.direction) {
		case STOP : break;
		case UP : newY += mc.velY * dt; break;
		case DOWN : newY -= mc.velY * dt; break;
		case LEFT : newX -= mc.velX * dt; break;
		case RIGHT : newX += mc.velX * dt; break;
		}
		
		if(sc != null && cc != null) {
			if(!map.canWalk(newX, newY)) {
				newX = pc.x;
				newY = pc.y;
			} else if(!map.canWalk(newX + sc.size.x, newY + sc.size.y)) {
				newX = pc.x;
				newY = pc.y;
			} 
		} 
		
		pc.x = newX;
		pc.y = newY;
	}
}
