package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class NameComponent extends Component{
	public String name;
	
	public NameComponent(String name) {
		this.name = name;
	}
}
