package com.mda.bomb.network.sync;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Connection;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.ecs.core.EntitySystem;
import com.mda.bomb.network.MyClient;
import com.mda.bomb.network.MyServer;

public class EntitySync extends BaseSync{
	public int entityID;
	public float posX, posY;

	@Override
	public void handleServer(MyServer server, Connection connection) {
		
	}
	
	@Override
	public void handleClient(MyClient client, Connection connection) {
		Entity e = client.getEngine().getSystem(EntitySystem.class).getEntity(entityID);
		if(e == null) return;
		PositionComponent pc = e.getAs(PositionComponent.class);
		if(pc == null) return;
		pc.x = MathUtils.lerp(pc.x, posX, .4f);
		pc.y = MathUtils.lerp(pc.y, posY, .4f);
	}
}
