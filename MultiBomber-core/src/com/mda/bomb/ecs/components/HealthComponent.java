package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class HealthComponent extends Component {
	public int health;
	
	public HealthComponent(int h) {
		health = h;
	}
}
