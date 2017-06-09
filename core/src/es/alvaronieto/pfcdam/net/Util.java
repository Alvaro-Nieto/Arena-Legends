package es.alvaronieto.pfcdam.net;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerSlot;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.gameobjects.GameRules;

public class Util {
	public static void registerPackets(Kryo kryo){
		for(Class clazz : Packets.class.getClasses()) 
			kryo.register(clazz);

		kryo.register(PlayerState.class);
		kryo.register(GameState.class);
		kryo.register(InputState.class);
		kryo.register(Vector2.class);
		kryo.register(HashMap.class);
		kryo.register(GameRules.class);
		kryo.register(PlayerSlot.class);
		kryo.register(LobbyState.class);
	}
	
	
}
