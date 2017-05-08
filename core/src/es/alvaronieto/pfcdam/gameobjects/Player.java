package es.alvaronieto.pfcdam.gameobjects;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.States.PlayerState;

public class Player {
	
	private Body body;
	private long userID;
	


	public Player(World world, Vector2 position, long userID){
		
		this.userID = userID;
		BodyDef bdef = new BodyDef();	
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		bdef.type = BodyDef.BodyType.DynamicBody; 
		bdef.position.set(position); // Desde el centro (Coordenadas b2d)
		
		shape.setAsBox(16 / Juego.PPM, 16 / Juego.PPM);
		fdef.shape = shape;
		
		body = world.createBody(bdef);
		body.createFixture(fdef);	
		body.setLinearDamping(10f);
	}
	
	public Player(World world, PlayerState playerState){
		this(world, playerState.getPosition(), playerState.getUserID());
	}
	
	public void update(float delta){
		
	}

	
	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public Body getBody() {
		return body;
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public void setPosition(Vector2 position) {
		this.body.getPosition().set(position);
	}
	
	public PlayerState getPlayerState(){
		return new PlayerState(this.getPosition(),userID);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player){
			return this.userID == ((Player)obj).getUserID();
		} else {
			return super.equals(obj);
		}
	}

	
	
}
