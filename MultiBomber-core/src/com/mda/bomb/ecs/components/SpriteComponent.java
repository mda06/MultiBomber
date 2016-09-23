package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;
import com.mda.bomb.entity.animation.AnimationFactory;
import com.mda.bomb.entity.animation.SimpleAnimation;

public class SpriteComponent extends Component {
	public int ID;
	public SimpleAnimation testAnimation;
	
	public SpriteComponent(int id) {
		ID = id;
		testAnimation = null;
	}
	
	public void initAnimation() {
		testAnimation = AnimationFactory.getSimpleAnimationOfSpriteNb(ID);
	}
}
