package com.mda.bomb.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SimpleAnimation {
	private TextureRegion[] frames;
	private TextureRegion currentFrame;
	private float stateTime;
	private Animation animation;
	
	public SimpleAnimation(String internalPath) {
		stateTime = 0;
		frames = new TextureRegion[1];
		frames[0] = new TextureRegion(new Texture(Gdx.files.internal(internalPath)));
		animation = new Animation(0, frames);
		update(0);
	}
	
	public SimpleAnimation(int nb_sprites, String internalPath) {
		this(nb_sprites, internalPath, 0.05f);
	}
	
	public SimpleAnimation(int nb_sprites, String internalPath, float animTime) {
		stateTime = 0;
		frames = new TextureRegion[nb_sprites];
		for(int i = 0; i < nb_sprites; i++) 
			frames[i] = new TextureRegion(new Texture(Gdx.files.internal(internalPath + i + ".png")));
		animation = new Animation(animTime, frames);
		update(0);
	}
	
	public void update(float dt) {
		stateTime += dt;
		currentFrame = animation.getKeyFrame(stateTime);
	}
	
	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public void resetAnim() {
		stateTime = 0;
		currentFrame = animation.getKeyFrame(stateTime);
	}
	
	public void dispose() {
		for(TextureRegion tr : frames)
			tr.getTexture().dispose();
	}
}
