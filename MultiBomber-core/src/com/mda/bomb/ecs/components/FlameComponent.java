package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class FlameComponent extends Component {
	public float renderTime;
	public boolean isRenderTimeFinish;
	
	public FlameComponent() {
		renderTime = 1f;
		isRenderTimeFinish = false;
	}
}
