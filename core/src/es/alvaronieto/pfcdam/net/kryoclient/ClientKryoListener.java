package es.alvaronieto.pfcdam.net.kryoclient;

import java.util.Date;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import es.alvaronieto.pfcdam.Sprites.Player;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.Packets;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionAccepted;
import es.alvaronieto.pfcdam.net.Packets.Packet04ConnectionRejected;
import es.alvaronieto.pfcdam.net.Packets.Packet05ClientConnected;

public class ClientKryoListener extends Listener{
	private Client client;
	private ClientListener clientListener;
	
	public void init(Client client, ClientListener clientListener){
		this.clientListener = clientListener;
		this.client = client;

	}
	
	@Override
	public void connected(Connection connection) {
		System.out.println("[C] >> You have connected.");
		
		Packet02ConnectionRequest request = new Packet02ConnectionRequest();
		request.clientName = "NOT DEFINED";
		request.timeStamp = new Date().getTime();
		connection.sendTCP(request);
		
		/*
		Packet01Message firstMessage = new Packet01Message();
		firstMessage.message = "Hello Server, How are you!";
		firstMessage.timeStamp = new Date().getTime();
		client.sendTCP(firstMessage);
		client.sendTCP(playerState);*/
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
