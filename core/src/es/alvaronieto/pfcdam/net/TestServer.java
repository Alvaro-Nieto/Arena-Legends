package es.alvaronieto.pfcdam.net;

import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import es.alvaronieto.pfcdam.Sprites.PlayerState;



public class TestServer {
	// Connection info
	int serverPort = 25565;
	
	
	// Kyonet "Server" object
	private Server server;
	ServerNetworkListener snl;
	
	//
	private List<Connection> clients;
	
	private World world;
	
	public TestServer() {
		clients = new ArrayList<Connection>();
		server = new Server();
		snl = new ServerNetworkListener(clients);
		
		server.addListener(snl);
		
		try {
			server.bind(serverPort);
		} catch(BindException be){
			System.err.println("en uso");
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		registerPackets();
		
		server.start();
	}
	
	private void registerPackets(){
		Kryo kryo = server.getKryo();
		
		kryo.register(Packets.Packet01Message.class);
		kryo.register(PlayerState.class);
		kryo.register(Vector2.class);
	}
}
