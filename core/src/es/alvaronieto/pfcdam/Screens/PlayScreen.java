package es.alvaronieto.pfcdam.Screens;

import static es.alvaronieto.pfcdam.Util.Constants.*;

import java.awt.SecondaryLoop;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Input.InputManager;
import es.alvaronieto.pfcdam.Scenes.DebugHud;
import es.alvaronieto.pfcdam.Scenes.PauseHud;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.gameobjects.Arena;
import es.alvaronieto.pfcdam.gameobjects.Game;
import es.alvaronieto.pfcdam.gameobjects.Player;
import es.alvaronieto.pfcdam.gameobjects.TruemoBall;
import es.alvaronieto.pfcdam.net.kryoserver.TestServer;

import static es.alvaronieto.pfcdam.Util.Constants.PPM;

public class PlayScreen implements Screen {

	private ScreenManager screenManager;
	private Juego juego;
	
	// Camera
	private OrthographicCamera gamecam;
    private Viewport gamePort;
    
    // Arena
    private Arena arena;
    
    // Box2D
    private World world;
    private Box2DDebugRenderer b2dr;
    
    // Hud
    private DebugHud debugHud;
	private boolean freeCameraEnabled = false;
	
	// Pause Hud
	private PauseHud pauseHud;
	
	// Player
	private Player player;
	
	// Testing Server
	private TestServer server;
	private Game game;
	
	private GameState lastSnapshot;
	private long lastSnapshotTime;
	
	private List<InputState> pendingInputs;
	private int inputSequenceNo = 0;
	private long snapSequenceNumber;
	private List<TruemoBall> balls;
	
	private double accumulator;
	private long currentTick;
	private boolean interpolating = false;
	
	// Used to render debugging stuff
	private ShapeRenderer sr;
	
	private float skill1CD = 0.5f;
	private float timeSinceSkill1 = skill1CD+1;
	
	private long timeLast = 0;
	
	public PlayScreen(ScreenManager screenManager, PlayerState playerState, GameState gameState) {
		this.screenManager = screenManager;
        this.juego = screenManager.getJuego();
        
        this.balls = new ArrayList<TruemoBall>();
        this.currentTick = 0;
        // SET CAMERA
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Gdx.graphics.getWidth() / PPM,Gdx.graphics.getHeight() / PPM, gamecam);
        
        // Box2D
        world = new World(Vector2.Zero, true);
        b2dr = new Box2DDebugRenderer();
        createCollisionListener();
        
        // LOAD TILED MAP
        loadMap();
	
        // Player
        player = new Player(playerState, world);
        
        // Hud
        debugHud = new DebugHud(juego.batch, player);
        
        // Pause Hud 
        pauseHud = new PauseHud(juego.batch);
        
        // Testing Server
        game = new Game(world, gameState);
        game.addPlayer(player);

        // Snapshots Stuff
        pendingInputs = new ArrayList<InputState>();
        
        accumulator = 0;
        sr = new ShapeRenderer();
        

