package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.DirectionComponent.Direction;
import com.mda.bomb.ecs.components.InputComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.screen.event.ChangeDirectionListener;

public class InputSystem extends BaseSystem {

	private DirectionComponent dc;
	private ChangeDirectionListener listener;
	
	public InputSystem(ChangeDirectionListener list) {
		this.listener = list;
	}
	
	public void update(float dt, Entity e) {
		if(e.getAs(InputComponent.class) == null) return;
		
		dc = e.getAs(DirectionComponent.class);
		if(dc == null) return;
		
		Direction newDirection = Direction.STOP;
		if(Gdx.input.isKeyPressed(Keys.LEFT)) newDirection = Direction.LEFT;
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)) newDirection = Direction.RIGHT;
		else if(Gdx.input.isKeyPressed(Keys.DOWN)) newDirection = Direction.DOWN;
		else if(Gdx.input.isKeyPressed(Keys.UP)) newDirection = Direction.UP;
		else newDirection = Direction.STOP;
		
		if(newDirection != dc.direction) {
			dc.direction = newDirection;
			if(listener != null)
				listener.directionChanged(e);
		}
	}
}
