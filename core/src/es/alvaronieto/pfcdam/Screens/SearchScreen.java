package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import es.alvaronieto.pfcdam.GameRules;
import es.alvaronieto.pfcdam.SecurityUtility;
import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;
import es.alvaronieto.pfcdam.net.kryoclient.TestClient;

public class SearchScreen extends MenuScreen {
	private Drawable drawaUnico;
	private Image personaje = new Image();
	private Table table;
	
	private String ipAddress = "localhost";
	private final TestClient client;
	
	public SearchScreen(final ScreenManager screenManager){
		super(screenManager);
		client = screenManager.launchGameClient();
		discoverServers();
	}

	private void discoverServers() {
		System.out.println("Empieza discover");
		new Thread(new Runnable() {
			@Override
			public void run() {
				client.startServerDiscovery();
			}
		}).start();
		System.out.println("Termina discover");
	}

	@Override
	protected void buildStage() {
        this.table = new Table();
        
        Table tablaOpcion=new Table();
        //table.pad(5f);
        //table.setFillParent(true);
        
        tablaOpcion.pad(5f);
        tablaOpcion.setFillParent(true);
        TextButton atrasBtn = new TextButton("Atras", getSkin());

        atrasBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.setMainScreen(new MainScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.MainScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getMainScreen());
				return false;
			}
        });
        tablaOpcion.top();
        tablaOpcion.right();
        tablaOpcion.add(atrasBtn);
        Label eligeServidor = new Label("ELIGE SERVIDOR", getSkin());
        eligeServidor.setFontScale(1.5f);
        eligeServidor.setPosition(15f, Gdx.graphics.getHeight()-50f);
        stage.addActor(eligeServidor);

        table.padRight(30f);
        table.top();
        table.row().height(100);
        table.row();
		Label servers=new Label("Nombre", getSkin());
		table.add(servers).minWidth(25);
	    Label players=new Label("Jugadores", getSkin());
		table.add(players).minWidth(25);
	    Label maps=new Label("Mapa", getSkin());
		table.add(maps).minWidth(25);
		Label seleccionar=new Label("Seleccionar", getSkin());
		table.add(seleccionar).minWidth(25);	
	    
	    //addEntry("LocalHost", GameRules.getDefault(), 0, "localhost");
        
        stage.addActor(tablaOpcion);

        ScrollPane scroll = new ScrollPane(table, getSkin());
        scroll.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scroll.setPosition(0, -100);
		stage.addActor(scroll);
		stage.setScrollFocus(scroll);
		stage.setDebugAll(true);
	}

	public void addEntry(String name, GameRules gameRules, int connectedPlayers, final String ipAddress) {
		table.row().height(60f);
		Label unserver = new Label(name, getSkin());
	    table.add(unserver).expandX();    
	    Label losplayers = new Label(
	    		String.format("%d//%d", connectedPlayers, gameRules.getMaxPlayers()), getSkin());
		table.add(losplayers).expandX();	
	    Label unmaps = new Label(gameRules.getArenaName(), getSkin());
		table.add(unmaps).expandX();        
        TextButton btn = new TextButton("Unirse", getSkin());
        btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				client.connect(ipAddress, SecurityUtility.getAdminToken());
				return false;
			}
        });
        table.add(btn).expandY();

	}

	@Override
	protected void postBuild() {
		// TODO Auto-generated method stub
	}
}


