package com.mda.bomb.entity.animation;

public class AnimationFactory {
	public static SimpleAnimation getSimpleAnimationOfSpriteNb(int nb) {
		if(nb == 1) {
			return new SimpleAnimation(8, "Sprites/Bomberman/Front/Bman_F_f0");
		} else {
			return new SimpleAnimation(6, "Sprites/Creep/Front/Creep_F_f0");
		}
	}
}
