package es.alvaronieto.pfcdam.net.kryoserver;

import static es.alvaronieto.pfcdam.Util.Constants.SERVER_PORT;
import static es.alvaronieto.pfcdam.Util.Constants.STEP;

import java.io.IOException;
import java.net.BindException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import es.alvaronieto.pfcdam.GameRules;
import es.alvaronieto.pfcdam.Input.InputManager;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerSlot;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.gameobjects.Game;
import es.alvaronieto.pfcdam.gameobjects.Player;
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
import es.alvaronieto.pfcdam.net.Util;

public class TestServer extends Listener {
	// Connection info
	int serverPort = SERVER_PORT;
	
	// Kryonet "Server" object
	private Server server;
	
	private HashMap<Long, ConnectedClient> clients;
	private HashMap<Long, PlayerState> initialPlayerStates;
	
	private LobbyState lobbyState;
	//private HashMap<Long, PlayerSlot> slotsTeam1;
	//private HashMap<Long, PlayerSlot> slotsTeam2;
	
	
	private Game game;
	//private World world;
	
	//
	private Random rnd;
	private SimpleDateFormat dateFormat;
	//private final int MAX_CLIENTS;
	
	private boolean gameStarted;
	
	private volatile long currentTick;
	private boolean isDemo;

	private long adminToken;

