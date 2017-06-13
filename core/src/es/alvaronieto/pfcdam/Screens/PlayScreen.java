package es.alvaronieto.pfcdam.Screens;

import static es.alvaronieto.pfcdam.Util.Constants.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

import es.alvaronieto.pfcdam.ArenaLegends;
import es.alvaronieto.pfcdam.Input.InputManager;
import es.alvaronieto.pfcdam.Scenes.DebugHud;
import es.alvaronieto.pfcdam.Scenes.Hud;
import es.alvaronieto.pfcdam.Scenes.PauseMenu;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.Util.Resources;
import es.alvaronieto.pfcdam.Util.SecurityUtility;
import es.alvaronieto.pfcdam.gameobjects.Game;
import es.alvaronieto.pfcdam.gameobjects.Player;
import es.alvaronieto.pfcdam.gameobjects.Projectile;
import es.alvaronieto.pfcdam.sound.SoundManager;

public class PlayScreen implements Screen {

	// Managers / Containers
	private ScreenManager screenManager;
	private ArenaLegends juego;
	
	// Camera
	private OrthographicCamera gamecam;
    private Viewport gamePort;
    
    // Box2D Renderer
    private Box2DDebugRenderer b2dr;
    
	// Scenes
    private DebugHud debugHud;
	private PauseMenu pauseMenu;
	private Hud hud;
	
	// Player
	private Player player;
	
	// Game
	private Game game;

	// Networking stuff
	private GameState lastSnapshot;
	private long lastSnapshotTime;
	private List<InputState> pendingInputs;
	private int inputSequenceNo = 0;
	private long snapSequenceNumber;
	
	// Ticks
	private double accumulator;
	private long currentTick;
	
	// Checks
	private boolean freeCameraEnabled = false;
	private boolean interpolating = false;
	
	// Used to render debugging stuff
	private ShapeRenderer sr;
	

	private boolean debugging = false;
	private boolean drawHud = true;

	private Console console;
	
	public PlayScreen(ScreenManager screenManager, long userID, GameState gameState) {
		this.screenManager = screenManager;
        this.juego = screenManager.getArenaLegends();

        this.currentTick = 0;
        
        // Game
        game = new Game(gameState);
        
        // SET CAMERA
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, gamecam);
        gamecam.position.set(game.getMapWidth() / 2 ,game.getMapHeight() / 2, 0);
        
        b2dr = new Box2DDebugRenderer();

	
        // Player
        player = game.getPlayer(userID);
        
        // Scenes
        debugHud = new DebugHud(juego.batch, player);
        pauseMenu = new PauseMenu(juego.batch);
        hud = new Hud(juego.batch, game, player);

        // Snapshots Stuff
        pendingInputs = new ArrayList<InputState>();
        
        accumulator = 0;
        
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        
        game.start();
        
        console = Resources.getInstance().getConsole();
        console.resetInputProcessing();
        
