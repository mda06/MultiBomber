package com.mda.bomb.ecs.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class RenderSystem extends BaseSystem {
	public abstract void render(Entity e, SpriteBatch batch);
}
