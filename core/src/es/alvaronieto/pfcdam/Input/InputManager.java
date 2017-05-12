package es.alvaronieto.pfcdam.Input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.gameobjects.Player;

public class InputManager {

	// TODO temp
	public static void applyInputToPlayer(InputState input, Player player) {
		Body body = player.getBody();
		if(input.isUpKey()){
			if(input.isRightKey()){
				body.applyLinearImpulse(new Vector2(0.4f,0.4f),body.getWorldCenter(), true);
			} 
			else if(input.isLeftKey()){
				body.applyLinearImpulse(new Vector2(-0.4f,0.4f),body.getWorldCenter(), true);
			} 
			else{
				body.applyLinearImpulse(new Vector2(0,0.8f),body.getWorldCenter(), true);
			}
		} 
		else if(input.isDownKey()){
			if(input.isRightKey()){
				body.applyLinearImpulse(new Vector2(0.4f,-0.4f),body.getWorldCenter(), true);
		    } 
			else if(input.isLeftKey()){
				body.applyLinearImpulse(new Vector2(-0.4f,-0.4f),body.getWorldCenter(), true);
		    } else{
		    	body.applyLinearImpulse(new Vector2(0,-0.8f),body.getWorldCenter(), true);
		    }
			
		}
		else if(input.isRightKey()){
			body.applyLinearImpulse(new Vector2(0.8f,0),body.getWorldCenter(), true);
		} 
		else if(input.isLeftKey() && body.getLinearVelocity().x >= -4){
			body.applyLinearImpulse(new Vector2(-0.8f,0),body.getWorldCenter(), true);
		}
	}
}
