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

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class SearchScreen extends MenuScreen {
	private Drawable drawaUnico;
	private Image personaje = new Image();
	private Table table;
	
	public SearchScreen(final ScreenManager screenManager){
		super(screenManager);
	}

	@Override
	protected void stageDefinition() {
		
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
	    
	    addEntry("Entrada de ejemplo 1","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 2","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 3","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 4","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 5","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 6","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 7","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 8","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 9","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 10","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 10","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 10","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 10","0/4","Lava cave");
	    addEntry("Entrada de ejemplo 10","0/4","Lava cave");
       
        
        stage.addActor(tablaOpcion);

        ScrollPane scroll = new ScrollPane(table, getSkin());
        scroll.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scroll.setPosition(0, -100);
		stage.addActor(scroll);
		stage.setScrollFocus(scroll);
		//stage.setDebugAll(true);
	}

	public void addEntry(String nombre, String jugadores, String mapa) {
		table.row().height(60f);
		Label unserver=new Label(nombre, getSkin());
	    table.add(unserver).expandX();    
	    Label losplayers=new Label(jugadores, getSkin());
		table.add(losplayers).expandX();	
	    Label unmaps=new Label(mapa, getSkin());
		table.add(unmaps).expandX();        
        TextButton btn = new TextButton("Unirse", getSkin());
        btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.launchGameClient();
				return false;
			}
        });
        table.add(btn).expandY();

	}
}


