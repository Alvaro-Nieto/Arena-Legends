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
import es.alvaronieto.pfcdam.Sprites.Player;
import es.alvaronieto.pfcdam.net.TestClient;
import es.alvaronieto.pfcdam.net.TestServer;

public class PlayScreen implements Screen {

	private Juego juego;
	private OrthographicCamera gamecam;
    private Viewport gamePort;
    
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
	
	// Player
	private Player player;
	
	// Testing Server
	private TestServer server;
	
	public PlayScreen(Juego juego) {
        this.juego = juego;
        
        // SET CAMERA
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Juego.V_WIDTH / Juego.PPM,Juego.V_HEIGHT / Juego.PPM, gamecam);
        
        // LOAD TILED MAP
        loadMap();
        
        // Box2D
        world = new World(Vector2.Zero, true);
        b2dr = new Box2DDebugRenderer();
	
        // Player
        player = new Player(world, mapWidth / 2, mapHeight / 2);
        
        // Hud
        debugHud = new DebugHud(juego.batch, player);
        
        // Testing Server
        server = new TestServer();
        new TestClient(player.getPlayerState(), world);
	}
	
	/*
	private void box2DTest() {
		BodyDef bdef = new BodyDef();	
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(new Vector2(mapWidth / 2 , mapHeight / 2 ));
		//bdef.position.set(new Vector2(0 , 0 ));
		
		shape.setAsBox(16 / Juego.PPM, 16 / Juego.PPM);
		fdef.shape = shape;
		
		body = world.createBody(bdef);
		body.createFixture(fdef);
	}
	*/

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
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F10))
        	debugHud.toggleFPS();
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F11))
        	debugHud.toggleInfoPlayer();
        	
        if(Gdx.input.isKeyJustPressed(Input.Keys.F12))
        	freeCameraEnabled = !freeCameraEnabled;
        
        /*System.out.println(gamecam.position.y*Juego.PPM 
        		+ ":" + gamePort.getWorldHeight()*Juego.PPM 
        		+ ":" + mapHeight*Juego.PPM);*/
        //System.out.println(gamecam.position.y + " : " + (mapHeight - gamePort.getWorldHeight()/2));
	}

	@Override
	public void render(float delta) {
		update(delta);
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        renderer.render();
        
        b2dr.render(world, gamecam.combined);
        
        juego.batch.setProjectionMatrix(gamecam.combined);
        juego.batch.begin();
        
        juego.batch.end();
        
    	juego.batch.setProjectionMatrix(debugHud.stage.getCamera().combined);
        debugHud.stage.draw();
        
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gamePort.update(width, height);
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
		// TODO Auto-generated method stub
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		debugHud.dispose();
	}
}