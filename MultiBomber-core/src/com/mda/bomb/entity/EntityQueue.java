package com.mda.bomb.entity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class EntityQueue {
	private static ConcurrentLinkedQueue<Integer> toRemoveEntities = new ConcurrentLinkedQueue<Integer>();
	
	public static void addToQueueToRemove(Integer id) {
		toRemoveEntities.add(id);
	}
	
	public static Integer pollEntityToRemove() {
		return toRemoveEntities.poll();
	}
}
