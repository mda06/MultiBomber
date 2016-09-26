package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.DirectionComponent.Direction;
import com.mda.bomb.ecs.components.DropBombComponent;
import com.mda.bomb.ecs.components.InputComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.screen.event.GameListener;

public class InputSystem extends BaseSystem {

	private DirectionComponent dc;
	private DropBombComponent bombComp;
	private GameListener listener;
	
	public InputSystem(GameListener list) {
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
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			bombComp = e.getAs(DropBombComponent.class);
			if(bombComp != null) {
				if(bombComp.nbOfBombs > 0) {
					listener.dropBomb(e);
				}
			}
		}
	}
}
