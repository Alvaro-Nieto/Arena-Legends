package es.alvaronieto.pfcdam.States;

import java.util.HashMap;

import es.alvaronieto.pfcdam.gameobjects.Player;

public class GameState {
	
	private String arena;
	private HashMap<Long, PlayerState> playerStates;
	
	public GameState(){
		this.playerStates = new HashMap<Long, PlayerState>();
	}
	
	public GameState(String arena){
		this();
		this.arena = arena;
	}

	public GameState(HashMap<Long, Player> players, String arena) {
		this(arena);
		for(Long userID : players.keySet()){
			playerStates.put(userID, players.get(userID).getPlayerState());
		}
	}


	public HashMap<Long, PlayerState> getPlayers() {
		return playerStates;
	}
	
	public void setPlayers(HashMap<Long, PlayerState> players) {
		this.playerStates = players;
	}


	public void setArena(String arena) {
		this.arena = arena;
	}

	public String getArena() {
		return this.arena;
	}


	
}
