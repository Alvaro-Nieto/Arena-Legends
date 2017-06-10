package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;
import es.alvaronieto.pfcdam.Util.Config;
import es.alvaronieto.pfcdam.Util.Resources;

public class TitleScreen extends MenuScreen {

	public TitleScreen(ScreenManager screenManager) {
		super(screenManager);
		Resources.getInstance().getConsole().log("PFC DAM");
		Config.getInstance();
	}

	@Override
	protected void buildStage() {
	    Table table = new Table();
	    table.pad(5f);
	    table.setFillParent(true);
	
	    Image logo = new Image(new Texture(Gdx.files.internal("logo.png")));
	    TextButton startBtn = new TextButton("START", getSkin());
	    startBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.showModeScreen();
				return false;
			}
	    });
	   
	    startBtn.center();
	    
	    table.add(logo);
	    table.row();
	    table.add(startBtn);
	    stage.addActor(table);
	}

	@Override
	protected void beforeBuild() {
		// TODO Auto-generated method stub
	}
}
