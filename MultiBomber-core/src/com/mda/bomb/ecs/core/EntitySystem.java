package com.mda.bomb.ecs.core;

import java.util.HashMap;

public class EntitySystem extends BaseSystem {
	private static int currentAvailableID;
	private HashMap<Integer, Entity> entities;

	public EntitySystem() {
		currentAvailableID = 0;
		entities = new HashMap<Integer, Entity>();
		Entity.entitySystem = this;
	}
	
	//Need to synchronize this method because we multithreading
	private int getNextAvailableID() {
		synchronized (EntitySystem.class) {
			do { 
				currentAvailableID++;
			} while(entities.containsKey(currentAvailableID));
			return currentAvailableID;
		}
	}

	protected void registerEntity(Entity e) {
		if (e == null)
			throw new Error("Entity is null. Cannot register entity.");

		e.setID(getNextAvailableID());
		entities.put(new Integer(e.getID()), e);
	}
	
	protected void registerEntityWithID(Entity e) {
		if (e == null)
			throw new Error("Entity is null. Cannot register entity.");

		entities.put(e.getID(), e);
	}

	public Entity getEntity(int id) {
		if (id == -1)
			return null;

		return entities.get(id);
	}

	public void removeEntity(int id) {
		entities.remove(id);
	}
	
	public void removeEntity(Entity e) {
		if (e == null)
			return;

		entities.remove(e.getID());
	}
	
	public HashMap<Integer, Entity> getEntities() {
		return entities;
	}

	@Override
	public void update(float dt, Entity e) {}
}
