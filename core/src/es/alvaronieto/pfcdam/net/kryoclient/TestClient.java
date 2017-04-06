package es.alvaronieto.pfcdam.net.kryoclient;

import java.io.IOException;
import java.util.Date;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionAccepted;
import es.alvaronieto.pfcdam.net.Packets.Packet04ConnectionRejected;
import es.alvaronieto.pfcdam.net.Packets.Packet05ClientConnected;
import es.alvaronieto.pfcdam.net.Util;

public class TestClient extends Listener{
	// Connection stuff
	int portSocket = 25565;
	String ipAddress = "localhost";
	
	// Kryonet stuff
	private Client client;
	//private ClientKryoListener cnl;
	
	private ClientListener clientListener;
	
	public TestClient(ClientListener clientListener){

		this.clientListener = clientListener;
		client = new Client();
		//cnl = new ClientKryoListener();
		
		//cnl.init(client, clientListener);
		client.addListener(this);
		registerPackets();
		
		client.start();
		
		try {
			client.connect(5000, ipAddress, portSocket, portSocket);
		} catch (IOException e) {
			e.printStackTrace();
			clientListener.couldNotConnect();
		}
		
		// TODO Remove
		// Prueba 
		/*
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(!Thread.interrupted()){
					client.sendUDP(playerState);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();*/
	}
	
	@Override
	public void connected(Connection connection) {
		System.out.println("[C] >> You have connected.");
		
		Packet02ConnectionRequest request = new Packet02ConnectionRequest();
		request.clientName = "NOT DEFINED";
		request.timeStamp = new Date().getTime();
		connection.sendTCP(request);
		
	}
	
	private void registerPackets() {
		Util.registerPackets(client.getKryo());
	}
	
	@Override
	public void disconnected(Connection connection) {
		System.out.println("[C] >> You have disconnected.");
	}

	@Override
	public void received(Connection connection, Object obj) {
		if( obj instanceof Packet01Message ){
			Packet01Message p = (Packet01Message)obj;
			
			System.out.println("[C](SERVER) >> " + p.message);
		}
		
		else if( obj instanceof Packet03ConnectionAccepted ){
			Packet03ConnectionAccepted accepted = (Packet03ConnectionAccepted)obj;
			System.out.println("[C] >> " + "Conexión aceptada");
			clientListener.connectionAccepted(accepted.playerState, accepted.gameState);
		}
		
		else if( obj instanceof Packet04ConnectionRejected ){
			Packet04ConnectionRejected rejected = (Packet04ConnectionRejected)obj;
			System.err.println("[C] >> " + "Conexión rechazada");
			System.exit(1);
		}
		
		
		else if( obj instanceof Packet05ClientConnected ){
			Packet05ClientConnected connected = (Packet05ClientConnected)obj;
			clientListener.newPlayerConnected(connected.playerState);
			System.out.println("[C]Cliente conectado con ID: " + connected.userID);
		}
		
	}

}
