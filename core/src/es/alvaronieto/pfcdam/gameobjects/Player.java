package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.FIROG;
import static es.alvaronieto.pfcdam.Util.Constants.VENETO;

import com.badlogic.gdx.Gdx;
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
import es.alvaronieto.pfcdam.Util.BodyData;
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
	
	private long lastSeqNoAttack1;
	
	private String playerName;
	private Game game;
	
	private float skill1CD = 0.5f;
	private float timeSinceSkill1 = skill1CD+1;
	private boolean dead = false;
	
	public Player(Game game, Vector2 position, long userID, String pj, int team, String playerName){
		this.position = position;
		this.userID = userID;
		this.pj = pj;
		this.team = team;
		this.lastSeqNoAttack1 = 0;
		this.playerName = playerName;
		this.game = game;
		
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
	
	public Player(Game game, Vector2 position, long userID, String pj, Vector2 velocity, int team, String playerName){
		this(game, position, userID, pj, team, playerName);
		this.body.setLinearVelocity(velocity);
	}
	
	public Player(PlayerState playerState, Game game){
		this(game, 
			playerState.getBodyPosition(), 
			playerState.getUserID(), 
			playerState.getPj(), 
			playerState.getVelocity(),
			playerState.getTeam(),
			playerState.getPlayerName());
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
		
		Vector2 v = body.getLinearVelocity();
		float sp = Math.abs(v.x) + Math.abs(v.y);
		
		stateTimer = currentState == previousState ? stateTimer + dt * sp : 0;
		previousState = currentState;
	}
	
	private void setCurrentState() {
		Vector2 velocity = body.getLinearVelocity();
		if(Math.abs(velocity.x) <= 0.20f && Math.abs(velocity.y) <= 0.20f){
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
			case VENETO:
				maxHealth = 90;
				break;
			default: maxHealth = 100;
		}
		health = maxHealth;
	}

	
	public void update(float dt){
		if(health <= 0) {
			dead = true;
			//if(body.isActive())
				//game.getWorld().destroyBody(body);
		}
		if(!dead){
			this.setPosition(getBodyPosition());
			setCurrentFrame(dt);
			timeSinceSkill1 += dt;
			
		}
		
	}
	
	public void setBody(Vector2 position, World world) {
		if(!dead) {
			BodyDef bdef = new BodyDef();	
			PolygonShape shape = new PolygonShape();
			FixtureDef fdef = new FixtureDef();
			
			bdef.type = BodyDef.BodyType.DynamicBody; 
			bdef.position.set(position); // Desde el centro (Coordenadas b2d)
			
			shape.setAsBox(16 / Constants.PPM, 16 / Constants.PPM);
			fdef.shape = shape;
			
			body = world.createBody(bdef);
			body.setUserData(new BodyData(userID, "player", team));
			body.createFixture(fdef);	
			body.setLinearDamping(10f);
			
			shape.dispose();
		}
		
	}
	
	public void setBody(PlayerState playerState, World world){
		if(!dead) {
			this.setBody(playerState.getBodyPosition(), world);
			this.body.setLinearVelocity(playerState.getVelocity());
		}
		
	}

	public void draw(Batch batch){
		if(!dead)
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
		return new PlayerState(this.getBodyPosition(), userID, this.getPj(), body.getLinearVelocity(), team, playerName, health);
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
		sprite.getTexture().dispose();
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

	public int getTeam() {
		return team;
	}

	public long newLastSeqNoAttack1() {
		lastSeqNoAttack1++;
		return lastSeqNoAttack1;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void hurt(int hp) {
		health -= hp;
	}
	
	public void attack1(Vector2 dir){
		if(!dead) {
			if(skill1CD < timeSinceSkill1){
				game.newProjectile(dir, pj, userID);
				timeSinceSkill1 = 0;
			}
		}
		
	}
	
	public void setHealth(int health) {
		this.health = health;
	}

	public void updateState(PlayerState playerState, World world) {
		if(!dead)
			this.setBody(playerState, world);
		this.setHealth(playerState.getHealth());
	}

	public boolean isDead() {
		return dead ;
	}
}
