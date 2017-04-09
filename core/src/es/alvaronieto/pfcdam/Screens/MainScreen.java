package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.kryoserver.TestServer;

public class MainScreen extends MenuScreen {

	private boolean readyToLaunch = false;
	private PlayerState playerState;
	private GameState gameState;

	public MainScreen(final ScreenManager screenManager) {
		super(screenManager);
	}
	
	@Override
	protected void stageDefinition() {
		// TODO Auto-generated method stub

        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        

        TextButton clienteBtn = new TextButton("Cliente", getSkin());
        clienteBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.launchGameClient(false);
				return false;
			}

			
        });
        TextButton serverBtn = new TextButton("Server", getSkin());
        serverBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.launchGameClient(true);
				return false;
			}
        });
       
        clienteBtn.center();
        serverBtn.center();
        
        table.add(clienteBtn);
        table.row();
        table.add(serverBtn);
        stage.addActor(table);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
        /*
         * TODO Buscar un metodo de hacer esto desde el screen manager sin que sea llamado por el hilo del kryoclient
         */
        if(readyToLaunch){
        	screenManager.setPlayScreen(new PlayScreen(screenManager, this.playerState, this.gameState));
        	screenManager.setCurrentScreen(Screens.PlayScreen);
        	screenManager.getScreen().dispose();
        	screenManager.setScreen(screenManager.getPlayScreen());
    		// It waits for server connection
    		// We should show some loading screen at this point
        }
	}

	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;		
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		
	}
	
	public boolean isReadyToLaunch() {
		return readyToLaunch;
	}

	public void setReadyToLaunch(boolean readyToLaunch) {
		this.readyToLaunch = readyToLaunch;
	}

}