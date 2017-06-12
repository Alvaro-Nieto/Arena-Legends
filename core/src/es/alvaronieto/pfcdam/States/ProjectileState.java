package es.alvaronieto.pfcdam.States;

import com.badlogic.gdx.math.Vector2;

public class ProjectileState extends BodyState {
	
	private String type;
	private long seqNo;
	
	public ProjectileState(){
		super();
	}
	
	public ProjectileState(Vector2 bodyPosition, long userID, Vector2 velocity, int team, String type, long seqNo) {
		super(bodyPosition, userID, velocity, team);
		this.type = type;
		this.seqNo = seqNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSeqNo() {
		return seqNo;
	}
	
	
}
