package es.alvaronieto.pfcdam.net.kryoserver;

import static es.alvaronieto.pfcdam.Util.Constants.TRUEMO;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import es.alvaronieto.pfcdam.Input.InputManager;
import es.alvaronieto.pfcdam.States.InputState;
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
import es.alvaronieto.pfcdam.net.Util;

public class TestServer extends Listener {
	// Connection info
	int serverPort = 25565;
	
	// Kyonet "Server" object
	private Server server;
	
	private HashMap<Long, ConnectedClient> clients;
	
	private Game game;
	private World world;
	
	//
	private Random rnd;
	private SimpleDateFormat dateFormat;
	private final int MAX_CLIENTS = 3;
	
	private boolean gameStarted;
	
	private volatile long currentTick;
	
	public TestServer(int maxClients) {
		clients = new HashMap<Long, ConnectedClient>();
		server = new Server();
		
		world = new World(Vector2.Zero, true);
		game = new Game();

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
		new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				currentTick++;
				world.step(1/60f, 6, 2);
				sendSnapshot(currentTick);
			}
		}, 0, (long) (1000/60f), TimeUnit.MILLISECONDS);
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
		
	}
	
	private void processMessage(Packet01Message msg) {
		System.out.println(dateFormat.format(new Date((msg.timeStamp)))+" : [S](CLIENT) >> " + msg.message);
	}

	private void processConnectionRequest(Connection connection, Packet02ConnectionRequest request) {
		if(clients.size() >= MAX_CLIENTS)
			rejectConnection(connection);
		else {
			// De momento la partida empieza con el primer jugador. No hay sala de espera.
			if(!gameStarted)
				startGame(); // TODO
			
			acceptConnection(connection);
		}
	}

	private void startGame() {
		gameStarted = true;
		startSimulation();
		// TODO 
	}

	private void rejectConnection(Connection connection) {
		Packet04ConnectionRejected rejected = new Packet04ConnectionRejected();
		rejected.timeStamp = new Date().getTime();
		// TODO
		System.out.println("[S] Conexión rechazada a un cliente");
		connection.close();
	}

	private void acceptConnection(Connection connection) {		
		final Packet03ConnectionAccepted accepted = new Packet03ConnectionAccepted();
		
		accepted.userID = getNewUserID();
		accepted.timeStamp = new Date().getTime();
		accepted.playerState = new PlayerState(new Vector2(1,1),accepted.userID, TRUEMO);
		accepted.gameState = game.getGameState();
		
		Gdx.app.postRunnable(new Runnable() {
		    @Override
		    public void run() {
		    	game.addPlayer(new Player(world, accepted.playerState));
		    }
		});
		connection.sendTCP(accepted);
		
		System.out.println("[S] Conectado cliente con ID: "+ accepted.userID);
		clients.put(accepted.userID,new ConnectedClient(accepted.userID, connection));
		
		Packet05ClientConnected clientConnected = new Packet05ClientConnected();
		clientConnected.userID = accepted.userID;
		clientConnected.timeStamp = new Date().getTime();
		clientConnected.playerState = accepted.playerState;
		TCPBroadcastExcept(clientConnected, connection);
	}
	
	private void proccessUserInput(Connection connection, Packet09UserInput inputPacket) {
		Player player = game.getPlayer(inputPacket.userID);
		InputState input = inputPacket.inputState;
		ConnectedClient client = clients.get(inputPacket.userID);
	
		client.setLastInputAccepted(inputPacket.inputState.getSequenceNumber());
		InputManager.applyInputToPlayer(input, player);
		//UDPBroadcastExcept(inputPacket, connection);
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
