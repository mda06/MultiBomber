package com.mda.bomb.ecs.systems;

import com.mda.bomb.ecs.components.SpriteComponent;
import com.mda.bomb.ecs.components.SpriteFeature;
import com.mda.bomb.ecs.core.BaseSystem;
import com.mda.bomb.ecs.core.Entity;

public class SpriteFeatureSystem extends BaseSystem {
	private SpriteComponent sc;
	private SpriteFeature sf;

	@Override
	public void update(float dt, Entity e) {
		sf = e.getAs(SpriteFeature.class);
		sc = e.getAs(SpriteComponent.class);
		if (sc != null && sf != null) {
			float scale = .2f;
			if (sf.scale) {
				sc.scaleX += scale * dt;
				sc.scaleY += scale * dt;
			}
			if(sf.rotate)
				;//sc.rotation += 90 * dt;
		}
	}

}
