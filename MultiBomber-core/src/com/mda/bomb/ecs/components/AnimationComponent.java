package com.mda.bomb.ecs.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mda.bomb.ecs.core.Component;

public class AnimationComponent extends Component {
	public TextureRegion[] frames;
	public TextureRegion currentFrame;
	public float stateTime, animTime;
	public Animation animation;


	public AnimationComponent(int nb_sprites, String internalPath) {
		this(nb_sprites, internalPath, 0.05f);
	}
	
	public AnimationComponent(int nb_sprites, String internalPath, float animTime) {
		this.animTime = animTime;
		stateTime = 0;
		frames = new TextureRegion[nb_sprites];
		for(int i = 0; i < nb_sprites; i++) 
			frames[i] = new TextureRegion(new Texture(Gdx.files.internal(internalPath + i + ".png")));
		animation = new Animation(animTime, frames);
	}
	
	public void dispose() {
		for(TextureRegion tr : frames)
			tr.getTexture().dispose();
	}
}
