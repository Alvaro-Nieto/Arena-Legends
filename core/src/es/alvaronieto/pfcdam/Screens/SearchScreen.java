package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.Table.DebugRect;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class SearchScreen extends MenuScreen {
	private Drawable drawaUnico;
	private Image personaje = new Image();
	
	public SearchScreen(final ScreenManager screenManager){
		super(screenManager);
	}

	@Override
	protected void stageDefinition() {
		
		
		//drawaUnico=new TextureRegionDrawable(new TextureRegion(new Texture("pruebaseleccion.png")));
        Table table = new Table();
        Table tablaOpcion=new Table();
        table.pad(5f);
        table.setFillParent(true);
        tablaOpcion.pad(5f);
        tablaOpcion.setFillParent(true);
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
        tablaOpcion.top();
        tablaOpcion.left();
        tablaOpcion.add(atrasBtn);
        table.row().height(200);
        Label label=new Label("ELIGE SERVIDOR", getSkin());
        table.add(label);
        table.row();
		Label servers=new Label("Servidores(23)", getSkin());
		table.add(servers).minWidth(20);
	    Label games=new Label("Juego", getSkin());
		table.add(games).minWidth(20);  
	    Label players=new Label("Jugadores", getSkin());
		table.add(players).minWidth(20);
	    Label maps=new Label("Mapa", getSkin());
		table.add(maps).minWidth(20);
		Label seleccionar=new Label("Seleccionar", getSkin());
		table.add(seleccionar).minWidth(20);	
	    table.row();
	    //Metodo que saca los datos de los servers disponibles.
	    Label unserver=new Label("Kancheski", getSkin());
	    table.add(unserver).expandX();
	    Label unjuego=new Label("Game Overflow bros hit smash", getSkin());
		table.add(unjuego).expandX();	    
	    Label losplayers=new Label("1/2", getSkin());
		table.add(losplayers).expandX();	
	    Label unmaps=new Label("Lava cave", getSkin());
		table.add(unmaps).expandX();        
        TextButton btn = new TextButton("Unirse", getSkin());
        btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setCharSelectionScreen(new CharSelectionScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.CharSelectionScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getCharSelectionScreen());
				return false;
			}
        });
        table.add(btn).expandX();
        table.row();
       
       
        
         /*
        Label cualidad1=new Label("CUALIDAD1 (Ataque)", getSkin());
        Label cualidad2=new Label("CUALIDAD2 (Resistencia)", getSkin());
        Label cualidad3=new Label("CUALIDAD3 (Velocidad)", getSkin());
       
        p1Btn.center();
        p2Btn.center();
        p3Btn.center();
        p4Btn.center();
        p5Btn.center();
        p6Btn.center();
        p7Btn.center();
        p8Btn.center();
        atrasBtn.center();
        seleccionBtn.center();
        
        table.add(label);
        table.row();
        table.add(p1Btn);
        table.add(p2Btn);
        table.add(p3Btn);
        table.row();
        table.add(p4Btn);
        table.add(p5Btn);
        table.row();
        table.add(p6Btn);
        table.add(p7Btn);
        table.add(p8Btn);
        
        
        tableopcion.right();
        tableopcion.add(atrasBtn);
        descripcion.center();
        descripcion.right();
        descripcion.add(cualidad1);
        descripcion.add(personaje);
        descripcion.row();
        //Falta añadir una imagen por cada cualidad que determine el nivel que tiene de esa habilidad a la derecha o abajo       
        descripcion.add(cualidad1);
        descripcion.row();
        descripcion.add(cualidad2);
        descripcion.row();
        descripcion.add(cualidad3);
        descripcion.row();
        stage.addActor(table);
        stage.addActor(tableopcion);
        stage.addActor(descripcion);	
        */
        stage.addActor(tablaOpcion);
		stage.addActor(table);
		
	}
	}


