package es.alvaronieto.pfcdam.States;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class PlayerState implements Serializable {
	
	private long userID;
	private Vector2 bodyPosition;
	private Vector2 velocity;
	private String pj;
	
	public PlayerState(){
		
	}
	
	public PlayerState(Vector2 bodyPosition, Vector2 velocity){
		this.bodyPosition = bodyPosition;
		this.velocity = velocity;
	}
	
	public PlayerState(Vector2 bodyPosition, long userID, String pj, Vector2 velocity){
		this(bodyPosition, velocity);
		this.userID = userID;
		this.pj = pj;
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

	public String getPj() {
		return pj;
	}

	public void setPj(String pj) {
		this.pj = pj;
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
		
	
}