	public TestServer(GameRules gameRules, long adminToken, boolean isDemo) {
		this.adminToken = adminToken;
		this.isDemo = isDemo;
		this.lobbyState = new LobbyState(gameRules);
		
		clients = new HashMap<Long, ConnectedClient>();
		server = new Server();
		
		this.initialPlayerStates = new HashMap<Long, PlayerState>();
		
		//MAX_CLIENTS = gameRules.getMaxPlayers();
		//game = new Game(gameRules);
		
		//
		
		rnd = new Random();
		dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		gameStarted = false;
		
		server.addListener(this);

		try {
			server.bind(serverPort, serverPort);
		} catch(BindException be){
			System.err.println("en uso");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Util.registerPackets(server.getKryo());
		
		server.start();
	}

	private void startSimulation() {
		this.currentTick = 0;
		game.start();
		new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				tick();
			}
		}, 0, (long)(STEP*1000), TimeUnit.MILLISECONDS);
	}

	private void tick() {
		currentTick++;
		game.update();
		game.step();
		sendSnapshot(currentTick);
	}
	
	protected void sendSnapshot(long currentTick) {
		for(Map.Entry<Long, ConnectedClient> entry : clients.entrySet()){
			Packet08GameUpdate snapshot = new Packet08GameUpdate();
			ConnectedClient client = entry.getValue();
			snapshot.gameState = game.getGameState();
			snapshot.timeStamp = new Date().getTime();
			snapshot.userLastInputProccessed = client.getLastInputAccepted();
			snapshot.serverCurrentTick = currentTick;
			client.sendUDP(snapshot);
		}
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
		if( obj instanceof Packet01Message )
			processMessage((Packet01Message)obj);
		
		else if( obj instanceof Packet02ConnectionRequest )
			processConnectionRequest(connection, (Packet02ConnectionRequest)obj);
		
		else if( obj instanceof Packet09UserInput )
			proccessUserInput(connection, (Packet09UserInput)obj);
		
		else if( obj instanceof Packet10RequestInfo )
			sendServerInfo(connection, (Packet10RequestInfo)obj);
		
		else if( obj instanceof Packet13StartRequest )
			processStartRequest((Packet13StartRequest)obj);
		
		else if( obj instanceof Packet14GameRulesChangeRequest )
			proccessGameRulesUpdate((Packet14GameRulesChangeRequest)obj);
		
		else if( obj instanceof Packet15SlotUpdate )
			processSlotUpdate(connection, (Packet15SlotUpdate)obj);
	}

	private void sendSlotUpdate(PlayerSlot playerSlot) {
		TCPBroadcast(playerSlot);
	}

	private void proccessGameRulesUpdate(Packet14GameRulesChangeRequest grChange) {
		if(grChange.adminToken == this.adminToken) {
			this.lobbyState.setGameRules(grChange.gameRules);
			sendLobbyState();
		}
	}

	private void processStartRequest(Packet13StartRequest startRequest) {
		if((lobbyState.isReadyToStart() || isDemo ) && startRequest.adminToken == this.adminToken)
			startGame();
	}

	private void processMessage(Packet01Message msg) {
		System.out.println(dateFormat.format(new Date((msg.timeStamp)))+" : [S](CLIENT) >> " + msg.message);
	}

	private void processConnectionRequest(Connection connection, Packet02ConnectionRequest request) {
		if(clients.size() >=  lobbyState.getMaxPlayersPerTeam()*2)
			rejectConnection(connection, "Lobby full");
		else {
			acceptConnection(connection, request.adminToken == this.adminToken);
		}
	}
	
	private void processSlotUpdate(Connection connection, Packet15SlotUpdate packet) {
		if (lobbyState.trySlotUpdate(packet.playerSlot));
			sendLobbyState();
	}
	
	private void sendLobbyState() {
		Packet16LobbyUpdate lobbyUpdate = new Packet16LobbyUpdate();
		lobbyUpdate.timeStamp = new Date().getTime();
		lobbyUpdate.lobbyState = lobbyState;
		TCPBroadcast(lobbyUpdate);
	}

	private void sendServerInfo(Connection connection, Packet10RequestInfo obj) {
		Packet11InfoAnswer info = new Packet11InfoAnswer();
		info.timeStamp = new Date().getTime();
		info.gameRules = lobbyState.getGameRules();
		info.connectedPlayers = clients.size();
		connection.sendUDP(info);
	}

	private void startGame() {
		gameStarted = true;
		
		Gdx.app.postRunnable(new Runnable(){

			@Override
			public void run() {
				game = new Game(lobbyState);
				System.out.println("[Debug] Players: "+game.getPlayers().size());
				for(Map.Entry<Long, ConnectedClient> entry : clients.entrySet()){
					Packet12GameStarted gameStarted = new Packet12GameStarted();
					ConnectedClient client = entry.getValue();
					long userID = entry.getKey();
					
					gameStarted.userID = userID;
					gameStarted.timeStamp = new Date().getTime();
					gameStarted.playerState = game.getPlayer(userID).getPlayerState();
					gameStarted.gameState = game.getGameState();
					
					client.sendTCP(gameStarted);
				}
				startSimulation();
			}
		});
	}

	private void rejectConnection(Connection connection, String reason) {
		Packet04ConnectionRejected rejected = new Packet04ConnectionRejected();
		rejected.timeStamp = new Date().getTime();
		rejected.reason = reason;
		connection.sendUDP(rejected);
		connection.close();
	}

	private void acceptConnection(Connection connection, boolean admin) {
		
		long userID = getNewUserID();
		if(lobbyState.newPlayer(userID)){
			final Packet03ConnectionAccepted accepted = new Packet03ConnectionAccepted();
			accepted.userID = userID;
			accepted.timeStamp = new Date().getTime();
			accepted.lobbyState = this.lobbyState;
			accepted.admin = admin;
			
			connection.sendTCP(accepted);
			sendLobbyState();
			
			System.out.println("[S] Conectado cliente con ID: "+ accepted.userID);
			clients.put(accepted.userID,new ConnectedClient(accepted.userID, connection));
			
			Packet05ClientConnected clientConnected = new Packet05ClientConnected();
			clientConnected.userID = accepted.userID;
			clientConnected.timeStamp = new Date().getTime();
			TCPBroadcastExcept(clientConnected, connection);
		}
	}
	
	private void proccessUserInput(Connection connection, Packet09UserInput inputPacket) {
		Player player = game.getPlayer(inputPacket.userID);
		InputState input = inputPacket.inputState;
		ConnectedClient client = clients.get(inputPacket.userID);
	
		client.setLastInputAccepted(inputPacket.inputState.getSequenceNumber());
		
		InputManager.applyInputToPlayer(input, player);
	}
	
	private void TCPBroadcastExcept(Object object, Connection exceptionConnection){
		for(Map.Entry<Long, ConnectedClient> entry : clients.entrySet()){
			ConnectedClient client = entry.getValue();
			if(!client.equals(exceptionConnection)){
				client.sendTCP(object);
			}
		}
	}
	
	private void UDPBroadcastExcept(Object object, Connection exceptionConnection){
		for(Map.Entry<Long, ConnectedClient> entry : clients.entrySet()){
			ConnectedClient client = entry.getValue();
			if(!client.equals(exceptionConnection)){
				client.sendUDP(object);
			}
		}
	}
	
	private void TCPBroadcast(Object object){
		for(Map.Entry<Long, ConnectedClient> entry : clients.entrySet()){
			ConnectedClient client = entry.getValue();
			client.sendTCP(object);
		}
	}
	
	private void UDPBroadcast(Object object){
		for(Map.Entry<Long, ConnectedClient> entry : clients.entrySet()){
			ConnectedClient client = entry.getValue();
			client.sendUDP(object);
		}
	}
	
	private long getNewUserID(){
		long userID;
		while(clients.containsKey((userID = rnd.nextLong())))
			;
		return userID;
	}
	
	
}
