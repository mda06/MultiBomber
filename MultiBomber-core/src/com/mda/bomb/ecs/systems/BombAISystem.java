package com.mda.bomb.ecs.systems;

import com.mda.bomb.ecs.components.BombAIComponent;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.screen.event.BombExplodeListener;

public class BombAISystem extends BaseSystem {
	private BombExplodeListener listener;
	private BombAIComponent bc;
	
	public BombAISystem(BombExplodeListener list) {
		listener = list;
	}
	
	public void update(float dt, Entity e) {
		bc = e.getAs(BombAIComponent.class);
		if(bc == null) return;
		
		bc.explodeTime -= dt;
		if(bc.explodeTime < 0 && !bc.isExploded) {
			bc.isExploded = true;
			if(listener != null)
				listener.explode(e);
		}
	}

}
