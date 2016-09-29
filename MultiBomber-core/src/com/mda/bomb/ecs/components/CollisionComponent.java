package com.mda.bomb.ecs.components;

import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.ecs.core.Component;

public class CollisionComponent extends Component {
	public Vector2 size;

	public CollisionComponent(Vector2 size) {
		this.size = size;
	}
}
