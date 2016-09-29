package com.mda.bomb.entity;

import com.mda.bomb.ecs.components.FlameComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.entity.animation.AnimationFactory;

public class FlameFactory {
	private static int flameID = 0;
	
	public static void createFlame(float x, float y) {
		Entity e = new Entity(--flameID);
		e.addComponent(new SpriteComponent(AnimationFactory.getFlameAnimation()));
		e.addComponent(new PositionComponent(x, y));
		e.addComponent(new FlameComponent());
	}
}
