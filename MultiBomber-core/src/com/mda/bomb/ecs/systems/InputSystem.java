package com.mda.bomb.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.DirectionComponent.HorizontalDirection;
import com.mda.bomb.ecs.components.DirectionComponent.VerticalDirection;
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
		
		HorizontalDirection newHD = HorizontalDirection.STOP;
		if(Gdx.input.isKeyPressed(Keys.LEFT)) newHD = HorizontalDirection.LEFT;
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)) newHD = HorizontalDirection.RIGHT;
		else newHD = HorizontalDirection.STOP;
		
		VerticalDirection newVD = VerticalDirection.STOP;
		if(Gdx.input.isKeyPressed(Keys.DOWN)) newVD = VerticalDirection.DOWN;
		else if(Gdx.input.isKeyPressed(Keys.UP)) newVD = VerticalDirection.UP;
		else newVD = VerticalDirection.STOP;
		
		
		if(newHD != dc.horizontalDirection || newVD != dc.verticalDirection) {
			dc.horizontalDirection = newHD;
			dc.verticalDirection = newVD;
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
