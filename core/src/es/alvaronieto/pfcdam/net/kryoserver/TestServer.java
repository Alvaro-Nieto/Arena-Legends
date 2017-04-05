package es.alvaronieto.pfcdam.net.kryoserver;

import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.Util;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionResponse;



public class TestServer {
	// Connection info
	int serverPort = 25565;
	
	
	// Kyonet "Server" object
	private Server server;
	ServerKryoListener snl;
	
	//
	private List<Connection> clients;
	
	private World world;
	
	public TestServer(int maxClients) {
		clients = new ArrayList<Connection>();
		server = new Server();
		snl = new ServerKryoListener(clients, maxClients);
		
		server.addListener(snl);

		try {
			server.bind(serverPort, serverPort);
		} catch(BindException be){
			System.err.println("en uso");
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		registerPackets();
		
		server.start();
	}
	
	private void registerPackets(){
		Util.registerPackets(server.getKryo());
	}
}
