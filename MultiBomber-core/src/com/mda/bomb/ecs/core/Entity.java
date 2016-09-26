package com.mda.bomb.ecs.core;

import java.util.HashMap;

public class Entity {
	public static EntitySystem entitySystem;
	private int ID;
	private HashMap<Class<?>, Component> components;
	
	public Entity() {
		if(entitySystem == null) throw new Error("No EntitySystem detected. Please create a EntitySystem before creating an Entity");
		entitySystem.registerEntity(this);
		components = new HashMap<Class<?>, Component>();
	}
	
	public Entity(int ID) {
		if(entitySystem == null) throw new Error("No EntitySystem detected. Please create a EntitySystem before creating an Entity");
		this.ID = ID;
		entitySystem.registerEntityWithID(this);
		components = new HashMap<Class<?>, Component>();
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
	
	public Entity addComponent(Component c) {
		components.put(c.getClass(), c);
		return this;
	}
	
	public void removeComponent(Class<?> type) {
		components.remove(type);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getAs(Class<T> type) {
		return (T) components.get(type);
	}
	
	public HashMap<Class<?>, Component> getComponents() {
		return components;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Entity)) return false;
		
		return ((Entity)o).ID == ID;
	}
}
