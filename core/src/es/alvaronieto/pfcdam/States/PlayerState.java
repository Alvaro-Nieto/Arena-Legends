package es.alvaronieto.pfcdam.States;

import com.badlogic.gdx.math.Vector2;

public class PlayerState extends BodyState {
	
	private String pj;
	private String playerName;
	private int health;
	
	public PlayerState(){
		super();
	}

	public PlayerState(Vector2 bodyPosition, long userID, String pj, Vector2 velocity, int team, String playerName, int health){
		super(bodyPosition, userID, velocity, team);
		this.pj = pj;
		this.playerName = playerName;
		this.health = health;
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

	public int getHealth() {
		return health;
	}
	
}
