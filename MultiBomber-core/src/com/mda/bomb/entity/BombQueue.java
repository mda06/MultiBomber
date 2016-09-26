package com.mda.bomb.entity;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.mda.bomb.ecs.components.PositionComponent;

public class BombQueue {
	
	public static class Bomb {
		public PositionComponent pc;
		public int ID;
		
		public Bomb(PositionComponent pc, int ID) {
			this.pc = pc;
			this.ID = ID;
		}
	}
	
	private static ConcurrentLinkedQueue<Bomb> bombList = new ConcurrentLinkedQueue<Bomb>();
	
	public static void addToQueue(Bomb b) {
		bombList.add(b);
	}
	
	public static Bomb poll() {
		return bombList.poll();
	}
}
