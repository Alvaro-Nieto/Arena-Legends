package es.alvaronieto.pfcdam.net.kryoclient;

import java.io.IOException;
import java.util.Date;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionAccepted;
import es.alvaronieto.pfcdam.net.Packets.Packet04ConnectionRejected;
import es.alvaronieto.pfcdam.net.Packets.Packet05ClientConnected;
import es.alvaronieto.pfcdam.net.Packets.Packet06ClientDisconnected;
import es.alvaronieto.pfcdam.net.Packets.Packet07PlayerUpdate;
import es.alvaronieto.pfcdam.net.Packets.Packet08GameUpdate;
import es.alvaronieto.pfcdam.net.Packets.Packet09UserInput;
import es.alvaronieto.pfcdam.net.Util;

public class TestClient extends Listener{
	// Connection stuff
	int portSocket = 25565;
	String ipAddress = "localhost";
	
	// Kryonet stuff
	private Client client;
	
	// Game stuff
	private ClientListener clientListener;
	
	public TestClient(ClientListener clientListener){

		this.clientListener = clientListener;
		client = new Client();
		client.addListener(this);
		registerPackets();
		
		client.start();
		
		try {
			client.connect(5000, ipAddress, portSocket, portSocket);
		} catch (IOException e) {
			e.printStackTrace();
			clientListener.couldNotConnect();
		}
		
	}
	
	@Override
	public void connected(Connection connection) {
		System.out.println("[C] >> You have connected.");
		
		Packet02ConnectionRequest request = new Packet02ConnectionRequest();
		request.clientName = "NOT DEFINED"; // TODO
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
		
		else if( obj instanceof Packet08GameUpdate ){
			Packet08GameUpdate gameUpdate = (Packet08GameUpdate)obj;
			clientListener.snapShotReceived(gameUpdate.timeStamp, gameUpdate.gameState, gameUpdate.userLastInputProccessed);
		}
		
	}

	public void sendInputState(InputState inputState, long userID) {
		Packet09UserInput inputPacket = new Packet09UserInput();
		inputPacket.userID = userID;
		inputPacket.timeStamp = new Date().getTime();
		inputPacket.inputState = inputState;
		//System.out.println(inputState.getSequenceNumber());
		client.sendUDP(inputPacket);
	}

}
