package es.alvaronieto.pfcdam.Sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import es.alvaronieto.pfcdam.Juego;

public class Player {
	
	private Body body;


	public Player(World world, float x, float y){
		
		BodyDef bdef = new BodyDef();	
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		bdef.type = BodyDef.BodyType.DynamicBody;
		//bdef.position.set(new Vector2(x + 16 / Juego.PPM , y + 16 / Juego.PPM)); // Desde esquina inferior izquierda (Coordenadas b2d) 
		bdef.position.set(new Vector2(x, y)); // Desde el centro (Coordenadas b2d)
		
		shape.setAsBox(16 / Juego.PPM, 16 / Juego.PPM);
		fdef.shape = shape;
		
		body = world.createBody(bdef);
		body.createFixture(fdef);	
		body.setLinearDamping(10f);
	}
	
	public void update(float delta){
		
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
		return new PlayerState(this.getPosition());
	}


	
}
