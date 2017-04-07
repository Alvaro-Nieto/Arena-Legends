package es.alvaronieto.pfcdam.Screens;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Sprites.Player;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.kryoclient.TestClient;
import es.alvaronieto.pfcdam.net.kryoserver.TestServer;

public class ScreenManager implements ClientListener {

	private Juego juego;
	private TestServer server;
	private TestClient testClient;
	private MainScreen mainScreen;
	private PlayScreen playScreen;
	public enum Screens{MainScreen, PlayScreen};
	private Screens currentScreen;
	private long lastSnap = Long.MIN_VALUE;
	
	public ScreenManager(Juego juego){
		this.juego = juego;
		mainScreen = new MainScreen(this);
		juego.setScreen(mainScreen);
		currentScreen = Screens.MainScreen;
	}
	
	public void launchGameClient(boolean launchServer) {
		if(launchServer){
        	server = new TestServer(3);
        }
        testClient = new TestClient(this);
	}

	
	public TestServer getServer() {
		return server;
	}

	public void setServer(TestServer server) {
		this.server = server;
	}

	public MainScreen getMainScreen() {
		return mainScreen;
	}

	public void setMainScreen(MainScreen mainScreen) {
		this.mainScreen = mainScreen;
	}

	public PlayScreen getPlayScreen() {
		return playScreen;
	}

	public void setPlayScreen(PlayScreen playScreen) {
		this.playScreen = playScreen;
	}

	public Screens getCurrentScreen() {
		return currentScreen;
	}

	public void setCurrentScreen(Screens currentScreen) {
		this.currentScreen = currentScreen;
	}

	public Juego getJuego() {
		return juego;
	}


	public void setJuego(Juego juego) {
		this.juego = juego;
	}

	public TestClient getTestClient() {
		return testClient;
	}

	public void setTestClient(TestClient testClient) {
		this.testClient = testClient;
	}

	@Override
	public void couldNotConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionAccepted(PlayerState playerState, GameState gameState) {
		mainScreen.setPlayerState(playerState);
		mainScreen.setGameState(gameState);
		mainScreen.setReadyToLaunch(true);
	}

	@Override
	public void newPlayerConnected(PlayerState playerState) {
		if(currentScreen == Screens.PlayScreen){
			playScreen.newNetworkPlayer(playerState);
		}
	}

	@Override
	public void inputReceived(InputState inputState, long userID) {
		// TODO Auto-generated method stub
		
		// SOMETHING LIKE CLIENT PREDICTION NOT REAL CLIENT PREDICTION
		if(currentScreen == Screens.PlayScreen){
			Body body = playScreen.getGame().getPlayer(userID).getBody();
			InputState input = inputState;
			if(input.isUpKey()){
				if(input.isRightKey()){
					body.applyLinearImpulse(new Vector2(0.4f,0.4f),body.getWorldCenter(), true);
				} 
				else if(input.isLeftKey()){
					body.applyLinearImpulse(new Vector2(-0.4f,0.4f),body.getWorldCenter(), true);
				} 
				else{
					body.applyLinearImpulse(new Vector2(0,0.8f),body.getWorldCenter(), true);
				}
			} 
			else if(input.isDownKey()){
				if(input.isRightKey()){
					body.applyLinearImpulse(new Vector2(0.4f,-0.4f),body.getWorldCenter(), true);
		        } 
				else if(input.isLeftKey()){
					body.applyLinearImpulse(new Vector2(-0.4f,-0.4f),body.getWorldCenter(), true);
		        } else{
		        	body.applyLinearImpulse(new Vector2(0,-0.8f),body.getWorldCenter(), true);
		        }
	        	
	        }
			else if(input.isRightKey()){
				body.applyLinearImpulse(new Vector2(0.8f,0),body.getWorldCenter(), true);
	        } 
			else if(input.isLeftKey() && body.getLinearVelocity().x >= -4){
				body.applyLinearImpulse(new Vector2(-0.8f,0),body.getWorldCenter(), true);
	        }
		}
		
	}

	@Override
	public void snapShotReceived(long timeStamp, GameState gameState) {
		// DANDO POR HECHO QUE NO HAY CLIENT PREDICTION
		
		/*
		if(currentScreen == Screens.PlayScreen && timeStamp > lastSnap){
			playScreen.setLastSnapshot(gameState);
			playScreen.setLastSnapshotTime(timeStamp);
			
			*/
			/*
			for (Map.Entry<Long, PlayerState> entry : gameState.getPlayers().entrySet()) {

		        long userID = entry.getKey();
		        PlayerState playerState = entry.getValue();
		        System.out.println("STATE:"+playerState.getPosition());
		        Player player = playScreen.getGame().getPlayer(userID);
		        System.out.println("OLD:"+player.getPosition());
		        player.getPosition().set(playerState.getPosition());
		        player.getBody().setTransform(playerState.getPosition(), 0);
		        System.out.println("NEW:"+player.getPosition());
		        
		    }*/
		/*
		} else
			System.out.println("fuera de orden");
		lastSnap = timeStamp;*/
	}
}
