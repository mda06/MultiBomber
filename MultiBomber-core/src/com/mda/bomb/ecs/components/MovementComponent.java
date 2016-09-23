package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class MovementComponent extends Component {
	public float velX, velY;
	
	public MovementComponent() {
		velX = velY = 0;
	}
}
