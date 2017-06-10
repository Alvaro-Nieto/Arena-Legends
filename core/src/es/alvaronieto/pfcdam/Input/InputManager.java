package es.alvaronieto.pfcdam.Input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.gameobjects.Player;

public class InputManager {

	private static float v = 0.35f;
	
	public static void applyInputToPlayer(InputState input, Player player) {
		
		Body body = player.getBody();
		
		if(input.isUpKey())
			body.applyLinearImpulse(new Vector2(0,v), body.getWorldCenter(), true);
		
		if(input.isDownKey())
			body.applyLinearImpulse(new Vector2(0,-v), body.getWorldCenter(), true);
		
		if(input.isRightKey())
			body.applyLinearImpulse(new Vector2(v,0), body.getWorldCenter(), true);
		
		if(input.isLeftKey())
			body.applyLinearImpulse(new Vector2(-v,0), body.getWorldCenter(), true);
	}
}
