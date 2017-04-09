package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class TitleScreen extends MenuScreen {

	public TitleScreen(ScreenManager screenManager) {
		super(screenManager);
	}

	@Override
	protected void stageDefinition() {
	    Table table = new Table();
	    table.pad(5f);
	    table.setFillParent(true);
	
	    Label label=new Label("TITULO DE JUEGO. EL JUEGAZO DE LAS PELEAS TURBO DASH POWER OVERFLOW BROS", getSkin());
	    TextButton startBtn = new TextButton("Pulsa para empezar", getSkin());
	    startBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setModeScreen(new ModeScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.ModeScreen);
	        	screenManager.getJuego().getScreen().dispose();
	        	screenManager.getJuego().setScreen(screenManager.getModeScreen());
				return false;
			}
	    });
	   
	    startBtn.center();
	    
	    table.add(label);
	    table.row();
	    table.add(startBtn);
	    stage.addActor(table);
	}
}
