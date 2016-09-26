package com.mda.bomb.ecs.components;

import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.ecs.core.Component;

public class SizeComponent extends Component {
	public Vector2 size;
	
	public SizeComponent(Vector2 size) {
		this.size = size;
	}
}
