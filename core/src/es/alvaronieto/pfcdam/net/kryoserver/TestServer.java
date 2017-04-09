package es.alvaronieto.pfcdam.net.kryoserver;

import java.io.IOException;
import java.net.BindException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.gameobjects.Game;
import es.alvaronieto.pfcdam.gameobjects.Player;
import es.alvaronieto.pfcdam.net.Util;
import es.alvaronieto.pfcdam.net.Packets.Packet01Message;
import es.alvaronieto.pfcdam.net.Packets.Packet02ConnectionRequest;
import es.alvaronieto.pfcdam.net.Packets.Packet03ConnectionAccepted;
import es.alvaronieto.pfcdam.net.Packets.Packet04ConnectionRejected;
import es.alvaronieto.pfcdam.net.Packets.Packet05ClientConnected;
import es.alvaronieto.pfcdam.net.Packets.Packet08GameUpdate;
import es.alvaronieto.pfcdam.net.Packets.Packet09UserInput;



public class TestServer extends Listener {
	// Connection info
	int serverPort = 25565;
	
	
	// Kyonet "Server" object
	private Server server;
	
	//
	private List<Connection> clients;
	
	private Game game;
	private World world;
	
	//
	
	private Random rnd;
	private SimpleDateFormat dateFormat;
	private final int MAX_CLIENTS = 3;
	
	public TestServer(int maxClients) {
		clients = new ArrayList<Connection>();
		server = new Server();
		
		
		world = new World(Vector2.Zero, true);
		game = new Game();
		
		new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				world.step(1/60f, 6, 2);
				sendSnapshot();
			}
		}, 0, (long) (1000/60f), TimeUnit.MILLISECONDS);
		System.out.println((long)(1000/30f));
		
		//snl = new ServerKryoListener(clients, maxClients);
		this.rnd = new Random();
		this.dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		server.addListener(this);

		try {
			server.bind(serverPort, serverPort);
		} catch(BindException be){
			System.err.println("en uso");
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		registerPackets();
		
		server.start();
		
	}
	
	protected void sendSnapshot() {
		Packet08GameUpdate snapshot = new Packet08GameUpdate();
		snapshot.gameState = game.getGameState();
		snapshot.timeStamp = new Date().getTime();
		UDPBroadcast(snapshot);
	}

	private void registerPackets(){
		Util.registerPackets(server.getKryo());
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
		
		else if( obj instanceof Packet02ConnectionRequest ){
			Packet02ConnectionRequest r = (Packet02ConnectionRequest)obj;
			
			if(clients.size() < MAX_CLIENTS){
				Packet03ConnectionAccepted accepted = new Packet03ConnectionAccepted();
				
				accepted.userID = rnd.nextLong();
				accepted.timeStamp = new Date().getTime();
				accepted.playerState = new PlayerState(new Vector2(1,1),accepted.userID);
				accepted.gameState = game.getGameState();
				
				/*System.out.println("Estados enviados:");
				HashMap<Long, PlayerState> playerStates = accepted.gameState.getPlayers();
				for(Long userID : playerStates.keySet()){
					PlayerState playerState = playerStates.get(userID);
					System.out.println(playerState.getUserID() + " : " +  playerState.getPosition().toString());
				}*/
				
				game.addPlayer(new Player(world, accepted.playerState));
				connection.sendTCP(accepted);
				
				System.out.println("[S] Conectado cliente con ID: "+ accepted.userID);
				clients.add(connection);
				Packet05ClientConnected clientConnected = new Packet05ClientConnected();
				clientConnected.userID = accepted.userID;
				clientConnected.timeStamp = new Date().getTime();
				clientConnected.playerState = accepted.playerState;
				TCPBroadcastExcept(clientConnected, connection);
			} 
			else {
				Packet04ConnectionRejected rejected = new Packet04ConnectionRejected();
				rejected.timeStamp = new Date().getTime();
				System.out.println("[S] Conexión rechazada a un cliente");
				connection.close();
			}
			
		}
		else if( obj instanceof Packet09UserInput ){
			Packet09UserInput inputPacket = (Packet09UserInput)obj;
			//System.out.println(inputPacket.userID + " INPUT >> "+ inputPacket.inputState);
			Body body = game.getPlayer(inputPacket.userID).getBody();
			InputState input = inputPacket.inputState;
			if(input.isUpKey()){
				if(input.isRightKey()){
					body.applyLinearImpulse(new Vector2(0.4f,0.4f),body.getWorldCenter(), true);
				} 
				else if(input.isLeftKey()){
					body.applyLinearImpulse(new Vector2(-0.4f,0.4f),body.getWorldCenter(), true);
				} 
				else{
					body.applyLinearImpulse(new Vector2(0,0.8f),body.getWorldCenter(), true);
				}
			} 
			else if(input.isDownKey()){
				if(input.isRightKey()){
					body.applyLinearImpulse(new Vector2(0.4f,-0.4f),body.getWorldCenter(), true);
		        } 
				else if(input.isLeftKey()){
					body.applyLinearImpulse(new Vector2(-0.4f,-0.4f),body.getWorldCenter(), true);
		        } else{
		        	body.applyLinearImpulse(new Vector2(0,-0.8f),body.getWorldCenter(), true);
		        }
	        	
	        }
			else if(input.isRightKey()){
				body.applyLinearImpulse(new Vector2(0.8f,0),body.getWorldCenter(), true);
	        } 
			else if(input.isLeftKey() && body.getLinearVelocity().x >= -4){
				body.applyLinearImpulse(new Vector2(-0.8f,0),body.getWorldCenter(), true);
	        }
			
			UDPBroadcastExcept(inputPacket, connection);
			
			//System.out.println(body.getPosition());
		}
		
		/*
		else if( obj instanceof PlayerState ){
			PlayerState playerState = (PlayerState)obj;
			for(Connection client : clients){
				if(!client.equals(connection)){
					client.sendTCP(playerState);
				}
			}
			System.out.println("[S]Recibido player: " + playerState.toString());
		}*/
		
		
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
	
	private void UDPBroadcast(Object object){
		for(Connection client : clients)
			client.sendUDP(object);
	}
	
	
}
