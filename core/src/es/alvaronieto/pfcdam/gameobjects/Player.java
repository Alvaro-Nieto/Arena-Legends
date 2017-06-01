package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.*;

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
import com.badlogic.gdx.utils.Disposable;

import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.Resources;

public class Player implements Disposable {
	
	private Body body;
	private long userID;
	private Sprite sprite;
	private TextureRegion truenoStand;
	private String pj;
	private Vector2 position;
	
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
		
		TextureAtlas atlas = Resources.getInstance().getTruemoAtlas();
		this.sprite = new Sprite(atlas.findRegion(pj+"stand"));
		truenoStand = new TextureRegion(atlas.findRegion(pj+"stand"));
		sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
		sprite.setRegion(truenoStand);
		game.addPlayer(this);
	}
	
	private void defineByPj(String pj) {
		switch(pj){
			case TRUEMO: 
				maxHealth = 100;
				health = maxHealth;
				break;
			default: break;
		}
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
	
	public void update(){
		this.setPosition(getBodyPosition());
	}
	
	public void update(Vector2 position){
		this.setPosition(position);
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
		//if(sprite != null)
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
