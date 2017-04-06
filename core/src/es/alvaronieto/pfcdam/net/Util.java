package es.alvaronieto.pfcdam.net;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionAccepted;
import es.alvaronieto.pfcdam.net.Packets.Packet04ConnectionRejected;
import es.alvaronieto.pfcdam.net.Packets.Packet05ClientConnected;
import es.alvaronieto.pfcdam.net.Packets.Packet06ClientDisconnected;

public class Util {
	public static void registerPackets(Kryo kryo){
		kryo.register(Packet01Message.class);
		kryo.register(Packet02ConnectionRequest.class);
		kryo.register(Packet03ConnectionAccepted.class);
		kryo.register(Packet04ConnectionRejected.class);
		kryo.register(Packet05ClientConnected.class);
		kryo.register(Packet06ClientDisconnected.class);
		kryo.register(PlayerState.class);
		kryo.register(GameState.class);
		kryo.register(Vector2.class);
		kryo.register(HashMap.class);
	}
}
