package es.alvaronieto.pfcdam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.Util.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Constants.V_HEIGHT;
		config.width = Constants.V_WIDTH;
		//config.vSyncEnabled = false; // Setting to false disables vertical sync
		//config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
		//config.backgroundFPS = 0; // Setting to 0 disables background fps throttling
		//config.fullscreen = true;
		new LwjglApplication(new Juego(), config);
	}
}
