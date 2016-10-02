package com.mda.bomb.network;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.mda.bomb.ecs.components.DirectionComponent;
import com.mda.bomb.ecs.components.PowerupComponent;
import com.mda.bomb.network.sync.DisconnectSync;
import com.mda.bomb.network.sync.game.BombExplodeSync;
import com.mda.bomb.network.sync.game.CollisionCompSync;
import com.mda.bomb.network.sync.game.DeadSync;
import com.mda.bomb.network.sync.game.DirectionSync;
import com.mda.bomb.network.sync.game.DropBombSync;
import com.mda.bomb.network.sync.game.EntitySync;
import com.mda.bomb.network.sync.game.HealthSync;
import com.mda.bomb.network.sync.game.InitMapSync;
import com.mda.bomb.network.sync.game.PowerupSpawnSync;
import com.mda.bomb.network.sync.game.ReadyGameSync;
import com.mda.bomb.network.sync.room.EnterRoomSync;
import com.mda.bomb.network.sync.room.ReadyRoomListenerSync;

public class KryoRegisters {
	public static void registerKryo(Kryo kryo) {
		kryo.register(EntitySync.class);
		kryo.register(EnterRoomSync.class);
		kryo.register(ReadyRoomListenerSync.class);
		kryo.register(DisconnectSync.class);
		kryo.register(ReadyGameSync.class);
		kryo.register(DirectionComponent.class);
		kryo.register(DirectionComponent.VerticalDirection.class);
		kryo.register(DirectionComponent.HorizontalDirection.class);
		kryo.register(DirectionSync.class);
		kryo.register(InitMapSync.class);
		kryo.register(BombExplodeSync.class);
		kryo.register(DropBombSync.class);
		kryo.register(Vector2.class);
		kryo.register(BombExplodeSync.class);
		kryo.register(HealthSync.class);
		kryo.register(CollisionCompSync.class);
		kryo.register(DeadSync.class);
		kryo.register(PowerupComponent.class);
		kryo.register(PowerupComponent.PowerupType.class);
		kryo.register(PowerupSpawnSync.class);
	}
}
