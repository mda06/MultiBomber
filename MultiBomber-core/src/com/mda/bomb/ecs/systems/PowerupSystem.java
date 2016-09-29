package com.mda.bomb.ecs.systems;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.ecs.components.CollisionComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.PowerupComponent;
import com.mda.bomb.ecs.components.PowerupComponent.PowerupType;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.ExternSystem;
import com.mda.bomb.map.Map;
import com.mda.bomb.screen.event.PowerupListener;

public class PowerupSystem extends ExternSystem {
	
	private Map map;
	private PowerupListener listener;
	private float spawnCooldown, currentCooldown;
	
	public PowerupSystem(Map m, PowerupListener list) {
		if(m == null)
			throw new IllegalArgumentException("Map passed in constructor of PowerupSystem is null ! Please enter a valid map");
		map = m;
		listener = list;
		spawnCooldown = 15;
		currentCooldown = 0;
	}
	
	@Override
	public void update(float dt) {
		currentCooldown -= dt;
		if(currentCooldown < 0) {
			currentCooldown = spawnCooldown;
			generatePowerup();
		}
	}
	
	private void generatePowerup() {
		Entity e = new Entity();
		e.addComponent(new CollisionComponent(new Vector2(64, 64)));
		List<Vector2> lst = map.getBackgroundTiles();
		Vector2 randPos = lst.get((int)(Math.random() * lst.size()));
		int ts = map.getTileSize();
		float x = randPos.x * ts + ts / 2 , y = randPos.y * ts + ts / 2;
		e.addComponent(new PositionComponent(x, y));
		e.addComponent(new PowerupComponent(PowerupType.BOMB));
		listener.PowerupGenerated(e);
	}
	
	public void handlePowerup() {
		
	}

}
