package es.alvaronieto.pfcdam.net;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;


import es.alvaronieto.pfcdam.Sprites.PlayerState;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;

public class TestClient {
	// Connection stuff
	int portSocket = 25565;
	String ipAddress = "localhost";
	
	// Kryonet stuff
	private Client client;
	private ClientNetworkListener cnl;
	
	private World world;
	
	public TestClient(PlayerState playerState, World world){
		this.world = world;
		client = new Client();
		cnl = new ClientNetworkListener();
		
		cnl.init(client, playerState, world);
		client.addListener(cnl);
		registerPackets();
		
		client.start();
		
		try {
			client.connect(5000, ipAddress, portSocket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void registerPackets() {
		Kryo kryo = client.getKryo();
		
		kryo.register(Packet01Message.class);
		kryo.register(PlayerState.class);
		kryo.register(Vector2.class);
	}
}
