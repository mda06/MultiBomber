package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.DirectionComponent.Direction;
import com.mda.bomb.ecs.components.MovementComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.map.Map;

public class MovementSystem extends BaseSystem {
	private PositionComponent pc;
	private MovementComponent mc;
	private DirectionComponent dc;
	private CollisionComponent cc;
	private Map map;

	public MovementSystem(Map map) {
		this.map = map;
	}

	public void update(float dt, Entity e) {
		pc = e.getAs(PositionComponent.class);
		mc = e.getAs(MovementComponent.class);
		dc = e.getAs(DirectionComponent.class);
		cc = e.getAs(CollisionComponent.class);
		if (pc == null || mc == null || dc == null)
			return;

		updateVelX(dt);
		updateVelY(dt);

		Vector2 movement = new Vector2();
		if(cc != null) {
			movement.x = checkColX(dt);
			movement.y = checkColY(dt);
		} else {
			movement.x = mc.vel.x * dt;
			movement.y = mc.vel.y * dt;
		}
		
		pc.x += movement.x;
		pc.y += movement.y;
	}

	private void updateVelX(float dt) {
		if (dc.direction == Direction.LEFT)
			mc.vel.x -= mc.accel.x * dt;
		else if (dc.direction == Direction.RIGHT)
			mc.vel.x += mc.accel.x * dt;
		else 
			deccelX(dt);

		if (mc.vel.x > mc.maxVel.x)
			mc.vel.x = mc.maxVel.x;
		if (mc.vel.x < -mc.maxVel.x)
			mc.vel.x = -mc.maxVel.x;
	}
	
	private void deccelX(float dt) {
		if (mc.vel.x > 0) {
			mc.vel.x -= mc.deccel.x * dt;
			if (mc.vel.x < 0)
				mc.vel.x = 0;
		} else {
			mc.vel.x += mc.deccel.x * dt;
			if (mc.vel.x > 0)
				mc.vel.x = 0;
		}
	}

	private void updateVelY(float dt) {
		if (dc.direction == Direction.DOWN)
			mc.vel.y -= mc.accel.y * dt;
		else if (dc.direction == Direction.UP)
			mc.vel.y += mc.accel.y * dt;
		else
			deccelY(dt);

		if (mc.vel.y > mc.maxVel.y)
			mc.vel.y = mc.maxVel.y;
		if (mc.vel.y < -mc.maxVel.y)
			mc.vel.y = -mc.maxVel.y;
	}
	
	private void deccelY(float dt) {
		if (mc.vel.y > 0) {
			mc.vel.y -= mc.deccel.y * dt;
			if (mc.vel.y < 0)
				mc.vel.y = 0;
		} else {
			mc.vel.y += mc.deccel.y * dt;
			if (mc.vel.y > 0)
				mc.vel.y = 0;
		}
	}

	private float checkColX(float dt) {
		float x = pc.x + mc.vel.x * dt;
		
		float halfW = cc.size.x / 2, halfH = cc.size.y / 2;
		float testX = x;
		
		if(mc.vel.x > 0) testX += halfW;
		else if(mc.vel.x < 0)	testX -= halfW;
		else return 0;
		
		
		if(map.canWalk(testX, pc.y - halfH) && map.canWalk(testX, pc.y + halfH)) 
			x = mc.vel.x * dt;
		else {
			x = 0;
			mc.vel.x = 0;
		}
		
		return x;
	}

	private float checkColY(float dt) {
		float y = pc.y + mc.vel.y * dt;
	
		float halfW = cc.size.x / 2, halfH = cc.size.y / 2;
		float testY = y;
		
		if(mc.vel.y > 0) testY += halfH;
		else if(mc.vel.y < 0) testY -= halfH;
		else return 0;
		
		
		if(map.canWalk(pc.x - halfW, testY) && map.canWalk(pc.x + halfW, testY)) 
			y = mc.vel.y * dt;
		else {
			y = 0;
			mc.vel.y = 0;
		}
		
		return y;
	}

}
