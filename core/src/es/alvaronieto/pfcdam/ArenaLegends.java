package es.alvaronieto.pfcdam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import es.alvaronieto.pfcdam.Screens.ScreenManager;
import es.alvaronieto.pfcdam.Util.Resources;

public class ArenaLegends extends Game {
	
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ScreenManager.getInstance().initialize(this);
		Resources.getInstance().getConsole().log("- Arena Legends -");
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
