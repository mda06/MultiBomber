package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class ReadyRoomComponent extends Component{
	public boolean isReady;
	
	public ReadyRoomComponent(boolean ready) {
		isReady = ready;
	}
}
