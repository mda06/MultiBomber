package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class PositionComponent extends Component {
	public float x, y;
	
	public PositionComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}
}