package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class DirectionComponent extends Component {
	public enum Direction {
		LEFT, RIGHT, DOWN, UP, STOP
	}
	
	public Direction direction = Direction.STOP;
}
