package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import es.alvaronieto.pfcdam.GameRules;
import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.net.ClientListener;
import es.alvaronieto.pfcdam.net.kryoclient.TestClient;
import es.alvaronieto.pfcdam.net.kryoserver.TestServer;

public class ScreenManager implements ClientListener {

	private static ScreenManager screenManager;
	
	private Juego juego;
	private TestServer server;
	private TestClient testClient;
	private TitleScreen titleScreen;
	private MainScreen mainScreen;
	private PlayScreen playScreen;
	private ModeScreen modeScreen;
	private CharSelectionScreen charSelectionScreen;
	private SearchScreen searchScreen;
	private LobbyScreen lobbyScreen;
	
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
	}
	
	public TestServer launchGameServer(GameRules gameRules, long adminToken){
		return (server = new TestServer(gameRules, adminToken));
	}
	
	public TestClient launchGameClient() {
        return (testClient = new TestClient(this));
	}

	@Override
	public void couldNotConnect() {
		System.out.println("No se puede conectar al server");
	}

	@Override
	public void startGame(final PlayerState playerState, final GameState gameState) {
		
		Gdx.app.postRunnable(new Runnable() {
	        @Override
	        public void run() {
	        	screenManager.setPlayScreen(new PlayScreen(screenManager, playerState.getUserID(), gameState));
	        	screenManager.setCurrentScreen(Screens.PlayScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getPlayScreen());
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
	public void connectionAccepted(final long userID, final LobbyState lobbyState, final boolean admin) {
		Gdx.app.postRunnable(new Runnable() {
	        @Override
	        public void run() {
	        	screenManager.lobbyScreen = new LobbyScreen(ScreenManager.getInstance(), admin, lobbyState, userID);
	        	screenManager.setCurrentScreen(Screens.LobbyScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getLobbyScreen());
	        }
		});
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

	public void setScreen(Screen screen) {
		juego.setScreen(screen);
	}

	public Screen getScreen() {
		return juego.getScreen();
	}
	
	public TestServer getServer() {
		return server;
	}

	public void setServer(TestServer server) {
		this.server = server;
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
	
	public TitleScreen getTitleScreen() {
		return titleScreen;
	}

	public void setTitleScreen(TitleScreen titleScreen) {
		this.titleScreen = titleScreen;
	}

	public MainScreen getMainScreen() {
		return mainScreen;
	}

	public void setMainScreen(MainScreen mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	public ModeScreen getModeScreen(){
		return modeScreen;
	}
	
	public void setModeScreen(ModeScreen modeScreen){
		this.modeScreen=modeScreen;
	}
	
	public CharSelectionScreen getCharSelectionScreen(){
		return charSelectionScreen;
	}
	
	public void setCharSelectionScreen(CharSelectionScreen charSelectionScreen){
		this.charSelectionScreen = charSelectionScreen;
	}
	
	public SearchScreen getSearchScreen(){
		return searchScreen;
	}
	
	public void setSearchScreen(SearchScreen searchScreen){
		this.searchScreen = searchScreen;
	}
	
	public LobbyScreen getLobbyScreen(){
		return lobbyScreen;
	}
	
	public void setLobbyScreen(LobbyScreen lobbyScreen){
		this.lobbyScreen = lobbyScreen;
	}

}