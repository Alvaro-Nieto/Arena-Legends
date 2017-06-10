package es.alvaronieto.pfcdam.States;

public class PlayerSlot {
	
	private int team;
	private String pj;
	private long userID;
	private String playerName;
	
	public PlayerSlot() {
		super();
	}

	public PlayerSlot(int team, String pj, long userID, String playerName) {
		super();
		this.team = team;
		this.pj = pj;
		this.userID = userID;
		this.playerName = playerName;
	}

	public int getTeam() {
		return team;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public String getPj() {
		return pj;
	}
	public void setPj(String pj) {
		this.pj = pj;
	}
	
	public long getUserID() {
		return userID;
	}
	
	public void setUserID(long userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
	
}
