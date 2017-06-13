package es.alvaronieto.pfcdam.States;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.utils.Array;

import es.alvaronieto.pfcdam.gameobjects.GameRules;
import es.alvaronieto.pfcdam.gameobjects.Player;
import es.alvaronieto.pfcdam.gameobjects.Projectile;

public class GameState {
	
	private GameRules gameRules;
	private HashMap<Long, PlayerState> playerStates;
	private HashMap<Long, HashMap<Long, ProjectileState>> projectileStates;
	
	private GameState(){
		this.playerStates = new HashMap<Long, PlayerState>();
		this.projectileStates = new HashMap<>();
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

	public GameState(HashMap<Long, Player> players, HashMap<Long, ConcurrentHashMap<Long, Projectile>> projectiles, GameRules gameRules) {
		this(gameRules);
		for(Long userID : players.keySet()){
			playerStates.put(userID, players.get(userID).getPlayerState());
		}
		for(Long userID : projectiles.keySet()){
			for(Long seqNo : projectiles.get(userID).keySet()){
				Projectile projectile = projectiles.get(userID).get(seqNo);
				if(!projectileStates.containsKey(userID))
					projectileStates.put(userID, new HashMap<Long, ProjectileState>());
				if(!projectile.isDisposed() || !projectile.shouldDispose())
					projectileStates.get(userID).put(seqNo, projectile.getProjectileState());
			}
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

	public HashMap<Long, HashMap<Long, ProjectileState>> getProjectileStates() {
		return projectileStates;
	}
	
}
