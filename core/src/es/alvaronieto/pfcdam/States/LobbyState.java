package es.alvaronieto.pfcdam.States;

import static es.alvaronieto.pfcdam.Util.Constants.TRUEMO;

import java.util.HashMap;

import es.alvaronieto.pfcdam.GameRules;

public class LobbyState {
	private GameRules gameRules;
	private HashMap<Long, PlayerSlot> slots;
	private int maxPlayersPerTeam;
	private int playersTeam1;
	private int playersTeam2;
	private int playersWithoutTeam;
	private boolean gameRunning;

	public LobbyState() {
		this.gameRules = GameRules.getDefault();
		this.slots = new HashMap<>();
		this.maxPlayersPerTeam = gameRules.getMaxPlayers() / 2;
		this.playersTeam1 = 0;
		this.playersTeam2 = 0;
		this.playersWithoutTeam = 0;
		this.gameRunning = false;
	}
	
	public LobbyState(GameRules gameRules) {
		this();
		setGameRules(gameRules);
	}

	public boolean isReadyToStart() {
		return slots.size() == gameRules.getMaxPlayers() &&
				playersTeam1 == maxPlayersPerTeam &&
				playersTeam2 == maxPlayersPerTeam &&
				!gameRunning;
	}

	public boolean trySlotUpdate(PlayerSlot newSlot) {
		boolean success = false;
		
		PlayerSlot oldSlot = slots.get(newSlot.getUserID());
		if(oldSlot.getTeam() != newSlot.getTeam())
			success = tryJoinTeam(oldSlot, newSlot);
		if(oldSlot.getPj() != newSlot.getPj() && !success) {
			success = true;
		}
		if(success)
			slots.put(newSlot.getUserID(), newSlot);
		return success;
	}

	private boolean tryJoinTeam(PlayerSlot oldSlot, PlayerSlot newSlot) {
		boolean success = true;
		if((newSlot.getTeam() == 1 && playersTeam1 < maxPlayersPerTeam)){
			playersTeam1++;
			playersTeam2--;
		}
		else if((newSlot.getTeam() == 2 && playersTeam2 < maxPlayersPerTeam)){
			playersTeam2++;
			playersTeam1--;
		} 
		else if(newSlot.getTeam() == 0){
			playersWithoutTeam++;
			if(oldSlot.getTeam() == 1)
				playersTeam1--;
			else if(oldSlot.getTeam() == 2)
				playersTeam2--;
		}
		else if(oldSlot.getTeam() == 0 && 
				((newSlot.getTeam() == 1 && playersTeam1 < maxPlayersPerTeam)||
				(newSlot.getTeam() == 2 && playersTeam2 < maxPlayersPerTeam))){
			
			playersWithoutTeam--;
			if(newSlot.getTeam() == 1)
				playersTeam1++;
			else if(newSlot.getTeam() == 2)
				playersTeam2++;
		} else {
			success = false;
		}
		return success;
	}

	public boolean newPlayer(long userID){
		if(slots.size() < gameRules.getMaxPlayers() && !slots.containsKey(userID)) {
			
			int team = playersTeam1 <= playersTeam2 ? 1 : 2;
			String pj = TRUEMO;
			
			slots.put(userID, new PlayerSlot(team, pj, userID));
			
			if(team == 1)
				playersTeam1++;
			else
				playersTeam2++;
			
			return true;
		}
		return false;
	}
	
	public boolean removePlayer(long userID) {
		if(slots.containsKey(userID)){
			PlayerSlot slot = slots.get(userID);
			switch (slot.getTeam()){
				case 1:
					playersTeam1--;
					break;
				case 2:
					playersTeam2--;
					break;
				case 0:
					playersWithoutTeam--;
					break;
			}
			slots.remove(userID);
			return true;
		}
		return false;
	}
	
	public void setGameRules(GameRules gameRules){
		this.gameRules = gameRules;
		this.maxPlayersPerTeam = gameRules.getMaxPlayers() / 2;
	}

	public GameRules getGameRules() {
		return this.gameRules;
	}

	public HashMap<Long, PlayerSlot> getPlayerSlots() {
		return this.slots;
	}

	public int getMaxPlayersPerTeam() {
		return maxPlayersPerTeam;
	}

	public int getPlayersTeam1() {
		return playersTeam1;
	}

	public int getPlayersTeam2() {
		return playersTeam2;
	}

	public int getPlayersWithoutTeam() {
		return playersWithoutTeam;
	}

	public boolean isGameRunning() {
		return gameRunning;
	}
	
	
	
}
