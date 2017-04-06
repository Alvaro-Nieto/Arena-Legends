package es.alvaronieto.pfcdam.Screens;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.kryoclient.TestClient;
import es.alvaronieto.pfcdam.net.kryoserver.TestServer;

public class ScreenManager implements ClientListener {

	private Juego juego;
	private TestServer server;
	private MainScreen mainScreen;
	private PlayScreen playScreen;
	public enum Screens{MainScreen, PlayScreen};
	private Screens currentScreen;
	
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
        TestClient testClient = new TestClient(this);
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
}
