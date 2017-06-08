package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.FIROG;

import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.Resources;

public class Player implements Disposable {
	
	private enum State {
		STANDING_DOWN, STANDING_LEFT, STANDING_UP, STANDING_RIGHT, 
		RUNNING_DOWN, RUNNING_LEFT, RUNNING_UP, RUNNING_RIGHT
	};
	
	private State currentState;
	private State previousState;
	
	private Animation<TextureRegion> downAnim;
	private Animation<TextureRegion> upAnim;
	private Animation<TextureRegion> rightAnim;
	
	private Body body;
	private long userID;
	private Sprite sprite;
	private String pj;
	private Vector2 position;
	
	private float stateTimer;
	private TextureAtlas atlas;
	
	private int health;
	private int maxHealth;
	private int team;
	
	public Player(Game game, Vector2 position, long userID, String pj, int team){
		this.position = position;
		this.userID = userID;
		this.pj = pj;
		this.team = team;
		
		defineByPj(pj);
		
		this.setBody(position, game.getWorld());
		
		currentState = State.STANDING_DOWN;
		previousState = currentState;
		
		atlas = Resources.getInstance().getPjAtlas(pj);
		this.sprite = new Sprite(atlas.findRegion(pj+"standdown"));
		
		sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
		sprite.setRegion(new TextureRegion(atlas.findRegion(pj+"standdown")));
		prepareAnimations();
		game.addPlayer(this);
		
	}
	
	private void prepareAnimations() {
		float frameDuration = 4f * 0.05f;
		Array<TextureRegion> frames = new Array<>();
		
		frames.add(atlas.findRegion(pj+"standdown"));
		frames.add(atlas.findRegion(pj+"walkdown1"));
		frames.add(atlas.findRegion(pj+"standdown"));
		frames.add(atlas.findRegion(pj+"walkdown2"));
		downAnim = new Animation<>(frameDuration, frames);
		frames.clear();
		
		frames.add(atlas.findRegion(pj+"standup"));
		frames.add(atlas.findRegion(pj+"walkup1"));
		frames.add(atlas.findRegion(pj+"standup"));
		frames.add(atlas.findRegion(pj+"walkup2"));
		upAnim = new Animation<>(frameDuration, frames);
		frames.clear();
		
		frames.add(atlas.findRegion(pj+"standright"));
		frames.add(atlas.findRegion(pj+"walkright1"));
		frames.add(atlas.findRegion(pj+"standright"));
		frames.add(atlas.findRegion(pj+"walkright2"));
		rightAnim = new Animation<>(frameDuration, frames);
		frames.clear();
	}

	private void setCurrentFrame(float dt){
		setCurrentState();
		
		switch(currentState){
			case STANDING_DOWN:
				sprite.setRegion(atlas.findRegion(pj+"standdown"));
				break;
			case STANDING_UP:
				sprite.setRegion(atlas.findRegion(pj+"standup"));
				break;
			case STANDING_RIGHT:
				sprite.setRegion(atlas.findRegion(pj+"standright"));
				break;
			case STANDING_LEFT:
				sprite.setRegion(atlas.findRegion(pj+"standright"));
				sprite.flip(true, false);
				break;
			case RUNNING_DOWN:
				sprite.setRegion(downAnim.getKeyFrame(stateTimer, true));
				break;
			case RUNNING_UP:
				sprite.setRegion(upAnim.getKeyFrame(stateTimer, true));
				break;
			case RUNNING_RIGHT:
				sprite.setRegion(rightAnim.getKeyFrame(stateTimer, true));
				break;
			case RUNNING_LEFT:
				sprite.setRegion(rightAnim.getKeyFrame(stateTimer, true));
				sprite.flip(true, false);
				break;
		}
		
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
	}
	
	private void setCurrentState() {
		Vector2 velocity = body.getLinearVelocity();
		if(velocity.x == 0 && velocity.y == 0){
			if(previousState == State.RUNNING_DOWN)
				currentState = State.STANDING_DOWN;
			
			else if(previousState == State.RUNNING_LEFT)
				currentState = State.STANDING_LEFT;

			else if(previousState == State.RUNNING_UP)
				currentState = State.STANDING_UP;
			
			else if(previousState == State.RUNNING_RIGHT)
				currentState = State.STANDING_RIGHT;
		} 
		else if( Math.abs(velocity.x) > Math.abs(velocity.y)){
			if(velocity.x < 0)
				currentState = State.RUNNING_LEFT;
			else
				currentState = State.RUNNING_RIGHT;
		}
		else {
			if(velocity.y < 0)
				currentState = State.RUNNING_DOWN;
			else
				currentState = State.RUNNING_UP;
		}
	}
	
	private void defineByPj(String pj) {
		switch(pj){
			case FIROG:
				maxHealth = 80;
				break;
			default: maxHealth = 100;
		}
		health = maxHealth;
	}

	public Player(Game game, Vector2 position, long userID, String pj, Vector2 velocity, int team){
		this(game, position, userID, pj, team);
		this.body.setLinearVelocity(velocity);
	}
	
	public Player(PlayerState playerState, Game game){
		this(game, 
			playerState.getBodyPosition(), 
			playerState.getUserID(), 
			playerState.getPj(), 
			playerState.getVelocity(),
			playerState.getTeam());
	}
	
	public void update(float dt){
		this.setPosition(getBodyPosition());
		setCurrentFrame(dt);
	}
	
	public void setBody(Vector2 position, World world) {
		BodyDef bdef = new BodyDef();	
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		bdef.type = BodyDef.BodyType.DynamicBody; 
		bdef.position.set(position); // Desde el centro (Coordenadas b2d)
		
		shape.setAsBox(16 / Constants.PPM, 16 / Constants.PPM);
		fdef.shape = shape;
		
		body = world.createBody(bdef);
		body.setUserData("PLAYER"+userID);
		body.createFixture(fdef);	
		body.setLinearDamping(10f);
		
		shape.dispose();
	}
	
	public void setBody(PlayerState playerState, World world){
		this.setBody(playerState.getBodyPosition(), world);
		this.body.setLinearVelocity(playerState.getVelocity());
	}

	public void draw(Batch batch){
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
	
	public Vector2 getBodyPosition() {
		return body.getPosition();
	}
	
	public void setBodyPosition(Vector2 position) {
		this.body.getPosition().set(position);
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position.set(position.x - sprite.getWidth() / 2,
				   position.y - sprite.getHeight() / 2);
		sprite.setPosition(this.position.x , this.position.y);
	}

	public PlayerState getPlayerState(){
		return new PlayerState(this.getBodyPosition(), userID, this.getPj(), body.getLinearVelocity(), team);
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

	@Override
	public void dispose() {
		//sprite.getTexture().dispose();
		//truenoStand.getTexture().dispose();
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public Sprite getSprite(){
		return this.sprite;
	}

	public int getHealth() {
		return this.health;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
}
