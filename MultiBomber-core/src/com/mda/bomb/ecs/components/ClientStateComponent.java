package com.mda.bomb.ecs.components;

import com.mda.bomb.ecs.core.Component;

public class ClientStateComponent extends Component {
	public enum ClientState {
		ROOM, GAME
	}
	
	public ClientState clientState;
	
	public ClientStateComponent(ClientState cs) {
		clientState = cs;
	}
}
