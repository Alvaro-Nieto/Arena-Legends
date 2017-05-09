package es.alvaronieto.pfcdam.gameobjects;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.PlayerState;

public class Game {
	private HashMap<Long, Player> players;
	
	
	public Game(){
		this.players = new HashMap<Long, Player>();
	}
	
	public Game(World world, GameState gameState){
		this();
		HashMap<Long, PlayerState> playerStates = gameState.getPlayers();
		for(Long userID : playerStates.keySet()){
			this.players.put(userID, new Player(world, playerStates.get(userID)));
		}
	}
	
	public void addPlayer(Player player){
		players.put(player.getUserID(), player);
	}
	
	public boolean removePlayer(long playerID){
		return players.remove(playerID) != null;
	}
	
	public Player getPlayer(long userID){
		return players.get(userID);
	}

	public HashMap<Long, Player> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<Long, Player> players) {
		this.players = players;
	}
	
	public GameState getGameState(){
		return new GameState(players);
	}
	
	public World resetWorld(GameState gameState){
		World world = new World(Vector2.Zero, true);
		this.players = new HashMap<Long, Player>();
		HashMap<Long, PlayerState> playerStates = gameState.getPlayers();
		for(Long userID : playerStates.keySet()){
			//System.out.println(playerStates.get(userID).getPj());
			this.players.put(userID, new Player(world, playerStates.get(userID)));
		}
		return world;
	}

}
