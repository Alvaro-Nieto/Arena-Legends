package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class ModeScreen extends MenuScreen {
	
	public ModeScreen(final ScreenManager screenManager){
		super(screenManager);
	}

	@Override
	protected void buildStage() {
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
				screenManager.showMainScreen();
				return false;
			}
        });
        TextButton onlineBtn = new TextButton("Online (No disponible)", getSkin());
        onlineBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				
				return false;
			}
        });
        TextButton atrasBtn = new TextButton("Atras", getSkin());
        atrasBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.showTitleScreen();
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

	@Override
	protected void beforeBuild() {
		// TODO Auto-generated method stub
	}
}



