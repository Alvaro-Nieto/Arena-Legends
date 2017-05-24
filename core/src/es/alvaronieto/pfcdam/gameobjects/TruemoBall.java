package es.alvaronieto.pfcdam.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import es.alvaronieto.pfcdam.Util.Resources;

import static es.alvaronieto.pfcdam.Util.Constants.PPM;
import static es.alvaronieto.pfcdam.Util.Constants.TRUEMOBALL;

public class TruemoBall {
	
	private Body body;
	private Sprite sprite;
	private float timeSinceStart = 0f;
	private float maxTimeAlive = 1f;
	private World world;
	private boolean disposed = false;
	private boolean shouldDispose = false;
	
	public TruemoBall(World world, Vector2 position){
			this.world = world;
			BodyDef bdef = new BodyDef();	
			CircleShape shape = new CircleShape();
			FixtureDef fdef = new FixtureDef();
			
			bdef.type = BodyDef.BodyType.DynamicBody; 
			bdef.position.set(position); // Desde el centro (Coordenadas b2d)
			
			shape.setRadius(16f/PPM);
			fdef.shape = shape;
			
			body = world.createBody(bdef);
			body.setUserData(TRUEMOBALL);
			body.createFixture(fdef);	
			
			

			TextureAtlas atlas = Resources.getInstance().getAtlas();
			this.sprite = new Sprite(atlas.findRegion(TRUEMOBALL+"1"));
			sprite.setBounds(0, 0, 32 / PPM, 32 / PPM);	
		}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public void update(float delta){
		if(!disposed){
			timeSinceStart += delta;
			sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
					   body.getPosition().y - sprite.getHeight() / 2);
			if(timeSinceStart > maxTimeAlive || shouldDispose )
				this.dispose();
		}
		
	
	}
	
	private void dispose() {
		world.destroyBody(body);
		disposed  = true;
	}

	public void draw(Batch batch){
		if(!disposed)
			sprite.draw(batch);
	}

	public void disposeNextUpdate() {
		this.shouldDispose = true;
	}
	
	
	
}
