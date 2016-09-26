package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class DropBombComponent extends Component{
	public int nbOfBombs;
	
	public DropBombComponent(int nb) {
		nbOfBombs = nb;
	}
}
