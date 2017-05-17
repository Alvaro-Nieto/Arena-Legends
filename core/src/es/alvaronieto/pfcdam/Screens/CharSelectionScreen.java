package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class CharSelectionScreen extends MenuScreen {
	private Drawable drawaUnico;
	private Image personaje = new Image();
	
	public CharSelectionScreen(final ScreenManager screenManager){
		super(screenManager);
	}
	
	@Override
	protected void stageDefinition() {
		
		drawaUnico=new TextureRegionDrawable(new TextureRegion(new Texture("pruebaseleccion.png")));
        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        Table tableopcion = new Table();
        tableopcion.pad(5f);
        tableopcion.setFillParent(true);
        //Datos de personaje seleccionado
        Table descripcion=new Table();
        descripcion.pad(5f);
        descripcion.setFillParent(true);

        Label label=new Label("ELIGE PERSONAJE", getSkin());
        ImageButton p1Btn = new ImageButton(drawaUnico);
        p1Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        ImageButton p2Btn = new ImageButton(drawaUnico);
        p2Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        ImageButton p3Btn = new ImageButton(drawaUnico);
        p3Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        ImageButton p4Btn = new ImageButton(drawaUnico);
        p4Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        ImageButton p5Btn = new ImageButton(drawaUnico);
        p5Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        ImageButton p6Btn = new ImageButton(drawaUnico);
        p6Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        ImageButton p7Btn = new ImageButton(drawaUnico);
        p7Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        ImageButton p8Btn = new ImageButton(drawaUnico);
        p8Btn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				//CARGA DE UNA IMAGEN Y DESCRIPCIÓN A LA DERECHA DE LA PANTALLA
				//NO FUNCIONA LA CARGA DE IMAGEN, HAY QUE SOLUCIONARLO
				personaje=new Image(drawaUnico);
				return false;
			}
        });
        TextButton seleccionBtn = new TextButton("Seleccionar", getSkin());
        seleccionBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				screenManager.setMainScreen(new MainScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.MainScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getMainScreen());
				return false;
			}
        });
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
        tableopcion.top();
        tableopcion.left();
        tableopcion.add(seleccionBtn);
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
	}
}

