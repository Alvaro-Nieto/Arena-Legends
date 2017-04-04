package es.alvaronieto.pfcdam.net;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import es.alvaronieto.pfcdam.Sprites.Player;
import es.alvaronieto.pfcdam.Sprites.PlayerState;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;

public class ClientNetworkListener extends Listener{
	private Client client;
	private PlayerState playerState;
	private World world;
	
	public void init(Client client, PlayerState playerState, World world){
		this.world = world;
		this.client = client;
		this.playerState = playerState;
	}
	
	@Override
	public void connected(Connection connection) {
		System.out.println("[CLIENT] >> You have connected.");
		
		
		Packet01Message firstMessage = new Packet01Message();
		firstMessage.message = "Hello Server, How are you!";
		client.sendTCP(firstMessage);
		client.sendTCP(playerState);
	}

	@Override
	public void disconnected(Connection connection) {
		System.out.println("[CLIENT] >> You have disconnected.");
	}

	@Override
	public void received(Connection connection, Object obj) {
		if( obj instanceof Packet01Message ){
			Packet01Message p = (Packet01Message)obj;
			
			System.out.println("[SERVER] >> " + p.message);
		}
		
		if( obj instanceof PlayerState ){
			PlayerState playerState = (PlayerState)obj;
			
			new Player(world, playerState.getPosition().x, playerState.getPosition().y);
			
			System.out.println("Añadido player: " + playerState.toString());
		}
	}
	
	
}
