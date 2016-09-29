package com.mda.bomb.entity;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.components.PowerupComponent;

public class PowerupQueue {

	public static class Powerup {
		public int ID;
		public PowerupComponent type;
		public PositionComponent pc;
		
		public Powerup(PositionComponent pc, PowerupComponent type, int ID) {
			this.pc = pc;
			this.type = type;
			this.ID = ID;
		}
	}
	
	private static ConcurrentLinkedQueue<Powerup> puList = new ConcurrentLinkedQueue<Powerup>();
	
	public static void addToQueue(Powerup b) {
		puList.add(b);
	}
	
	public static Powerup poll() {
		return puList.poll();
	}
}
