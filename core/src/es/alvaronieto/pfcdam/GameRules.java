package es.alvaronieto.pfcdam;

import static es.alvaronieto.pfcdam.Util.Constants.ARENA_WATER;

public class GameRules {
	
	private String arena;
	private int maxPlayers;
	private int	gameLengthMinutes;
	private int gameLengthSeconds;
	
	public GameRules(){
		
	}
	
	public GameRules(String arena, int maxPlayers, int gameLengthMinutes, int gameLengthSeconds){
		this.arena = arena;
		this.maxPlayers = maxPlayers;
		this.gameLengthMinutes = gameLengthMinutes;
		this.gameLengthSeconds = gameLengthSeconds;
	}
	
	public static GameRules getDefault(){
		return new GameRules(ARENA_WATER, 4, 0, 30);
	}

	public String getArena() {
		return this.arena;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getGameLengthMinutes() {
		return gameLengthMinutes;
	}

	public int getGameLengthSeconds() {
		return gameLengthSeconds;
	}

}
