package es.alvaronieto.pfcdam;

import static es.alvaronieto.pfcdam.Util.Constants.ARENA_WATER;

public class GameRules {
	
	private String arena;
	private int maxPlayers;
	
	public GameRules(){
		
	}
	
	public GameRules(String arena, int maxPlayers){
		this.arena = arena;
		this.maxPlayers = maxPlayers;
	}
	
	public static GameRules getDefault(){
		return new GameRules(ARENA_WATER, 4);
	}

	public String getArena() {
		return this.arena;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
	
}
