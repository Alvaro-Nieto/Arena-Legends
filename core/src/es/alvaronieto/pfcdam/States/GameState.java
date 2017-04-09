package es.alvaronieto.pfcdam.States;

import java.util.HashMap;

import es.alvaronieto.pfcdam.gameobjects.Player;

public class GameState {
	private HashMap<Long, PlayerState> playerStates;
	
	public GameState(){
		this.playerStates = new HashMap<Long, PlayerState>();
	}
	

	public GameState(HashMap<Long, Player> players) {
		this();
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

	
}
