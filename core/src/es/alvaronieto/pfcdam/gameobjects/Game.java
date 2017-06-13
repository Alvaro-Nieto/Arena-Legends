package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerSlot;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.States.ProjectileState;
import es.alvaronieto.pfcdam.Util.BodyData;
import es.alvaronieto.pfcdam.Util.CountDownTimer;
import es.alvaronieto.pfcdam.Util.ProjectileContact;

public class Game implements Disposable {
	
	private HashMap<Long, Player> players;
	private HashMap<Long, ConcurrentHashMap<Long, Projectile>> projectiles;
	private World world;
	private GameRules gameRules;
	private Arena arena;
	private CountDownTimer timer;
	

	public Game(GameRules gameRules){
		this.gameRules = gameRules;
		this.world = new World(Vector2.Zero, true);
		this.projectiles = new HashMap<Long, ConcurrentHashMap<Long, Projectile>>();
		this.players = new HashMap<Long, Player>();
		this.arena = new Arena(gameRules.getArenaPath(), world);
		this.timer = new CountDownTimer(gameRules.getGameLengthMinutes(),
										gameRules.getGameLengthSeconds());
	}

	public void activeContactListener() {
		this.world.setContactListener(new ContactListener(){
			@Override
			public void beginContact(Contact contact) {
				Body ba = contact.getFixtureA().getBody();
				Body bb = contact.getFixtureB().getBody();
				BodyData bdataA = (BodyData)ba.getUserData();
				BodyData bdataB = (BodyData)bb.getUserData();
				if(isProjectile(bdataB.getType()) && isPlayer(bdataA.getType()) &&
						bdataB.getTeam() != bdataA.getTeam()){
					
					Projectile projectile = projectiles.get(bdataB.getUserID()).get(bdataB.getSeqNo());
					Player player = getPlayer(bdataA.getUserID());
					
					if(!projectile.isDisposed()) {
						player.hurt(10);
						projectile.dispose();
					}
					
				}
		
			}
			private boolean isPlayer(String type) {
				return type.equals("player");
			}
			private boolean isProjectile(String type) {
				return type.equals("projectile");
			}
			@Override
			public void endContact(Contact contact) {}
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});
	}
	
	public Game(GameState gameState){
		this(gameState.getGameRules());
		HashMap<Long, PlayerState> playerStates = gameState.getPlayers();
		for(Long userID : playerStates.keySet()){
			this.players.put(userID, new Player(playerStates.get(userID), this));
		}
	}
	
	public Game(LobbyState lobbyState) {
		this(lobbyState.getGameRules());
		HashMap<Long, PlayerSlot> playerSlots = lobbyState.getPlayerSlots();
		for(Long userID : playerSlots.keySet()){
			// TODO Crear metodo para calcular la posici�n de comienzo
			Vector2 position = new Vector2(getMapWidth() / 2, getMapHeight() / 2);
			PlayerSlot slot = playerSlots.get(userID);
			this.players.put(userID, new Player(this, position, userID, slot.getPj(), slot.getTeam(), slot.getPlayerName()));
		}
	}

	public void addPlayer(Player player){
		players.put(player.getUserID(), player);
	}
	
	public boolean removePlayer(long playerID){
		return players.remove(playerID) != null;
	}
	
	public Player getPlayer(long userID){
		return players.get(userID);
	}

