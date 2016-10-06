package com.mda.bomb.ecs.components;

import java.util.HashMap;

import com.mda.bomb.ecs.core.Component;
import com.mda.bomb.entity.animation.AnimationFactory;
import com.mda.bomb.entity.animation.SimpleAnimation;

public class SpriteComponent extends Component {
	public int ID;
	public enum AnimationHash {
		ANIM_SIMPLE, ANIM_FRONT, ANIM_BACK, ANIM_LEFT, ANIM_RIGHT;
	}
	public HashMap<AnimationHash, SimpleAnimation> animations;
	public AnimationHash oldAnim = null;
	public float scaleX, scaleY, rotation;
	
	public SpriteComponent(int id) {
		ID = id;
		animations = new HashMap<AnimationHash, SimpleAnimation>();
		scaleX = scaleY = 1;
		rotation = 0;
	}
	
	public SpriteComponent(SimpleAnimation anim) {
		animations = new HashMap<AnimationHash, SimpleAnimation>();
		animations.put(AnimationHash.ANIM_SIMPLE, anim);
		scaleX = scaleY = 1;
		rotation = 0;
	}
 	
	public void initAnimation() {
		if(ID == 1) {
			animations.put(AnimationHash.ANIM_FRONT, AnimationFactory.getBombermanFrontAnimation());
			animations.put(AnimationHash.ANIM_BACK, AnimationFactory.getBombermanBackAnimation());
			animations.put(AnimationHash.ANIM_LEFT, AnimationFactory.getBombermanSideAnimation());
			animations.put(AnimationHash.ANIM_RIGHT, AnimationFactory.getBombermanSideAnimation());
		} else {
			animations.put(AnimationHash.ANIM_FRONT, AnimationFactory.getCreepFrontAnimation());
			animations.put(AnimationHash.ANIM_BACK, AnimationFactory.getCreepBackAnimation());
			animations.put(AnimationHash.ANIM_LEFT, AnimationFactory.getCreepSideAnimation());
			animations.put(AnimationHash.ANIM_RIGHT, AnimationFactory.getCreepSideAnimation());
		}
	}
	
	public SimpleAnimation getAnimation(AnimationHash anim) {
		return animations.get(anim);
	}
}
