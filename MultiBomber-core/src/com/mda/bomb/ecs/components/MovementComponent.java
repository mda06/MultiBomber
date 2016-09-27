package com.mda.bomb.ecs.components;

import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.ecs.core.Component;

public class MovementComponent extends Component {
	public Vector2 vel, accel, maxVel, deccel;
	
	
	public MovementComponent() {
		vel = new Vector2();
		accel = new Vector2(850, 850);
		maxVel = new Vector2(200, 200);
		deccel = new Vector2(450, 450);
	}
}
