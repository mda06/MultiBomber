package com.mda.bomb.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.mda.bomb.ecs.components.AnimationComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.SimpleAnimationEntity;
import com.mda.bomb.ecs.systems.AnimationSystem;

public class TestScreen implements Screen {

	private SimpleAnimationEntity anim1, anim2;
	private AnimationSystem anim;
	private SpriteBatch batch;
	
	public TestScreen() {
		anim1 = new SimpleAnimationEntity(new PositionComponent(50, 50), new AnimationComponent(8, "Sprites/Bomberman/Front/Bman_F_f0"));
		anim1.ac.animation.setPlayMode(PlayMode.LOOP);
		anim2 = new SimpleAnimationEntity(new PositionComponent(250, 150), new AnimationComponent(6, "Sprites/Creep/Front/Creep_F_f0"));
		anim2.ac.animation.setPlayMode(PlayMode.LOOP);
		anim = new AnimationSystem();
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		anim.update(delta, anim1);
		anim.update(delta, anim2);
		
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		anim.render(anim1, batch);
		anim.render(anim2, batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		anim1.ac.dispose();
		anim2.ac.dispose();
	}

}
