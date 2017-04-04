package es.alvaronieto.pfcdam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import es.alvaronieto.pfcdam.Juego;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Juego.V_HEIGHT;
		config.width = Juego.V_WIDTH;
		new LwjglApplication(new Juego(), config);
	}
}
