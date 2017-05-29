package es.alvaronieto.pfcdam.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.Resources;

/*
 * Clase que se encarga de mostrar el menú de pausa in-game
 */

public class PauseMenu implements Disposable{
	
	private Stage stage;
	private Group group;
	private Viewport viewPort;
	
	private Label pause;
	private TextButton btnContinue;
	private TextButton btnExit;
	
	private Skin skin;
	
	public PauseMenu(SpriteBatch sb){
		
		viewPort = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
		group = new Group();
		group.setSize(Constants.V_WIDTH, Constants.V_HEIGHT);
		stage = new Stage(viewPort, sb);
		skin = Resources.getInstance().getSkin();
		
		Table table = new Table();
		table.pad(5f);
		table.setFillParent(true);
		
		pause = new Label("Menu de Pausa", skin);
		btnContinue = new TextButton("Continuar", skin);
		btnExit = new TextButton("Salir del programa", skin);
		
		btnContinue.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				togglePauseMenu();
				return false;
			}
		});
		
		btnExit.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				Gdx.app.exit(); //TODO Cuando esté el lobby completado hacer que vuelva a él
				return false;
			}
		});
		
		btnContinue.center();
		btnExit.center();
		
		table.add(pause);
		table.row();
		table.add(btnContinue);
		table.row();
		table.add(btnExit);
		
		group.addActor(table);
		group.setVisible(false);
		
		stage.addActor(group);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void dispose(){
		stage.dispose();
	}
	
	public void togglePauseMenu(){
		group.setVisible(!group.isVisible());
	}

	public Viewport getViewPort() {
		return viewPort;
	}

	public void draw() {
		stage.draw();
	}
	
	public Matrix4 getProjectionMatrix() {
		return stage.getCamera().combined;
	}
}
