package es.alvaronieto.pfcdam.States;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class PlayerState implements Serializable {
	
	private long playerID;
	private Vector2 position;
	
	public PlayerState(){
		
	}
	
	public PlayerState(Vector2 position){
		this.position = position;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "["+position.x + ","+ position.y +"]";
	}
		
	
}
