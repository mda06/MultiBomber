package com.mda.bomb.screen.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundActor extends Actor {

	private Sprite sprite;

	public BackgroundActor() {
		sprite = new Sprite(new Texture(Gdx.files.internal("Sprites/Menu/title_background.jpg")));
		sprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
	}
}
