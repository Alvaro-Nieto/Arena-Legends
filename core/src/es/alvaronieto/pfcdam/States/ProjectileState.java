package es.alvaronieto.pfcdam.States;

import com.badlogic.gdx.math.Vector2;

public class ProjectileState extends BodyState {
	
	private String type;
	private long seqNo;
	private boolean disposed;
	
	public ProjectileState(){
		super();
	}
	
	public ProjectileState(Vector2 bodyPosition, long userID, Vector2 velocity, int team, String type, long seqNo, boolean disposed) {
		super(bodyPosition, userID, velocity, team);
		this.type = type;
		this.seqNo = seqNo;
		this.disposed = disposed;
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
	
	public boolean isDisposed() {
		return disposed;
	}
	
}