	public HashMap<Long, Player> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<Long, Player> players) {
		this.players = players;
	}
	
	public GameState getGameState(){
		return new GameState(players, projectiles, gameRules);
	}
	
	public void resetWorld(GameState gameState){
		world = new World(Vector2.Zero, true);
		HashMap<Long, PlayerState> playerStates = gameState.getPlayers();
		for(Long userID : playerStates.keySet()){
			if(players.containsKey(userID))
				players.get(userID).setBody(playerStates.get(userID), world);
			else
				players.put(userID, new Player(playerStates.get(userID), this));
		}
		projectiles = new HashMap<>();
	}

	@Override
	public void dispose() {
		for(Long userID : players.keySet()){
			players.get(userID).dispose();
		}
		world.dispose();
		arena.dispose();
	}
	
	public void updateWorld(GameState gameState){
		destroyBodies();
		
		HashMap<Long, PlayerState> playerStates = gameState.getPlayers();
		for(Long userID : playerStates.keySet()){
			if(players.containsKey(userID)) {
				Player player = players.get(userID);
				player.updateState(playerStates.get(userID), world);
				
			}
			else
				players.put(userID, new Player(playerStates.get(userID), this));
		}
		
		HashMap<Long, HashMap<Long, ProjectileState>> projectileStates = gameState.getProjectileStates();
		for(Long userID : projectileStates.keySet()){
			for(Long seqNo : projectileStates.get(userID).keySet()){
				ProjectileState state = projectileStates.get(userID).get(seqNo);
				if(projectiles.containsKey(userID) && projectiles.get(userID).containsKey(seqNo))
					projectiles.get(userID).get(seqNo).setBody(state.getBodyPosition(), state.getVelocity());
				else {
					addProjectile(new Projectile(this, state));
				}
			}
		}
	}
	
	private void addProjectile(Projectile projectile){
		long userID = projectile.getUserID();
		long seqNo = projectile.getSeqNo();
		if(!projectiles.containsKey(userID))
			projectiles.put(userID, new ConcurrentHashMap<Long, Projectile>());
		projectiles.get(userID).put(seqNo, projectile);
	}
	
	public void newProjectile(Vector2 dir, String type, long userID){
		long seqNo = players.get(userID).newLastSeqNoAttack1() + 1;
		Projectile projectile = new Projectile(this, dir, type, userID, seqNo);
		addProjectile(projectile);
	}

	public World getWorld() {
		return world;
	}

	private void destroyBodies() {
		for (Map.Entry<Long, Player> entry : players.entrySet()) {
			Body body = entry.getValue().getBody();
			if(body.isActive()){
				this.world.destroyBody(body);
			}
		}
		for(Long userID : projectiles.keySet()){
			for(Long seqNo : projectiles.get(userID).keySet()){
				Body body = projectiles.get(userID).get(seqNo).getBody();
				if(body.isActive()){
					this.world.destroyBody(body);
				}
			}
		}
	}
	
	public void step(){
		world.step(STEP, 6, 2);
	}
	
	public void start(){
		timer.start();
	}
	
	public void update(float dt){
		
		for(Long userID : players.keySet()){
			Player player = players.get(userID);
			player.update(dt);
		}
		
		for(Long userID : projectiles.keySet()){
			for(Long seqNo : projectiles.get(userID).keySet()){
				Projectile projectile = projectiles.get(userID).get(seqNo);
				projectile.update(dt);
				if(projectile.isDisposed())
					this.removeProjectile(projectile);
			}
		}
		
		timer.update();
		
	}
	
	public float getMapWidth(){
		return arena.getMapWidth();
	}
	
	public float getMapHeight(){
		return arena.getMapHeight();
	}

	public OrthogonalTiledMapRenderer getMapRenderer() {
		return arena.getRenderer();
	}
	
	public void updateLiquidAnimations(){
		arena.updateCells();
	}

	public CountDownTimer getTimer() {
		return this.timer;
	}


	public void draw(Batch batch) {
		drawAllPlayers(batch);
		drawProjectiles(batch);
	}
	
	private void drawProjectiles(Batch batch) {
		for(Long userID : projectiles.keySet()){
			for(Long seqNo : projectiles.get(userID).keySet()){
				projectiles.get(userID).get(seqNo).draw(batch);
			}
		}
	}

	private void drawAllPlayers(Batch batch) {
        for(Long userID : players.keySet()){
			players.get(userID).draw(batch);
		}
	}

	public void removeProjectile(Projectile projectile) {
		world.destroyBody(projectile.getBody());
		projectiles.get(projectile.getUserID()).remove(projectile.getSeqNo());
	}

}
