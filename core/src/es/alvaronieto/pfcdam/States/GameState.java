package es.alvaronieto.pfcdam.States;

import java.util.HashMap;

import es.alvaronieto.pfcdam.gameobjects.GameRules;
import es.alvaronieto.pfcdam.gameobjects.Player;

public class GameState {
	
	private GameRules gameRules;
	private HashMap<Long, PlayerState> playerStates;
	
	private GameState(){
		this.playerStates = new HashMap<Long, PlayerState>();
	}
	
	public GameState(GameRules gameRules){
		this();
		this.gameRules = gameRules;
	}
	
	public GameState(GameRules gameRules, PlayerState playerState){
		this();
		this.gameRules = gameRules;
		playerStates.put(playerState.getUserID(), playerState);
	}

	public GameState(HashMap<Long, Player> players, GameRules gameRules) {
		this(gameRules);
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

	public GameRules getGameRules() {
		return this.gameRules;
	}
	
}
