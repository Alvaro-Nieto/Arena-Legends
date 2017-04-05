package es.alvaronieto.pfcdam.net;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionResponse;
import es.alvaronieto.pfcdam.net.Packets.Packet04ClientConnected;
import es.alvaronieto.pfcdam.net.Packets.Packet05ClientDisconnected;

public class Util {
	public static void registerPackets(Kryo kryo){
		kryo.register(Packet01Message.class);
		kryo.register(Packet02ConnectionRequest.class);
		kryo.register(Packet03ConnectionResponse.class);
		kryo.register(Packet04ClientConnected.class);
		kryo.register(Packet05ClientDisconnected.class);
		kryo.register(PlayerState.class);
		kryo.register(Vector2.class);
	}
}
