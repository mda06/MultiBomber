package com.mda.bomb.screen.event;

import com.mda.bomb.ecs.core.Entity;

public interface ChangeDirectionListener {
	public void directionChanged(Entity e);
}
