package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.Disposable;

import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.Resources;

public class Player implements Disposable {
	
	private Body body;
	private long userID;
	private Sprite sprite;
	private TextureRegion[] stand;
	private String pj;
	private Vector2 position;
	private Animation animation;
	private float enlapsedTime;
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
		
		atlas = Resources.getInstance().getTruemoAtlas();
		this.sprite = new Sprite(atlas.findRegion(pj+"standdown"));
		
		stand = new TextureRegion[1];
		stand[0] = new TextureRegion(atlas.findRegion(pj+"standrigth"));
		sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
		sprite.setRegion(stand[0]);
		animation = new Animation(1f/4f, stand);
		game.addPlayer(this);
	}
	private void movimientos(){
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standdown"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkdown1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standdown"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkdown2"));
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standup"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkup1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standup"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkup2"));
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standrigth"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkrigth1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standrigth"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkrigth2"));
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standrigth"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkrigth1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standrigth"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkrigth2"));
			stand[0].flip(true, false);
			stand[1].flip(true, false);
			stand[2].flip(true, false);
			stand[3].flip(true, false);
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standup"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkup1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standup"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkup2"));
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}else if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standup"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkup1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standup"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkup2"));
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standdown"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkdown1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standdown"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkdown2"));
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			stand = new TextureRegion[4];
			stand[0] = new TextureRegion(atlas.findRegion(pj+"standdown"));
			stand[1] = new TextureRegion(atlas.findRegion(pj+"walkdown1"));
			stand[2] = new TextureRegion(atlas.findRegion(pj+"standdown"));
			stand[3] = new TextureRegion(atlas.findRegion(pj+"walkdown2"));
			sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
			sprite.setRegion(stand[1]);
			animation = new Animation(1f/4f, stand);
		}else if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			System.out.println(pj);
			if(pj.equals("truemo")){
				if(Gdx.input.isKeyJustPressed(20)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"fistdown"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(19)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"fistup"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(21)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"fistrigth"));
					stand[0].flip(true, false);
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}if(Gdx.input.isKeyJustPressed(22)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"fistrigth"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
			}else if(pj.equals("firog")){
				if(Gdx.input.isKeyJustPressed(20)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuadown"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(19)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuaup"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(21)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuarigth"));
					stand[0].flip(true, false);
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}if(Gdx.input.isKeyJustPressed(22)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuarigth"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
			}
		}else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
			if(pj.equals("truemo")){
				if(Gdx.input.isKeyJustPressed(20)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"stormdown"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(19)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"stormup"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(21)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"stormrigth"));
					stand[0].flip(true, false);
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}if(Gdx.input.isKeyJustPressed(22)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"stormrigth"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
			}else if(pj.equals("firog")){
				if(Gdx.input.isKeyJustPressed(20)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuadown"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(19)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuaup"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
				if(Gdx.input.isKeyJustPressed(21)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuarigth"));
					stand[0].flip(true, false);
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}if(Gdx.input.isKeyJustPressed(22)){
					stand = new TextureRegion[1];
					stand[0] = new TextureRegion(atlas.findRegion(pj+"ascuarigth"));
					sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
					sprite.setRegion(stand[0]);
					animation = new Animation(1f/4f, stand);
				}
			}
		}else{
			if(Gdx.input.isKeyJustPressed(20)){
				stand = new TextureRegion[1];
				stand[0] = new TextureRegion(atlas.findRegion(pj+"standdown"));
				sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
				sprite.setRegion(stand[0]);
				animation = new Animation(1f/4f, stand);
			}
			if(Gdx.input.isKeyJustPressed(19)){
				stand = new TextureRegion[1];
				stand[0] = new TextureRegion(atlas.findRegion(pj+"standup"));
				sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
				sprite.setRegion(stand[0]);
				animation = new Animation(1f/4f, stand);
			}
			if(Gdx.input.isKeyJustPressed(21)){
				stand = new TextureRegion[1];
				stand[0] = new TextureRegion(atlas.findRegion(pj+"standrigth"));
				stand[0].flip(true, false);
				sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
				sprite.setRegion(stand[0]);
				animation = new Animation(1f/4f, stand);
			}if(Gdx.input.isKeyJustPressed(22)){
				stand = new TextureRegion[1];
				stand[0] = new TextureRegion(atlas.findRegion(pj+"standrigth"));
				sprite.setBounds(0, 0, 32 / Constants.PPM, 32 / Constants.PPM);
				sprite.setRegion(stand[0]);
				animation = new Animation(1f/4f, stand);
			}
		}
	}
	
	private void defineByPj(String pj) {
		switch(pj){
			case TRUEMO: 
				maxHealth = 100;
				health = maxHealth;
				break;
			case FIROG:
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
		enlapsedTime+=Gdx.graphics.getDeltaTime();
		movimientos();
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
			//sprite.draw(batch);
			batch.draw((TextureRegion)animation.getKeyFrame(enlapsedTime, true), position.x, position.y, 2, 2,
					sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), 0);
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
