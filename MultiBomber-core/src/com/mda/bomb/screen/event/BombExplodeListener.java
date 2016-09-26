package com.mda.bomb.screen.event;

import com.mda.bomb.ecs.core.Entity;

public interface BombExplodeListener {
	public void explode(Entity e);
}
