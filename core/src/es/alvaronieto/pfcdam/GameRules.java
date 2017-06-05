package es.alvaronieto.pfcdam;

import static es.alvaronieto.pfcdam.Util.Constants.ARENA_WATER;
import static es.alvaronieto.pfcdam.Util.Constants.ARENA_LAVA;

public class GameRules {
	
	private String arenaPath;
	private String arenaName;
	private int maxPlayers;
	private int	gameLengthMinutes;
	private int gameLengthSeconds;
	private int rounds;
	
	public GameRules(){
		
	}
	
	public GameRules(String arenaPath, int maxPlayers, int gameLengthMinutes, int gameLengthSeconds, int rounds){
		this.arenaPath = arenaPath;
		this.maxPlayers = maxPlayers;
		this.gameLengthMinutes = gameLengthMinutes;
		this.gameLengthSeconds = gameLengthSeconds;
		this.rounds = rounds;
		setArenaName(arenaPath);
	}
	
	private void setArenaName(String arenaPath) {
		switch (arenaPath){
			case ARENA_LAVA: 
				arenaName = "Lava Cave";
				break;
			case ARENA_WATER:
				arenaName = "Island";
				break;
			default:
				arenaName = "Undefined";
		}
	}

	public static GameRules getDefault(){
		return new GameRules(ARENA_WATER, 2, 0, 30, 5);
	}

	public String getArenaPath() {
		return this.arenaPath;
	}
	
	public String getArenaName() {
		return this.arenaName;
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

	public int getRounds() {
		return this.rounds;
	}

}
