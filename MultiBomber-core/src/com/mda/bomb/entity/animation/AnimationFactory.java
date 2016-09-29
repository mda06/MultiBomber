package com.mda.bomb.entity.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class AnimationFactory {
	
	public static SimpleAnimation getBombermanFrontAnimation() {
		SimpleAnimation anim = new SimpleAnimation(8, "Sprites/Bomberman/Front/Bman_F_f0");
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}

	public static SimpleAnimation getBombermanBackAnimation() {
		SimpleAnimation anim = new SimpleAnimation(8, "Sprites/Bomberman/Back/Bman_B_f0");
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}

	public static SimpleAnimation getBombermanSideAnimation() {
		SimpleAnimation anim = new SimpleAnimation(8, "Sprites/Bomberman/Side/Bman_F_f0");
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}
	
	public static SimpleAnimation getCreepFrontAnimation() {
		SimpleAnimation anim = new SimpleAnimation(6, "Sprites/Creep/Front/Creep_F_f0");
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}

	public static SimpleAnimation getCreepBackAnimation() {
		SimpleAnimation anim = new SimpleAnimation(6, "Sprites/Creep/Back/Creep_B_f0");
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}

	public static SimpleAnimation getCreepSideAnimation() {
		SimpleAnimation anim = new SimpleAnimation(6, "Sprites/Creep/Side/Creep_S_f0");
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}

	public static SimpleAnimation getBombAnimation() {
		SimpleAnimation anim = new SimpleAnimation(3, "Sprites/Bomb/Bomb_f0");
		anim.getAnimation().setFrameDuration(.23f);
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}

	public static SimpleAnimation getFlameAnimation() {
		SimpleAnimation anim = new SimpleAnimation(5, "Sprites/Flame/Flame_f0");
		anim.getAnimation().setFrameDuration(.3f);
		anim.getAnimation().setPlayMode(PlayMode.LOOP);
		return anim;
	}
	
	public static SimpleAnimation getPowerupBomb() {
		SimpleAnimation anim = new SimpleAnimation("Sprites/Powerups/BombPowerup.png");
		return anim;
	}
}
