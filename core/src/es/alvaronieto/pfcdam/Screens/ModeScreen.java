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

public class ModeScreen implements Screen {
	private Juego juego;
	private OrthographicCamera gamecam;
    private Viewport viewPort;
    private Stage stage;
	private Skin skin;
	private ScreenManager screenManager;
	
	public ModeScreen(final ScreenManager screenManager){
		this.screenManager = screenManager;
        this.juego = screenManager.getJuego();
        
        // SET CAMERA
        gamecam = new OrthographicCamera();
        viewPort = new FitViewport(Juego.V_WIDTH,Juego.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, juego.batch);

        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        Table tableatras = new Table();
        tableatras.pad(5f);
        tableatras.setFillParent(true);

        Label label=new Label("SELECCIONA MODO DE JUEGO", getSkin());
        TextButton localBtn = new TextButton("Multijugador Local", getSkin());
        localBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setMainScreen(new MainScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.MainScreen);
	        	juego.getScreen().dispose();
	    		juego.setScreen(screenManager.getMainScreen());
				return false;
			}
        });
        TextButton onlineBtn = new TextButton("Online", getSkin());
        onlineBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setMainScreen(new MainScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.MainScreen);
	        	juego.getScreen().dispose();
	    		juego.setScreen(screenManager.getMainScreen());
				return false;
			}
        });
        TextButton atrasBtn = new TextButton("Atras", getSkin());
        atrasBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setTitleScreen(new TitleScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.TitleScreen);
	        	juego.getScreen().dispose();
	    		juego.setScreen(screenManager.getTitleScreen());
				return false;
			}
        });
       
        localBtn.center();
        onlineBtn.center();
        atrasBtn.center();
        
        table.add(label);
        table.row();
        table.add(localBtn);
        table.row();
        table.add(onlineBtn);
        tableatras.top();
        tableatras.right();
        tableatras.add(atrasBtn);
        stage.addActor(table);
        stage.addActor(tableatras);
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



