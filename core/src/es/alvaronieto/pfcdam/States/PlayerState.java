package es.alvaronieto.pfcdam.States;

import com.badlogic.gdx.math.Vector2;

public class PlayerState extends BodyState {
	
	private String pj;
	
	public PlayerState(){
		super();
	}
	
	public PlayerState(Vector2 bodyPosition, Vector2 velocity, int team, String pj){
		super(bodyPosition, velocity, team);
		this.pj = pj;
	}

	public PlayerState(Vector2 bodyPosition, long userID, String pj, Vector2 velocity, int team){
		super(bodyPosition, userID, velocity, team);
		this.pj = pj;
	}
	
	public String getPj() {
		return pj;
	}

	public void setPj(String pj) {
		this.pj = pj;
	}
	
}
