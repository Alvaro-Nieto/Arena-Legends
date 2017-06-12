package es.alvaronieto.pfcdam.States;

import com.badlogic.gdx.math.Vector2;

public class PlayerState extends BodyState {
	
	private String pj;
	private String playerName;
	
	public PlayerState(){
		super();
	}
	
	public PlayerState(Vector2 bodyPosition, Vector2 velocity, int team, String pj, String playerName){
		super(bodyPosition, velocity, team);
		this.pj = pj;
		this.playerName = playerName;
	}

	public PlayerState(Vector2 bodyPosition, long userID, String pj, Vector2 velocity, int team, String playerName){
		super(bodyPosition, userID, velocity, team);
		this.pj = pj;
		this.playerName = playerName;
		
	}
	
	public String getPj() {
		return pj;
	}

	public void setPj(String pj) {
		this.pj = pj;
	}

	public String getPlayerName() {
		return playerName;
	}
	
}
