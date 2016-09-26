package com.mda.bomb.screen.event;

import com.mda.bomb.ecs.core.Entity;

public interface GameListener {
	public void startGame();
	public void directionChanged(Entity e);
	public void dropBomb(Entity e);
}
