package es.alvaronieto.pfcdam.Util;

public class BodyData {
	private long userID;
	private String type;
	private int team;
	private long seqNo; // only if projectile
	
	public BodyData(long userID, String type, int team) {
		this.userID = userID;
		this.type = type;
		this.team = team;
	}
	
	public BodyData(long userID, String type, int team, long seqNo) {
		this.userID = userID;
		this.type = type;
		this.team = team;
		this.seqNo = seqNo;
	}

	public long getUserID() {
		return userID;
	}

	public String getType() {
		return type;
	}

	public int getTeam() {
		return team;
	}
	
	public long getSeqNo() {
		return seqNo;
	}

	@Override
	public String toString() {
		return "BodyData [userID=" + userID + ", type=" + type + ", team=" + team + ", seqNo=" + seqNo + "]";
	}
	
}
