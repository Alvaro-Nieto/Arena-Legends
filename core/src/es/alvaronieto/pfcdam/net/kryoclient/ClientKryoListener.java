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
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionResponse;
import es.alvaronieto.pfcdam.net.Packets.Packet04ClientConnected;

public class ClientKryoListener extends Listener{
	private Client client;
	private PlayerState playerState;
	private ClientListener clientListener;
	
	public void init(Client client, PlayerState playerState, ClientListener clientListener){
		this.clientListener = clientListener;
		this.client = client;
		this.playerState = playerState;
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
		
		if( obj instanceof Packet03ConnectionResponse ){
			Packet03ConnectionResponse r = (Packet03ConnectionResponse)obj;
			
			if(r.accepted){
				System.out.println("[C] >> " + "Conexión aceptada");
			} else {
				System.err.println("[C] >> " + "Conexión rechazada");
				System.exit(1);
			}
		}
		
		
		if( obj instanceof Packet04ClientConnected ){
			Packet04ClientConnected c = (Packet04ClientConnected)obj;
			
			System.out.println("[C]Cliente conectado con ID: " + c.userID);
		}
		
		if( obj instanceof PlayerState ){
			PlayerState playerState = (PlayerState)obj;
			clientListener.PlayerStateReceived(playerState);
			
			//new Player(world, playerState.getPosition().x, playerState.getPosition().y);
			//System.out.println("[C]Añadido player: " + playerState.toString());
		}
	}
	
	
}
