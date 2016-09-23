package com.mda.bomb.ecs.core;

import com.mda.bomb.ecs.components.AnimationComponent;
import com.mda.bomb.ecs.components.PositionComponent;

public class SimpleAnimationEntity {
	public PositionComponent pc;
	public AnimationComponent ac;
	
	public SimpleAnimationEntity(PositionComponent pc, AnimationComponent ac) {
		this.pc = pc;
		this.ac = ac;
	}
}
