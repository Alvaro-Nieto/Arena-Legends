package es.alvaronieto.pfcdam.net.kryoserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.Packets;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionResponse;
import es.alvaronieto.pfcdam.net.Packets.Packet04ClientConnected;

public class ServerKryoListener extends Listener {

	private List<Connection> clients;
	private Random rnd;
	private SimpleDateFormat dateFormat;
	private final int MAX_CLIENTS;
	
	public ServerKryoListener(List<Connection> clients, int maxClients){
		this.MAX_CLIENTS = maxClients;
		this.clients = clients;
		this.rnd = new Random();
		this.dateFormat = new SimpleDateFormat("HH:mm:ss");
		
	}
	
	

	@Override
	public void connected(Connection connection) {
		//System.out.println("[S]Someone has connected");
		//clients.add(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		//System.out.println("[S]Someone has disconnected");
		
	}


	@Override
	public void received(Connection connection, Object obj) {
		if( obj instanceof Packet01Message ){
			Packet01Message p = (Packet01Message)obj;
			System.out.println(dateFormat.format(new Date((p.timeStamp)))+" : [S](CLIENT) >> " + p.message);
		}
		
		if( obj instanceof Packet02ConnectionRequest ){
			Packet02ConnectionRequest r = (Packet02ConnectionRequest)obj;
			Packet03ConnectionResponse response = new Packet03ConnectionResponse();
			response.accepted = clients.size() < MAX_CLIENTS;
			response.userID = response.accepted ? rnd.nextLong() : 0;
			response.timeStamp = new Date().getTime();
			connection.sendTCP(response);
			if(!response.accepted){
				System.out.println("[S] Conexión rechazada a un cliente");
				connection.close();
			}
			else{
				System.out.println("[S] Conectado cliente con ID: "+ response.userID);
				clients.add(connection);
				Packet04ClientConnected clientConnected = new Packet04ClientConnected();
				clientConnected.userID = response.userID;
				clientConnected.timeStamp = new Date().getTime();
				TCPBroadcastExcept(clientConnected, connection);
			}
		}
		
		if( obj instanceof PlayerState ){
			PlayerState playerState = (PlayerState)obj;
			for(Connection client : clients){
				if(!client.equals(connection)){
					client.sendTCP(playerState);
				}
			}
			System.out.println("[S]Recibido player: " + playerState.toString());
		}
		
		
	}
	
	private void TCPBroadcastExcept(Object object, Connection exceptionConnection){
		for(Connection client : clients){
			if(!client.equals(exceptionConnection)){
				client.sendTCP(object);
			}
		}
	}
	
	private void UDPBroadcastExcept(Object object, Connection exceptionConnection){
		for(Connection client : clients){
			if(!client.equals(exceptionConnection)){
				client.sendUDP(object);
			}
		}
	}
	
	
	
}
