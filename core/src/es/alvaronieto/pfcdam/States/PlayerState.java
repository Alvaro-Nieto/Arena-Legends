package es.alvaronieto.pfcdam.States;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class PlayerState implements Serializable {
	
	private long userID;
	private Vector2 position;
	private String pj;
	
	public PlayerState(){
		
	}
	
	public PlayerState(Vector2 position){
		this.position = position;
	}
	
	public PlayerState(Vector2 position, long userID, String pj){
		this.position = position;
		this.userID = userID;
		this.pj = pj;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public String getPj() {
		return pj;
	}

	public void setPj(String pj) {
		this.pj = pj;
	}

	@Override
	public String toString() {
		return "["+position.x + ","+ position.y +"]";
	}
		
	
}
