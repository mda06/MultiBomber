package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class BombAIComponent extends Component {
	public float explodeTime;
	public int explodeSize;
	public boolean isExploded;
	
	public BombAIComponent() {
		explodeTime = 1.5f;
		explodeSize = 3;
		isExploded = false;
	}
}
