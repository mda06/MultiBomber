package com.mda.bomb.ecs.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Engine {
	private HashMap<Class<?>, BaseSystem> systems;
	private boolean isGameStarted;

	public Engine() {
		systems = new HashMap<Class<?>, BaseSystem>();
		isGameStarted = false;
	}
	
	public void addSystem(BaseSystem bs) {
		systems.put(bs.getClass(), bs);
	}

	public void removeSystem(Class<?> type) {
		systems.remove(type);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseSystem> T getSystem(Class<T> classType) {
		return (T) systems.get(classType);
	}

	public void update(float dt) {
		//Don't update the engine of the game is not started
		if(!isGameStarted) return;
		
		EntitySystem es = getSystem(EntitySystem.class);
		if (es == null)
			throw new Error("No EntitySystem was found in the Engine. Add a EntitySystem to the Engine.");

		for (Entity entity : es.getEntities().values()) {
			Iterator<Entry<Class<?>, BaseSystem>> it = systems.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Class<?>, BaseSystem> pair = it.next();
				pair.getValue().update(dt, entity);
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		//Don't render the game if it's not started
		if(!isGameStarted) return;
		
		for (Entity entity : getSystem(EntitySystem.class).getEntities().values()) {
			Iterator<Entry<Class<?>, BaseSystem>> it = systems.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Class<?>, BaseSystem> pair = it.next();
				if(pair.getValue() instanceof RenderSystem) {
					RenderSystem render = (RenderSystem) pair.getValue();
					render.render(entity, batch);
				}
			}
		}
	}
	
	public void setGameStarted(boolean s) {
		isGameStarted = s;
	}
	
	public boolean isGameStarted() {
		return isGameStarted;
	}

}
