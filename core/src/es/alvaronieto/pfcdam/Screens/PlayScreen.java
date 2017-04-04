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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Juego;

public class PlayScreen implements Screen {

	private Juego juego;
	private OrthographicCamera gamecam;
    private Viewport gamePort;
    
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private float mapWidth;
    private float mapHeight;
	
	public PlayScreen(Juego juego) {
        this.juego = juego;
        // SET CAMERA
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Juego.V_WIDTH / Juego.PPM,Juego.V_HEIGHT / Juego.PPM, gamecam);
        
        // LOAD TILED MAP
        loadMap();
	
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
		gamecam.update();
		renderer.setView(gamecam);
	}
	
	private void handleInput(float dt) {
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
        	
        /*System.out.println(gamecam.position.y*Juego.PPM 
        		+ ":" + gamePort.getWorldHeight()*Juego.PPM 
        		+ ":" + mapHeight*Juego.PPM);*/
        System.out.println(gamecam.position.y + " : " + (mapHeight - gamePort.getWorldHeight()/2));
	}

	@Override
	public void render(float delta) {
		update(delta);
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        renderer.render();
        
        juego.batch.setProjectionMatrix(gamecam.combined);
        juego.batch.begin();
        
        juego.batch.end();
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
		
		
	}
}