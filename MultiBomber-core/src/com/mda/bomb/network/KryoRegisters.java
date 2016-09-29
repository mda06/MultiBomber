package com.mda.bomb.network;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.network.sync.BombExplodeSync;
import com.mda.bomb.network.sync.DeadSync;
import com.mda.bomb.network.sync.DirectionSync;
import com.mda.bomb.network.sync.DropBombSync;
import com.mda.bomb.network.sync.EnterRoomSync;
import com.mda.bomb.network.sync.EntitySync;
import com.mda.bomb.network.sync.HealthSync;
import com.mda.bomb.network.sync.InitMapSync;
import com.mda.bomb.network.sync.ReadyGameSync;
import com.mda.bomb.network.sync.ReadyRoomDisconnectSync;
import com.mda.bomb.network.sync.ReadyRoomListenerSync;

public class KryoRegisters {
	public static void registerKryo(Kryo kryo) {
		kryo.register(EntitySync.class);
		kryo.register(EnterRoomSync.class);
		kryo.register(ReadyRoomListenerSync.class);
		kryo.register(ReadyRoomDisconnectSync.class);
		kryo.register(ReadyGameSync.class);
		kryo.register(DirectionComponent.class);
		kryo.register(DirectionComponent.Direction.class);
		kryo.register(DirectionSync.class);
		kryo.register(InitMapSync.class);
		kryo.register(BombExplodeSync.class);
		kryo.register(DropBombSync.class);
		kryo.register(Vector2.class);
		kryo.register(BombExplodeSync.class);
		kryo.register(HealthSync.class);
		kryo.register(DeadSync.class);
	}
}
