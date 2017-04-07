package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.PlayerState;

public class TitleScreen implements Screen {
	private Juego juego;
	private OrthographicCamera gamecam;
    private Viewport viewPort;
    private Stage stage;
	private Skin skin;
	private ScreenManager screenManager;
	
	public TitleScreen(final ScreenManager screenManager){
		this.screenManager = screenManager;
        this.juego = screenManager.getJuego();
        
        // SET CAMERA
        gamecam = new OrthographicCamera();
        viewPort = new FitViewport(Juego.V_WIDTH,Juego.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, juego.batch);

        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);

        Label label=new Label("TITULO DE JUEGO. EL JUEGAZO DE LAS PELEAS TURBO DASH POWER OVERFLOW BROS", getSkin());
        TextButton startBtn = new TextButton("Pulsa para empezar", getSkin());
        startBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setMainScreen(new MainScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.MainScreen);
	        	juego.getScreen().dispose();
	    		juego.setScreen(screenManager.getMainScreen());
				return false;
			}
        });
       
        startBtn.center();
        
        table.add(label);
        table.row();
        table.add(startBtn);
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
	
	public void update(float dt) {
		gamecam.update();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	juego.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
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
}
