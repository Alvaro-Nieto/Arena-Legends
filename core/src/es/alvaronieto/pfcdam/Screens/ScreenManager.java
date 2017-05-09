package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
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
	
	public enum Screens{TitleScreen, ModeScreen, MainScreen, PlayScreen, CharSelectionScreen};
	private Screens currentScreen;
	private long lastSnap = Long.MIN_VALUE;
	
	private ScreenManager(Juego juego){
		this.juego = juego;

		titleScreen = new TitleScreen(this);
		juego.setScreen(titleScreen);
		currentScreen = Screens.TitleScreen;
	}
	
	public static synchronized ScreenManager getInstance(Juego juego){
		if(screenManager == null){
			screenManager = new ScreenManager(juego);
		}
		return screenManager;
	}
	
	public void launchGameServer(){
		server = new TestServer(3);
	}
	
	public void launchGameClient() {
        testClient = new TestClient(this);
	}

	@Override
	public void couldNotConnect() {
		System.out.println("No se puede conectar al server");
	}

	@Override
	public void connectionAccepted(final PlayerState playerState, final GameState gameState) {
		
		Gdx.app.postRunnable(new Runnable() {
	        @Override
	        public void run() {
	        	screenManager.setPlayScreen(new PlayScreen(screenManager, playerState, gameState));
	        	screenManager.setCurrentScreen(Screens.PlayScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getPlayScreen());
	        }
		});
	}

	@Override
	public void newPlayerConnected(PlayerState playerState) {
		if(currentScreen == Screens.PlayScreen){
			playScreen.newNetworkPlayer(playerState);
		}
	}

	@Override
	public void inputReceived(InputState inputState, long userID) {
		
		/*if(currentScreen == Screens.PlayScreen){
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
}