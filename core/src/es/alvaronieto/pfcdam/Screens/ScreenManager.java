package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.LogLevel;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.Util.Resources;
import es.alvaronieto.pfcdam.gameobjects.GameRules;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.kryoclient.KryoClient;
import es.alvaronieto.pfcdam.net.kryoserver.KryoServer;

public class ScreenManager implements ClientListener {

	private static ScreenManager screenManager;
	
	private Juego juego;
	private KryoServer server;
	private KryoClient testClient;
	private TitleScreen titleScreen;
	private MainScreen mainScreen;
	private PlayScreen playScreen;
	private ModeScreen modeScreen;
	private CharSelectionScreen charSelectionScreen;
	private SearchScreen searchScreen;
	private LobbyScreen lobbyScreen;
	private Console console;
	
	public enum Screens{
		TitleScreen, 
		ModeScreen, 
		MainScreen, 
		PlayScreen, 
		CharSelectionScreen, 
		SearchScreen, 
		LobbyScreen
	};
	
	private Screens currentScreen;
	private long lastSnap = Long.MIN_VALUE;
	
	private ScreenManager(){
		
	}
	
	public static synchronized ScreenManager getInstance(){
		if(screenManager == null){
			screenManager = new ScreenManager();
		}
		return screenManager;
	}
	
	public void initialize(Juego juego){
		this.juego = juego;

		titleScreen = new TitleScreen(this);
		juego.setScreen(titleScreen);
		currentScreen = Screens.TitleScreen;
		this.console = Resources.getInstance().getConsole();
	}
	
	public KryoServer launchGameServer(GameRules gameRules, long adminToken){
		return (server = new KryoServer(gameRules, adminToken, false));
	}
	
	public KryoServer launchDemoServer(GameRules gameRules, long adminToken) {
		return (server = new KryoServer(gameRules, adminToken, true));
	}
	
	public KryoClient launchGameClient() {
        return (testClient = new KryoClient(this));
	}

	@Override
	public void couldNotConnect() {
		System.out.println("No se puede conectar al server");
	}

	@Override
	public void startGame(final PlayerState playerState, final GameState gameState) {
		console.log("Starting game...");
		Gdx.app.postRunnable(new Runnable() {
	        @Override
	        public void run() {
	        	screenManager.showPlayScreen(playerState.getUserID(), gameState);
	        }
		});
	}
	
	@Override
	public void newPlayerConnected(long userID) {
		System.out.println("alguien más en el lobby");
		/*if(currentScreen == Screens.PlayScreen){
			playScreen.newNetworkPlayer(playerState);
		}*/
	}

	@Override
	public void snapShotReceived(long timeStamp, GameState gameState, long sequenceNumber) {
		// DANDO POR HECHO QUE NO HAY CLIENT PREDICTION

		if(currentScreen == Screens.PlayScreen && timeStamp > lastSnap){
			// TODO Esto es muy cutre, hay que arreglarlo
			playScreen.setLastSnapshot(gameState);
			playScreen.setLastSnapshotTime(timeStamp);
			playScreen.setSnapSequenceNumber(sequenceNumber);
		} else {
			//
		}
		
		lastSnap = timeStamp;
	}
	
	@Override
	public void newServerDiscovered(final String name, final GameRules gameRules, final int connectedPlayers, final String ipAddress) {
		if(currentScreen == Screens.SearchScreen) {
			Gdx.app.postRunnable(new Runnable() {
		        @Override
		        public void run() {
		        	searchScreen.addEntry(name, gameRules, connectedPlayers, ipAddress);
		        }
			});	
		}
	}
	
	@Override
	public void connectionAccepted(final long userID, final LobbyState lobbyState, final boolean admin, String ipAddress) {
		console.log("Succesfuly connected to lobby at "+ipAddress, LogLevel.SUCCESS);
		Gdx.app.postRunnable(new Runnable() {
	        @Override
	        public void run() {
	        	screenManager.showLobbyScreen(admin, lobbyState, userID);
	        }
		});
	}
	
	@Override
	public void connectionRejected(String ipAddress, String reason) {
		console.log("Connection rejected from "+ipAddress+": '"+reason+"'", LogLevel.ERROR);
	}

	@Override
	public void lobbyUpdate(final LobbyState lobbyState) {
		Gdx.app.postRunnable(new Runnable() {
	        @Override
	        public void run() {
	        	if(currentScreen == Screens.LobbyScreen){
	    			lobbyScreen.updateLobby(lobbyState);
	    		}
	        }
		});
	}

	public Juego getJuego() {
		return juego;
	}

	public void setJuego(Juego juego) {
		this.juego = juego;
	}

	public KryoClient getTestClient() {
		return testClient;
	}

	public void showTitleScreen(){
		juego.getScreen().dispose();
		this.titleScreen = new TitleScreen(this);
		currentScreen = Screens.TitleScreen;
		juego.setScreen(titleScreen);
	}
	
	public void showModeScreen(){
		juego.getScreen().dispose();
		modeScreen = new ModeScreen(this);
    	currentScreen = Screens.ModeScreen;
    	juego.setScreen(modeScreen);
	}

	public void showMainScreen() {
		juego.getScreen().dispose();
		mainScreen = new MainScreen(this);
    	currentScreen = Screens.MainScreen;
    	juego.setScreen(mainScreen);
	}

	public void showSearchScreen() {
		juego.getScreen().dispose();
		searchScreen = new SearchScreen(this);
    	currentScreen = Screens.SearchScreen;
    	juego.setScreen(searchScreen);
	}
	
	public void showLobbyScreen(boolean admin, LobbyState lobbyState, long userID) {
		juego.getScreen().dispose();
		lobbyScreen = new LobbyScreen(this, admin, lobbyState, userID);
    	currentScreen = Screens.LobbyScreen;
    	juego.setScreen(lobbyScreen);
	}
	
	public void showPlayScreen(long userID, GameState gameState) {
		juego.getScreen().dispose();
		playScreen = new PlayScreen(this, userID, gameState);
    	currentScreen = Screens.PlayScreen;
    	juego.setScreen(playScreen);
	}
	
	public void showCharSelectionScreen() {
		// TODO
	}



}