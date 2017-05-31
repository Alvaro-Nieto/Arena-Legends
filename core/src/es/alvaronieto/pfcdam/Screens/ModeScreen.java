package es.alvaronieto.pfcdam.Screens;

import java.util.Date;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class ModeScreen extends MenuScreen {
	private MenuListener localListener;
	private MenuListener onlineListener;
	
	
	public ModeScreen(final ScreenManager screenManager){
		super(screenManager);
	}

	@Override
	protected void stageDefinition() {
		float timeOut = 0.8f;		
		Table table = new Table();
        table.pad(0f);
        table.pack();
        table.setFillParent(true);

        
        Table tableatras = new Table();
        tableatras.pad(5f);
        tableatras.setFillParent(true);
        
        
        final TextButton test1 = new TextButton("TESET1", getSkin());
        final TextButton test2 = new TextButton("TESET2", getSkin());
        final TextButton test3 = new TextButton("TESET3", getSkin());
        final TextButton test4 = new TextButton("TESET4", getSkin());
        /*test1.setVisible(false);
        test2.setVisible(false);
        test3.setVisible(false);
        test4.setVisible(false);*/
        
       

        Label label = new Label("SELECCIONA MODO DE JUEGO", getSkin());
        TextButton localBtn = new TextButton("Multijugador Local", getSkin());
        localListener = new MenuListener(new TextButton[]{test1, test2}, new TextButton[]{test3,test4}, timeOut);
        localBtn.addListener(localListener);
        
        localBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				/*screenManager.setMainScreen(new MainScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.MainScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getMainScreen());*/
				
				return false;
			}
        });
        TextButton onlineBtn = new TextButton("Online (No disponible)", getSkin());
        onlineListener = new MenuListener(new TextButton[]{test3, test4}, new TextButton[]{test1,test2}, timeOut);
        onlineBtn.addListener(onlineListener);
        onlineBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				return false;
			}
        });
        TextButton atrasBtn = new TextButton("Atras", getSkin());
        atrasBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				screenManager.setTitleScreen(new TitleScreen(screenManager));
	        	screenManager.setCurrentScreen(Screens.TitleScreen);
	        	screenManager.getScreen().dispose();
	        	screenManager.setScreen(screenManager.getTitleScreen());
				return false;
			}
        });
        //localBtn.center();
        //onlineBtn.center();
        //atrasBtn.center();
        
        table.top().left();
        table.add(label);
        table.row();
        table.add(localBtn);
        table.add(test1);
        table.row();
        table.add();
        table.add(test2);
        table.row();
        table.add(onlineBtn);
        table.add(test3);
        table.row();
        table.add();
        table.add(test4);
        
        
        
        tableatras.top();
        tableatras.right();
        tableatras.add(atrasBtn);
        stage.addActor(table);
        stage.addActor(tableatras);
        
        stage.setDebugAll(true);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		localListener.update(dt);
		onlineListener.update(dt);
	}
	
	
}



