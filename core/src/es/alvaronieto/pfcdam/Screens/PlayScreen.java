package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Scenes.DebugHud;
import es.alvaronieto.pfcdam.Scenes.PauseHud;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.gameobjects.Game;
import es.alvaronieto.pfcdam.gameobjects.Player;
import es.alvaronieto.pfcdam.net.kryoserver.TestServer;

public class PlayScreen implements Screen {

	private ScreenManager screenManager;
	private Juego juego;
	
	// Camera
	private OrthographicCamera gamecam;
    private Viewport gamePort;
    
    // Tiled Map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private float mapWidth;
    private float mapHeight;
    
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
	
	public PlayScreen(ScreenManager screenManager, PlayerState playerState, GameState gameState) {
		this.screenManager = screenManager;
        this.juego = screenManager.getJuego();
        
        // SET CAMERA
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Juego.V_WIDTH / Juego.PPM,Juego.V_HEIGHT / Juego.PPM, gamecam);
        
        // LOAD TILED MAP
        loadMap();
        
        // Box2D
        world = new World(Vector2.Zero, true);
        b2dr = new Box2DDebugRenderer();
	
        // Player
        player = new Player(world, playerState);
        
        // Hud
        debugHud = new DebugHud(juego.batch, player);
        
        // Pause Hud 
        pauseHud = new PauseHud(juego.batch);
        
        // Testing Server
        //gameState = new GameState();
        game = new Game(world, gameState);
        game.addPlayer(player);
        /*if(createServer){
        	server = new TestServer(3);
        }
        TestClient testClient = new TestClient(player.getPlayerState(), this);
              */
	}
	
	private void loadMap() {
		mapLoader = new TmxMapLoader();
        map = mapLoader.load("testlevel.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / Juego.PPM);
	
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2,0);
        
        
        MapProperties prop = map.getProperties();

        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        mapWidth = (mapWidth * tilePixelWidth) / Juego.PPM;
        mapHeight = (mapHeight * tilePixelHeight) / Juego.PPM;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	public void update(float dt) {
		handleInput(dt);
		// Pruebas snapshots
		/*
		if(lastSnapshot!=null){
			for (Map.Entry<Long, PlayerState> entry : lastSnapshot.getPlayers().entrySet()) {

		        long userID = entry.getKey();
		        PlayerState playerState = entry.getValue();
		        //System.out.println("STATE:"+playerState.getPosition());
		        Player player = getGame().getPlayer(userID);
		        //System.out.println("OLD:"+player.getPosition());
		        //player.getPosition().set(playerState.getPosition());
		        player.getBody().setTransform(playerState.getPosition(), 0);
		        //System.out.println("NEW:"+player.getPosition());
		        
		    }
			lastSnapshot = null;
		}*/
		
		
		world.step(1/60f, 6, 2);
		
		debugHud.update(dt);
		
		
		if(!freeCameraEnabled){
			
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
	        }
		}
		
		gamecam.update();
		renderer.setView(gamecam);
	}
	
	private void handleInput(float dt) {
		if(freeCameraEnabled){
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
		} else {
			
			// TODO Hay que hacer algo decente
			InputState inputState = new InputState(
					Gdx.input.isKeyPressed(Input.Keys.UP), 
					Gdx.input.isKeyPressed(Input.Keys.DOWN),
					Gdx.input.isKeyPressed(Input.Keys.LEFT),
					Gdx.input.isKeyPressed(Input.Keys.RIGHT));
			
			screenManager.getTestClient().sendInputState(inputState, player.getUserID());
			 
			//  THIS SHOULD BE CLIENT PREDICTION INPUT
			if(Gdx.input.isKeyPressed(Input.Keys.UP)){
				if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
					player.getBody().applyLinearImpulse(new Vector2(0.4f,0.4f),player.getBody().getWorldCenter(), true);
				} 
				else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
					player.getBody().applyLinearImpulse(new Vector2(-0.4f,0.4f),player.getBody().getWorldCenter(), true);
				} 
				else{
					player.getBody().applyLinearImpulse(new Vector2(0,0.8f),player.getBody().getWorldCenter(), true);
				}
			} 
			else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
		        	 player.getBody().applyLinearImpulse(new Vector2(0.4f,-0.4f),player.getBody().getWorldCenter(), true);
		        } 
				else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
		        	player.getBody().applyLinearImpulse(new Vector2(-0.4f,-0.4f),player.getBody().getWorldCenter(), true);
		        } else{
		        	player.getBody().applyLinearImpulse(new Vector2(0,-0.8f),player.getBody().getWorldCenter(), true);
		        }
	        	
	        }
			else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
	        	 player.getBody().applyLinearImpulse(new Vector2(0.8f,0),player.getBody().getWorldCenter(), true);
	        } 
			else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getBody().getLinearVelocity().x >= -4){
	        	player.getBody().applyLinearImpulse(new Vector2(-0.8f,0),player.getBody().getWorldCenter(), true);
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
	
	public void newNetworkPlayer(PlayerState playerState){
		game.addPlayer(new Player(world, playerState));
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        renderer.render();
        
        b2dr.render(world, gamecam.combined);
        
        juego.batch.setProjectionMatrix(gamecam.combined);
        juego.batch.begin();
        
        juego.batch.end();
        
    	juego.batch.setProjectionMatrix(debugHud.stage.getCamera().combined);
        debugHud.stage.draw();
        pauseHud.stage.draw();
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
		map.dispose();
		renderer.dispose();
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
	
}
