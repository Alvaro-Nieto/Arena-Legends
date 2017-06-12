package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.alvaronieto.pfcdam.Util.Config;
import es.alvaronieto.pfcdam.Util.SecurityUtility;
import es.alvaronieto.pfcdam.gameobjects.GameRules;

public class MainScreen extends MenuScreen {

	public MainScreen(final ScreenManager screenManager) {
		super(screenManager);
	}
	
	@Override
	protected void buildStage() {
		
		Table tableatras=new Table();
        
		tableatras.pad(5f);
		tableatras.setFillParent(true);
        TextButton atrasBtn = new TextButton("Atras", getSkin());

        atrasBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.showModeScreen();
				return false;
			}
        });
        
        tableatras.top();
        tableatras.right();
        tableatras.add(atrasBtn);
		
        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        
        Label info = new Label("", getSkin());
        
        TextButton clienteBtn = new TextButton("Unirse a partida", getSkin());
        clienteBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.showSearchScreen();
				return false;
				
			}

        });
        clienteBtn.addListener(new InputListener(){
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "Busca y unete a un servidor");
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "");
			};
        });        
        TextButton serverBtn = new TextButton("Crear partida", getSkin());
        serverBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				long adminToken = SecurityUtility.getAdminToken();
				screenManager.launchGameServer(GameRules.getDefault(), adminToken); // TODO Descomentar
				screenManager.launchGameClient().connect("localhost", adminToken, Config.getInstance().playerName);
				return false;
			}
        });
        serverBtn.addListener(new InputListener(){
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "Crea y configura un servidor");
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
				changeText(info, "");
			};
        });        
       
        clienteBtn.center();
        serverBtn.center();
        
        table.add(clienteBtn);
        table.row();
        table.add(serverBtn).padBottom(200f);
        table.row();
        table.add(info);
        stage.addActor(tableatras);
        stage.addActor(table);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	protected void beforeBuild() {
		// TODO Auto-generated method stub
	}
	

}