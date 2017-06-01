package es.alvaronieto.pfcdam.States;

public class PlayerSlot {
	
	private int team;
	private String pj;
	private long userID;
	
	public PlayerSlot() {
		super();
	}

	public PlayerSlot(int team, String pj, long userID) {
		super();
		this.team = team;
		this.pj = pj;
		this.userID = userID;
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
		return "Empty";
	}
	
	
	
}
