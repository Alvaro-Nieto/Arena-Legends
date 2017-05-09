package es.alvaronieto.pfcdam.gameobjects;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import es.alvaronieto.pfcdam.Screens.PlayScreen;
import es.alvaronieto.pfcdam.Screens.ScreenManager;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.Resources;

public class Player {
	
	private Body body;
	private long userID;
	private Sprite sprite;
	private TextureRegion truenoStand;
	private String pj;
	
	public Player(World world, Vector2 position, long userID, String pj){
		
		this.userID = userID;
		this.pj = pj;
		BodyDef bdef = new BodyDef();	
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		bdef.type = BodyDef.BodyType.DynamicBody; 
		bdef.position.set(position); // Desde el centro (Coordenadas b2d)
		
		shape.setAsBox(16 / Constants.PPM, 16 / Constants.PPM);
		fdef.shape = shape;
		
		body = world.createBody(bdef);
		body.createFixture(fdef);	
		body.setLinearDamping(10f);
		
		TextureAtlas atlas = Resources.getInstance().getAtlas();
		this.sprite = new Sprite(atlas.findRegion(pj+"stand"));
		truenoStand = new TextureRegion(atlas.findRegion(pj+"stand"));
		sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
		sprite.setRegion(truenoStand);
		
		
	}
	
	public Player(World world, PlayerState playerState){
		this(world, playerState.getPosition(), playerState.getUserID(), playerState.getPj());
	}
	
	public void update(float delta){
		if(sprite != null){
			sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
					   body.getPosition().y - sprite.getHeight() / 2);
		}
		
	}
	
	public void draw(Batch batch){
		if(sprite != null)
			sprite.draw(batch);
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
		return new PlayerState(this.getPosition(),userID, this.getPj());
	}

	
	public String getPj() {
		return pj;
	}

	public void setPj(String pj) {
		this.pj = pj;
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
