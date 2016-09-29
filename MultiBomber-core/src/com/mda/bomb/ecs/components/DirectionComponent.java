package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class DirectionComponent extends Component {
	
	public enum HorizontalDirection {
		LEFT, RIGHT, STOP
	}
	
	public enum VerticalDirection {
		UP, DOWN, STOP
	}

	public HorizontalDirection horizontalDirection = HorizontalDirection.STOP;
	public VerticalDirection verticalDirection = VerticalDirection.STOP;
}
