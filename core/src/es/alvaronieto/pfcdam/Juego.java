package es.alvaronieto.pfcdam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import es.alvaronieto.pfcdam.Screens.PlayScreen;

public class Juego extends Game {
	
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;
	public static final float PPM = 100;
	
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		newGame();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	
	public void newGame () {
		setScreen(new PlayScreen(this));
	}
}
