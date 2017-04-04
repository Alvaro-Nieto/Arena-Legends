package es.alvaronieto.pfcdam.net;

import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import es.alvaronieto.pfcdam.Sprites.PlayerState;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;

public class ServerNetworkListener extends Listener {

	private List<Connection> clients;
	
	public ServerNetworkListener(List<Connection> clients){
		this.clients = clients;
	}
	
	

	@Override
	public void connected(Connection connection) {
		System.out.println("Someone has connected");
		clients.add(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		System.out.println("Someone has disconnected");
	}


	@Override
	public void received(Connection connection, Object obj) {
		if( obj instanceof Packet01Message ){
			Packet01Message p = (Packet01Message)obj;
			
			System.out.println("[CLIENT] >> " + p.message);
		}
		
		if( obj instanceof PlayerState ){
			PlayerState playerState = (PlayerState)obj;
			for(Connection client : clients){
				if(!client.equals(connection)){
					client.sendTCP(playerState);
				}
			}
			System.out.println("Recibido player: " + playerState.toString());
		}
	}
	
	
	
}