        sr.setAutoShapeType(true);
	}
	
	private void loadMap() {
		this.arena = new Arena(ARENA_LAVA, world);
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2,0);
	}
	
	private void createCollisionListener() {
		// TODO some testing. this is temp
        world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                //TEMP
                if(fixtureA.getBody().getUserData().equals(TRUEMOBALL)){
                	for(TruemoBall ball : balls){
                		if(ball.getBody().equals(fixtureA.getBody())){
                			ball.disposeNextUpdate();
                		}
                	}
                } else if(fixtureB.getBody().getUserData().equals(TRUEMOBALL)){
                	for(TruemoBall ball : balls){
                		if(ball.getBody().equals(fixtureB.getBody())){
                			ball.disposeNextUpdate();
                		}
                	}
                }
			}

			@Override
			public void endContact(Contact contact) {
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}



        });
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	public void update(float dt) {
		timeSinceSkill1 += dt;
		accumulator += dt;
		while(accumulator >= STEP) {
			tick(dt);
			accumulator -= STEP;
		} 
		
		updateAllPlayers(dt);
		updateAllBalls(dt);
		debugHud.update(dt);
		
		if(!freeCameraEnabled){
			camFollowPlayer();
		}
		
		gamecam.update();
		arena.getRenderer().setView(gamecam);
		//System.out.println("UPDATE:"+player.getPosition());
	}

	private void tick(float dt) {
		handleInput(dt);
		
		//sendInput()
		
		
		// Pruebas snapshots
		if(lastSnapshot!=null){
			newSnapshot();
		}
		
		//System.out.println("C >> "+currentTick);
		currentTick++;
		//world.step(STEP, 6, 2);
		//System.out.println("["+snapSequenceNumber+"]"+"STEP:"+player.getPosition());
	}

	private void updateAllBalls(float dt) {
		for(TruemoBall ball : balls)
			ball.update(dt);
	}

	private void updateAllPlayers(float dt) {
		HashMap<Long, Player> players =  game.getPlayers();
        for(Long userID : players.keySet()){
			Player player = players.get(userID);
			if(interpolating ){
				// TODO interpolating
				// Vector2 targetPosition = lastSnapshot.getPlayers().get(userID).getPosition();
				// getInterpolatedPosition(player.getPosition(),targetPosition,time);
			}
			else
				player.update(player.getPosition());
		}
	}

	private void newSnapshot() {
		destroyBodies();
		game.fillWorld(lastSnapshot, world);
		for (Map.Entry<Long, PlayerState> entry : lastSnapshot.getPlayers().entrySet()) {
			
		    long userID = entry.getKey();
		    PlayerState playerState = entry.getValue();
		    Player snapshotPlayer = getGame().getPlayer(userID);
		    
		    if(snapshotPlayer.equals(this.player)){
		    	stateReconciliation(playerState);
		    } else{
		    	// TODO Do interpolation
		    }
		    
		}
	}

	private void destroyBodies() {
		for (Map.Entry<Long, Player> entry : game.getPlayers().entrySet()) {
			Body body = entry.getValue().getBody();
			if(body.isActive()){
				this.world.destroyBody(body);
			}
		}
	}

	private void camFollowPlayer() {
		
		float dt = Gdx.graphics.getDeltaTime();
		gamecam.position.y += (player.getBody().getPosition().y - gamecam.position.y) * 5f * dt;
		gamecam.position.x += (player.getBody().getPosition().x - gamecam.position.x) * 5f * dt;
		
		/*
		gamecam.position.y = player.getBody().getPosition().y;
		if(player.getBody().getPosition().y - gamecam.viewportHeight / 2 < 0) {
		    gamecam.position.y = gamecam.viewportHeight / 2;
		} else if(player.getBody().getPosition().y + gamecam.viewportHeight / 2 > mapHeight){
		    gamecam.position.y = mapHeight - gamecam.viewportHeight / 2;
		}
		
		gamecam.position.x = player.getBody().getPosition().x;
		if(player.getBody().getPosition().x - gamecam.viewportWidth / 2 < 0) {
		    gamecam.position.x = gamecam.viewportWidth / 2;
		} else if(player.getBody().getPosition().x + gamecam.viewportWidth / 2 > mapWidth){
		    gamecam.position.x = mapWidth - gamecam.viewportWidth / 2;
		}*/
	}
	
	private void stateReconciliation(PlayerState playerState) {
		Iterator<InputState> it = pendingInputs.iterator();
		while(it.hasNext()){
			InputState input = it.next();
			if(input.getSequenceNumber() <= snapSequenceNumber){
				it.remove();
			}
			else {
				Body body = game.getPlayer(playerState.getUserID()).getBody();
				InputManager.applyInputToPlayer(input, player);
				//if(it.hasNext())
					world.step(STEP, 6, 2);
			}
		}
		
	}

	
	private void handleInput(float dt) {
		if(freeCameraEnabled){
			moveFreeCamera(dt);
		} else {
			inputSequenceNo++;
			// TODO Hay que hacer algo decente
			InputState inputState = new InputState(
					Gdx.input.isKeyPressed(Input.Keys.UP), 
					Gdx.input.isKeyPressed(Input.Keys.DOWN),
					Gdx.input.isKeyPressed(Input.Keys.LEFT),
					Gdx.input.isKeyPressed(Input.Keys.RIGHT), inputSequenceNo);
			
			screenManager.getTestClient().sendInputState(inputState, player.getUserID());
			pendingInputs.add(inputState);

			//  THIS SHOULD BE CLIENT PREDICTION INPUT
			//InputManager.applyInputToPlayer(inputState, player);
			
			// TODO implementar en net
			
			if(Gdx.input.isTouched() && skill1CD < timeSinceSkill1){
				ballTest(dt);
				timeSinceSkill1 = 0;
			}
			
		}
		
		// Debug HUD
		if(Gdx.input.isKeyJustPressed(Input.Keys.F10))
        	debugHud.toggleFPS();
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F11))
        	debugHud.toggleInfoPlayer();
        	
        if(Gdx.input.isKeyJustPressed(Input.Keys.F12))
        	freeCameraEnabled = !freeCameraEnabled;
        
        // Pause HUD
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        	pauseHud.togglePauseMenu();
        
	}

	private void ballTest(float dt) {
		Vector2 position = player.getPosition();
		Vector3 click = gamecam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 dir = new Vector2(click.x - position.x, click.y - position.y);
		dir.nor();
		
		TruemoBall tball = new TruemoBall(world, new Vector2(position.x+dir.x*40/PPM, position.y+dir.y*40/PPM));
		tball.getBody().setLinearVelocity(dir.scl(5f));

		balls.add(tball);
	}

	private void moveFreeCamera(float dt) {
		float mapHeight = arena.getMapHeight();
		float mapWidth = arena.getMapWidth();
		
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			gamecam.position.y += 10 * dt;
			if(gamecam.position.y > mapHeight - gamePort. getWorldHeight()/2)
				gamecam.position.y = mapHeight - gamePort.getWorldHeight()/2;
		}
		    
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			gamecam.position.x += 10 * dt;
			if(gamecam.position.x > mapWidth - gamePort. getWorldWidth()/2)
				gamecam.position.x = mapWidth - gamePort.getWorldWidth()/2;
		}
			
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			gamecam.position.x -= 10 * dt;
			if(gamecam.position.x < 0 + gamePort.getWorldWidth()/2)
				gamecam.position.x = 0 + gamePort.getWorldWidth()/2;
		}
			
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			gamecam.position.y -= 10 * dt;
			if(gamecam.position.y < 0 + gamePort.getWorldHeight()/2)
				gamecam.position.y = 0 + gamePort.getWorldHeight()/2;
		}
	}
	
	
	public void newNetworkPlayer(PlayerState playerState){
		game.addPlayer(new Player(playerState, world));
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        arena.getRenderer().render();
        
        //b2dr.render(world, gamecam.combined);
        
        juego.batch.setProjectionMatrix(gamecam.combined);
        juego.batch.begin();
        drawAllPlayers();
        drawBalls();
        juego.batch.end();
        //drawGhost();
        
    	juego.batch.setProjectionMatrix(debugHud.stage.getCamera().combined);
        debugHud.stage.draw();
        pauseHud.stage.draw();
	}

	/*
	 * Draw the last player position acknowledged by the server.
	 */
	private void drawGhost() {
		if(lastSnapshot != null){
			sr.setProjectionMatrix(gamecam.combined);
        	Vector2 pos = lastSnapshot.getPlayers().get(player.getUserID()).getPosition();
        	sr.begin();
        	sr.setColor(Color.RED);
            sr.rect(pos.x - 16 / PPM, pos.y - 16 / PPM, 32 / PPM, 32 / PPM);
            sr.end();
        }
	}

	private void drawBalls() {
		for(TruemoBall ball : balls){
			ball.draw(juego.batch);
		}		
	}

	private void drawAllPlayers() {
		HashMap<Long, Player> players =  game.getPlayers();
        for(Long userID : players.keySet()){
			players.get(userID).draw(juego.batch);
		}
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		pauseHud.getViewPort().update(width, height);
		debugHud.getViewPort().update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		arena.dispose();
		world.dispose();
		b2dr.dispose();
		debugHud.dispose();
		pauseHud.dispose();
	}

	public Game getGame() {
		return game;
	}

	public void setLastSnapshot(GameState gameState) {
		this.lastSnapshot = gameState;
	}

	public long getLastSnapshotTime() {
		return lastSnapshotTime;
	}

	public void setLastSnapshotTime(long lastSnapshotTime) {
		this.lastSnapshotTime = lastSnapshotTime;
	}

	public void setSnapSequenceNumber(long sequenceNumber) {
		this.snapSequenceNumber = sequenceNumber;
	}

	public long getCurrentTick() {
		return currentTick;
	}
	
}
