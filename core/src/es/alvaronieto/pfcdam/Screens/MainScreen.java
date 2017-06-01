package es.alvaronieto.pfcdam.Screens;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.alvaronieto.pfcdam.GameRules;
import es.alvaronieto.pfcdam.SecurityUtility;
import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class MainScreen extends MenuScreen {

	public MainScreen(final ScreenManager screenManager) {
		super(screenManager);
	}
	
	@Override
	protected void buildStage() {
		// TODO Auto-generated method stub

		
		Table tableatras=new Table();
        
		tableatras.pad(5f);
		tableatras.setFillParent(true);
        TextButton atrasBtn = new TextButton("Atras", getSkin());

        atrasBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.setModeScreen(new ModeScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.ModeScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getModeScreen());
				return false;
			}
        });
        
        tableatras.top();
        tableatras.right();
        tableatras.add(atrasBtn);
		
        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        
        TextButton clienteBtn = new TextButton("Unirse a partida", getSkin());
        clienteBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.setSearchScreen(new SearchScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.SearchScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getSearchScreen());
				return false;
				
			}

        });
        TextButton serverBtn = new TextButton("Crear partida", getSkin());
        serverBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				long adminToken = SecurityUtility.getAdminToken();
				screenManager.launchGameServer(GameRules.getDefault(), adminToken); // TODO Descomentar
				screenManager.launchGameClient().connect("localhost", adminToken);
				
				/*screenManager.setLobbyScreen(new LobbyScreen(screenManager, true));
				screenManager.setCurrentScreen(Screens.LobbyScreen);
				screenManager.getScreen().dispose();
				screenManager.setScreen(screenManager.getLobbyScreen());*/
				return false;
			}
        });
       
        clienteBtn.center();
        serverBtn.center();
        
        table.add(clienteBtn);
        table.row();
        table.add(serverBtn);
        stage.addActor(tableatras);
        stage.addActor(table);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	protected void postBuild() {
		// TODO Auto-generated method stub
		
	}
	

}