package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class SpriteFeature extends Component {
	public boolean scale, rotate;

	public SpriteFeature() {
		scale = true;
		rotate = true;
	}
}
