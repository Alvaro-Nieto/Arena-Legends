package es.alvaronieto.pfcdam.net.kryoclient;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.Util;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionResponse;

public class TestClient {
	// Connection stuff
	int portSocket = 25565;
	String ipAddress = "localhost";
	
	// Kryonet stuff
	private Client client;
	private ClientKryoListener cnl;
	
	private ClientListener clientListener;
	private PlayerState playerState;
	
	public TestClient(final PlayerState playerState,ClientListener clientListener){
		this.playerState = playerState;
		this.clientListener = clientListener;
		client = new Client();
		cnl = new ClientKryoListener();
		
		cnl.init(client, playerState, clientListener);
		client.addListener(cnl);
		registerPackets();
		
		client.start();
		
		try {
			client.connect(5000, ipAddress, portSocket, portSocket);
		} catch (IOException e) {
			e.printStackTrace();
			clientListener.couldNotConnect();
		}
		
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
		}).start();
	}
	
	private void registerPackets() {
		Util.registerPackets(client.getKryo());
	}

	public PlayerState getPlayer() {
		if(playerState == null){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getPlayer();
		}
		return playerState;
	}
}
