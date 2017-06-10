package es.alvaronieto.pfcdam.States;

import com.badlogic.gdx.math.Vector2;

public class BodyState {

	private long userID;
	private Vector2 bodyPosition;
	private Vector2 velocity;
	private int team;
	
	public BodyState(){
		
	}
	
	public BodyState(Vector2 bodyPosition, Vector2 velocity, int team){
		this.bodyPosition = bodyPosition;
		this.velocity = velocity;
	}
	
	public BodyState(Vector2 bodyPosition, long userID, Vector2 velocity, int team){
		this(bodyPosition, velocity, team);
		this.userID = userID;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public Vector2 getBodyPosition() {
		return bodyPosition;
	}

	public void setBodyPosition(Vector2 bodyPosition) {
		this.bodyPosition = bodyPosition;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	@Override
	public String toString() {
		return "["+bodyPosition.x + ","+ bodyPosition.y +"]";
	}

	public int getTeam() {
		return team;
	}
		
	
}
