package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class ModeScreen extends MenuScreen {
	
	public ModeScreen(final ScreenManager screenManager){
		super(screenManager);
	}

	@Override
	protected void stageDefinition() {
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
				
				screenManager.setCharSelectionScreen(new CharSelectionScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.CharSelectionScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getCharSelectionScreen());
				return false;
			}
        });
        TextButton onlineBtn = new TextButton("Online", getSkin());
        onlineBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setCharSelectionScreen(new CharSelectionScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.CharSelectionScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getCharSelectionScreen());
				return false;
			}
        });
        TextButton atrasBtn = new TextButton("Atras", getSkin());
        atrasBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setTitleScreen(new TitleScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.TitleScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getTitleScreen());
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
	}
}



