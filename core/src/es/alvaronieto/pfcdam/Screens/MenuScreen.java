package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Util.Constants;

public abstract class MenuScreen implements Screen {
	
	private Juego juego;
	protected OrthographicCamera gamecam;
	protected Viewport viewPort;
    protected Stage stage;
	private Skin skin;
	protected ScreenManager screenManager;
	
	public MenuScreen(final ScreenManager screenManager){
		this.screenManager = screenManager;
        this.juego = screenManager.getJuego();
        this.skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
        // SET CAMERA
        gamecam = new OrthographicCamera();
        viewPort = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, juego.batch);
        Gdx.input.setInputProcessor(stage);
        buildStage();
        postBuild();
	}
	
	protected abstract void buildStage();
	protected abstract void postBuild();
	protected Skin getSkin() {
		return skin;
	}
	
	public void update(float dt) {
		gamecam.update();
		stage.act(dt);
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
		viewPort.update(width, height, true);
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
		skin.dispose();
		
	}

	public Juego getJuego() {
		return juego;
	}

	public OrthographicCamera getGamecam() {
		return gamecam;
	}

	public Viewport getViewPort() {
		return viewPort;
	}

	public Stage getStage() {
		return stage;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}
	
}
