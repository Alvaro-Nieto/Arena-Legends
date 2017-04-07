package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Server;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Scenes.DebugHud;
import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;
import es.alvaronieto.pfcdam.Sprites.Player;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.kryoclient.TestClient;
import es.alvaronieto.pfcdam.net.kryoserver.TestServer;

public class MainScreen implements Screen {

	private Juego juego;
	private OrthographicCamera gamecam;
    private Viewport viewPort;
    private Stage stage;
	private Skin skin;
	private TestServer server;
	private ScreenManager screenManager;
	private boolean readyToLaunch = false;
	private PlayerState playerState;
	private GameState gameState;

	public boolean isReadyToLaunch() {
		return readyToLaunch;
	}

	public void setReadyToLaunch(boolean readyToLaunch) {
		this.readyToLaunch = readyToLaunch;
	}

	public MainScreen(final ScreenManager screenManager) {
		this.screenManager = screenManager;
        this.juego = screenManager.getJuego();
        
        // SET CAMERA
        gamecam = new OrthographicCamera();
        viewPort = new FitViewport(Juego.V_WIDTH,Juego.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, juego.batch);

        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        

        TextButton clienteBtn = new TextButton("Cliente", getSkin());
        clienteBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.launchGameClient(false);
				return false;
			}

			
        });
        TextButton serverBtn = new TextButton("Server", getSkin());
        serverBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.launchGameClient(true);
				return false;
			}
        });
       
        clienteBtn.center();
        serverBtn.center();
        
        table.add(clienteBtn);
        table.row();
        table.add(serverBtn);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        
	}
	
	protected Skin getSkin() {
		// TODO Mejorar esto y no repetirlo en todos lados
		if(skin == null){
			skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		}
		return skin;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void update(float dt) {
		gamecam.update();
	}
	
	
	@Override
	public void render(float delta) {
		update(delta);
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	juego.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
        /*
         * TODO Buscar un metodo de hacer esto desde el screen manager sin que sea llamado por el hilo del kryoclient
         */
        if(readyToLaunch){
        	screenManager.setPlayScreen(new PlayScreen(screenManager, this.playerState, this.gameState));
        	screenManager.setCurrentScreen(Screens.PlayScreen);
        	juego.getScreen().dispose();
    		juego.setScreen(screenManager.getPlayScreen());
    		// It waits for server connection
    		// We should show some loading screen at this point
        }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		viewPort.update(width, height);
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
		stage.dispose();
	}

	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;		
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		
	}

}