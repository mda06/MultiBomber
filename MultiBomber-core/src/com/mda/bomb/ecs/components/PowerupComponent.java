package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class PowerupComponent extends Component{
	public enum PowerupType {
		BOMB
	}
	
	public PowerupType type;
	public boolean isUsed;
	
	public PowerupComponent() {
		this(PowerupType.BOMB);
	}
	
	public PowerupComponent(PowerupType type) {
		this.type = type;
		isUsed = false;
	}
}
