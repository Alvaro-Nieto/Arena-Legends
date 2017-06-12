package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

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

        final Label info = new Label("", getSkin());
        Label label=new Label("SELECCIONA MODO DE JUEGO", getSkin());
        TextButton localBtn = new TextButton("Multijugador Local", getSkin());
        localBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.showMainScreen();
				return false;
			}
        });
        localBtn.addListener(new InputListener(){
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "Juega con gente que se encuentre dentro de tu misma red");
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "");
			};
        });        
        TextButton onlineBtn = new TextButton("Online (No disponible)", getSkin());
        onlineBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				
				return false;
			}
        });
        onlineBtn.addListener(new InputListener(){
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "Juega con gente de alrededor del mundo");
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "");
			};
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
        table.add(onlineBtn).padBottom(200f);
        table.row();
        table.add(info);
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