        SoundManager sm = SoundManager.getInstance();
        sm.stopLobby();
        if(gameState.getGameRules().getArenaPath().equals(ARENA_LAVA))
        	sm.startLava();
        else
        	sm.startWater();
		
	}
	


	public void update(float dt) {
		accumulator += dt;
		if(dt > 0.30f) dt = 0.30f;
		if(!console.isVisible())
			handleInstantInput(dt);
		while(accumulator >= STEP) {
			tick(dt);
			accumulator -= STEP;

		}         
		
		game.update(dt);
		debugHud.update(dt);
		hud.update(100);
		
		if(player.isDead())
			freeCameraEnabled = true;
		if(!freeCameraEnabled){
			camFollowPlayer();
		}
		
		gamecam.update();
		game.getMapRenderer().setView(gamecam);
	}

	private void tick(float dt) {
		if(!console.isVisible())
			handleInput(dt);
		
		if(lastSnapshot!=null){
			applyLastSnapshot();
		}
		
		currentTick++;
	}

	private void applyLastSnapshot() {
		game.updateWorld(lastSnapshot);
		PlayerState playerState = lastSnapshot.getPlayers().get(player.getUserID());
		stateReconciliation(playerState);
	}

	private void camFollowPlayer() {
		
		float dt = Gdx.graphics.getDeltaTime();
		gamecam.position.y += (player.getBody().getPosition().y - gamecam.position.y) * 5f * dt;
		gamecam.position.x += (player.getBody().getPosition().x - gamecam.position.x) * 5f * dt;
		
	}
	
	private void stateReconciliation(PlayerState playerState) {
		Iterator<InputState> it = pendingInputs.iterator();
		while(it.hasNext()){
			InputState input = it.next();
			if(input.getSequenceNumber() <= snapSequenceNumber){
				it.remove();
			}
			else {
				InputManager.applyInputToPlayer(input, player);
				game.step();
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
					Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W), 
					Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S),
					Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A),
					Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D), inputSequenceNo);
			
			screenManager.getTestClient().sendInputState(inputState, player.getUserID());
			pendingInputs.add(inputState);
			
			// TODO implementar en net
			if(Gdx.input.isTouched()){
				tryAttack1(dt);
			}
		}
		
		
	}

	private void handleInstantInput(float dt) {
		// Debug HUD
		if(Gdx.input.isKeyJustPressed(Input.Keys.F8))
        	debugging  = !debugging;
		
		if(debugging){
			if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
	        	player.hurt(10);
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.F10))
	        	debugHud.toggleFPS();
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.F11))
	        	debugHud.toggleInfoPlayer();
	        	
	        if(Gdx.input.isKeyJustPressed(Input.Keys.F12))
	        	freeCameraEnabled = !freeCameraEnabled;
		}

        // Pause Menu
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        	pauseMenu.togglePauseMenu();
	}

	private void tryAttack1(float dt) {
		Vector2 position = player.getBodyPosition();
		Vector3 click = gamecam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 dir = new Vector2(click.x - position.x, click.y - position.y);
		dir.nor();
		
		screenManager.getTestClient().sendAttack1Request(dir, player.getUserID());
		
	}

	private void moveFreeCamera(float dt) {
		float mapHeight = game.getMapHeight();
		float mapWidth = game.getMapWidth();
		
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
		new Player(playerState, game);
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.getMapRenderer().render();
        game.updateLiquidAnimations();
        
        juego.batch.setProjectionMatrix(gamecam.combined);
        juego.batch.begin();
        game.draw(juego.batch);
        juego.batch.end();
        
        if(debugging){
        	b2dr.render(game.getWorld(), gamecam.combined);
            drawGhost();
            juego.batch.setProjectionMatrix(debugHud.getProjectionMatrix());
            debugHud.draw();
        }
        
       
        juego.batch.setProjectionMatrix(pauseMenu.getProjectionMatrix());
        pauseMenu.draw();
        
        if(drawHud ){
            juego.batch.setProjectionMatrix(hud.getProjectionMatrix());
            hud.draw();
        }
        
        console.draw();
        
    	
	}

	/*
	 * Draw the last player position acknowledged by the server.
	 */
	private void drawGhost() {
		if(lastSnapshot != null){
			sr.setProjectionMatrix(gamecam.combined);
        	Vector2 pos = lastSnapshot.getPlayers().get(player.getUserID()).getBodyPosition();
        	
        	sr.begin();
        	sr.setColor(Color.RED);
            sr.rect(pos.x - 16f / PPM, pos.y - 16f / PPM, 32f / PPM, 32f / PPM);
            sr.end();
        } 
	}

	@Override
	public void resize(int width, int height) {
		console.refresh();
		gamePort.update(width, height);
		pauseMenu.getViewPort().update(width, height);
		debugHud.getViewPort().update(width, height);
		hud.getViewPort().update(width, height);
	}

	
	@Override
	public void show() {}
	
	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		game.dispose();
		b2dr.dispose();
		debugHud.dispose();
		pauseMenu.dispose();
		hud.dispose();
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

	public void destroyProjectile(long userID, long seqNo) {
		Projectile p = game.getProjectile(userID, seqNo);
		if(p!=null)
			p.disposeNextUpdate();
	}
	
}
