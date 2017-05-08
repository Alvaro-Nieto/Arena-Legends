package es.alvaronieto.pfcdam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import es.alvaronieto.pfcdam.Screens.MainScreen;
import es.alvaronieto.pfcdam.Screens.PlayScreen;
import es.alvaronieto.pfcdam.Screens.ScreenManager;

public class Juego extends Game {
	
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;
	public static final float PPM = 100;
	
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ScreenManager.getInstance(this);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	
}
