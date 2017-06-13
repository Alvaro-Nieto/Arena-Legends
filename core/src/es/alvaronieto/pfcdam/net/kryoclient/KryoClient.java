package es.alvaronieto.pfcdam.net.kryoclient;

import static es.alvaronieto.pfcdam.Util.Constants.SERVER_PORT;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerSlot;
import es.alvaronieto.pfcdam.gameobjects.GameRules;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionAccepted;
import es.alvaronieto.pfcdam.net.Packets.Packet04ConnectionRejected;
import es.alvaronieto.pfcdam.net.Packets.Packet05ClientConnected;
import es.alvaronieto.pfcdam.net.Packets.Packet08GameUpdate;
import es.alvaronieto.pfcdam.net.Packets.Packet09UserInput;
import es.alvaronieto.pfcdam.net.Packets.Packet10RequestInfo;
import es.alvaronieto.pfcdam.net.Packets.Packet11InfoAnswer;
import es.alvaronieto.pfcdam.net.Packets.Packet12GameStarted;
import es.alvaronieto.pfcdam.net.Packets.Packet13StartRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet14GameRulesChangeRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet15SlotUpdate;
import es.alvaronieto.pfcdam.net.Packets.Packet16LobbyUpdate;
import es.alvaronieto.pfcdam.net.Packets.Packet17Attack1Request;
import es.alvaronieto.pfcdam.net.Packets.Packet18ProjectileDestroyed;
import es.alvaronieto.pfcdam.net.Util;

public class KryoClient extends Listener {
	
	// Kryonet stuff
	private Client client;
	
	// Game stuff
	private ClientListener clientListener;
	
	public KryoClient(ClientListener clientListener){
		this.clientListener = clientListener;
		client = new Client();
		client.addListener(this);
		registerPackets();
		client.start();
	}

	public void connect(String ipAddress, long adminToken, String clientName) {
		try {
			System.out.println("Client: adminToken->"+adminToken);
			client.connect(5000, ipAddress, SERVER_PORT, SERVER_PORT);
			Packet02ConnectionRequest request = new Packet02ConnectionRequest();
			request.clientName = clientName;
			request.timeStamp = new Date().getTime();
			request.adminToken = adminToken;
			client.sendTCP(request);
		} catch (IOException e) {
			e.printStackTrace();
			clientListener.couldNotConnect();
		}
	}
	
	@Override
	public void connected(Connection connection) {
		//		
	}
	
	private void registerPackets() {
		Util.registerPackets(client.getKryo());
	}
	
	@Override
	public void disconnected(Connection connection) {
		//
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
			clientListener.connectionAccepted(accepted.userID, accepted.lobbyState, accepted.admin, connection.getRemoteAddressTCP().getHostString());
			System.out.println("Client: aceptado "+ (accepted.admin ? "con admin" : "sin admin"));
		}
		
		else if( obj instanceof Packet04ConnectionRejected ){
			Packet04ConnectionRejected rejected = (Packet04ConnectionRejected)obj;
			clientListener.connectionRejected(connection.getRemoteAddressTCP().getHostString(), rejected.reason);
		}
	
		else if( obj instanceof Packet05ClientConnected ){
			Packet05ClientConnected connected = (Packet05ClientConnected)obj;
			clientListener.newPlayerConnected(connected.userID);
			System.out.println("[C]Cliente conectado con ID: " + connected.userID);
		}
		
		else if( obj instanceof Packet08GameUpdate ){
			Packet08GameUpdate gameUpdate = (Packet08GameUpdate)obj;
			clientListener.snapShotReceived(gameUpdate.timeStamp, gameUpdate.gameState, gameUpdate.userLastInputProccessed);
		}
		
		else if( obj instanceof Packet11InfoAnswer){
			Packet11InfoAnswer info = (Packet11InfoAnswer)obj;
			String ipAddress = connection.getRemoteAddressUDP().getAddress().getHostAddress();
			clientListener.newServerDiscovered(info.name, info.gameRules, info.connectedPlayers, ipAddress);
		}
		
		else if( obj instanceof Packet12GameStarted){
			Packet12GameStarted gameStarted = (Packet12GameStarted)obj;
			clientListener.startGame(gameStarted.playerState, gameStarted.gameState);
		}
		
		else if( obj instanceof Packet16LobbyUpdate){
			Packet16LobbyUpdate lobbyUpdate = (Packet16LobbyUpdate)obj;
			clientListener.lobbyUpdate(lobbyUpdate.lobbyState);
		}
		
		else if( obj instanceof Packet18ProjectileDestroyed){
			Packet18ProjectileDestroyed des = (Packet18ProjectileDestroyed)obj;
			clientListener.packetDestroyed(des.userID, des.seqNo);
		}
		
	}

	public void sendInputState(InputState inputState, long userID) {
		Packet09UserInput inputPacket = new Packet09UserInput();
		inputPacket.userID = userID;
		inputPacket.timeStamp = new Date().getTime();
		inputPacket.inputState = inputState;
		client.sendUDP(inputPacket);
	}

	public void startServerDiscovery() {
		
		List<InetAddress> list = client.discoverHosts(SERVER_PORT, 500);
		
		for(InetAddress address: list){
			try {
				client.connect(500, address, SERVER_PORT, SERVER_PORT);
				Packet10RequestInfo request = new Packet10RequestInfo();
				request.timeStamp = new Date().getTime();
				client.sendUDP(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendGameRulesUpdate(GameRules gameRules, long adminToken) {
		Packet14GameRulesChangeRequest grUpdate = new Packet14GameRulesChangeRequest();
		grUpdate.timeStamp = new Date().getTime();
		grUpdate.adminToken = adminToken;
		grUpdate.gameRules = gameRules;
		client.sendUDP(grUpdate);
	}

	public void sendStartRequest(long adminToken) {
		Packet13StartRequest startRequest = new Packet13StartRequest();
		startRequest.timeStamp = new Date().getTime();
		startRequest.adminToken = adminToken;
		client.sendUDP(startRequest);
	}

	public void stop() {
		client.stop();
	}

	public void sendAttack1Request(Vector2 dir, long userID) {
		Packet17Attack1Request attack = new Packet17Attack1Request();
		attack.userID = userID;
		attack.dir = dir;
		client.sendUDP(attack);
	}

	public void sendSlotUpdate(PlayerSlot slot) {
		Packet15SlotUpdate update = new Packet15SlotUpdate();
		update.timeStamp = new Date().getTime();
		update.playerSlot = slot;
		client.sendTCP(update);
	}
	
}
