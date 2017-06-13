package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.*;
import static es.alvaronieto.pfcdam.Util.Constants.TRUEMOBALL;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import es.alvaronieto.pfcdam.States.ProjectileState;
import es.alvaronieto.pfcdam.Util.BodyData;
import es.alvaronieto.pfcdam.Util.Resources;

public class Projectile {
	
	private Body body;
	private Sprite sprite;
	private float timeSinceStart = 0f;
	private float maxTimeAlive = 2f;
	private World world;
	private Game game;
	private boolean disposed = false;
	private boolean shouldDispose = false;
	private long userID;
	private String type;
	
	private float velocity = 5.0f;
	private int team;
	
	private long seqNo;
	
	public Projectile(Game game, ProjectileState state){
		this.game = game;
		this.world = game.getWorld();
		this.seqNo = state.getSeqNo();
		this.userID = state.getUserID();
		BodyDef bdef = new BodyDef();	
		CircleShape shape = new CircleShape();
		FixtureDef fdef = new FixtureDef();
		
		this.type = state.getType();
		this.team = state.getTeam();
		
		bdef.type = BodyDef.BodyType.KinematicBody; 
		bdef.position.set(state.getBodyPosition());
		
		shape.setRadius(16f/PPM);
		fdef.shape = shape;
		
		body = world.createBody(bdef);
		body.setUserData(new BodyData(userID, "projectile", team, seqNo));
		body.createFixture(fdef);	
		
		body.setLinearVelocity(state.getVelocity());
		
		TextureAtlas atlas = getAtlas();
		this.sprite = new Sprite(atlas.findRegion("projectile1"));
		sprite.setBounds(0, 0, 32 / PPM, 32 / PPM);	
	}
	
	public Projectile(Game game, Vector2 dir, String type, long userID, long seqNo){
		this.world = game.getWorld();
		this.seqNo = seqNo;
		this.userID = userID;
		BodyDef bdef = new BodyDef();	
		CircleShape shape = new CircleShape();
		FixtureDef fdef = new FixtureDef();
		
		Vector2 position = game.getPlayer(userID).getBodyPosition();
		
		this.type = type;
		this.team = game.getPlayer(userID).getTeam();
		
		bdef.type = BodyDef.BodyType.KinematicBody; 
		bdef.position.set(new Vector2(position.x+dir.x*40/PPM, position.y+dir.y*40/PPM));
		
		shape.setRadius(16f/PPM);
		fdef.shape = shape;
		
		body = world.createBody(bdef);
		body.setUserData(new BodyData(userID, "projectile", team, seqNo));
		body.createFixture(fdef);	
		
		body.setLinearVelocity(dir.scl(velocity));
		
		TextureAtlas atlas = getAtlas();
		this.sprite = new Sprite(atlas.findRegion("projectile1"));
		sprite.setBounds(0, 0, 32 / PPM, 32 / PPM);	
		
		shape.dispose();
	}
	

	private TextureAtlas getAtlas() {
		TextureAtlas atlas = null;
		switch(type){
		case VENETO:
			atlas = Resources.getInstance().getVenetoAtlas();
			break;
		case TRUEMO:
			atlas = Resources.getInstance().getTruemoAtlas();
			break;
		case FIROG:
			atlas = Resources.getInstance().getFirogAtlas();
			break;
		}
		return atlas;
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
	
	public void dispose() {
		//game.removeProjectile(this);
		disposed = true;
	}

	public void draw(Batch batch){
		if(!disposed)
			sprite.draw(batch);
	}

	public void disposeNextUpdate() {
		this.shouldDispose = true;
	}

	public long getUserID() {
		return userID;
	}

	public String getType() {
		return type;
	}

	public int getTeam() {
		return team;
	}

	public long getSeqNo() {
		return seqNo;
	}

	public ProjectileState getProjectileState() {
		return new ProjectileState(body.getPosition(), userID, body.getLinearVelocity(), team, type, seqNo);
	}
	
	public void setBody(Vector2 position, Vector2 velocity){
		if(!disposed){
			BodyDef bdef = new BodyDef();	
			CircleShape shape = new CircleShape();
			FixtureDef fdef = new FixtureDef();
			
			bdef.type = BodyDef.BodyType.KinematicBody; 
			bdef.position.set(position);
			
			shape.setRadius(16f/PPM);
			fdef.shape = shape;
			
			body = world.createBody(bdef);
			body.setUserData(new BodyData(userID, "projectile", team, seqNo));
			body.createFixture(fdef);	
			
			body.setLinearVelocity(velocity);

			shape.dispose();
		}
	}

	public boolean isDisposed() {
		return disposed;
	}

	public boolean shouldDispose() {
		return shouldDispose;
	}
	
}
