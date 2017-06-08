package es.alvaronieto.pfcdam.Util;

import static es.alvaronieto.pfcdam.Util.Constants.*;

import java.text.SimpleDateFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

import es.alvaronieto.pfcdam.SecurityUtility;
import es.alvaronieto.pfcdam.Screens.ScreenManager;

public class Resources {

	private static Resources resources;
	private TextureAtlas truemoAtlas;
	private TextureAtlas firogAtlas;
	private Skin skin;
	private Console console;
	
	private Resources(){
		this.truemoAtlas = new TextureAtlas("truemo.pack");
		this.firogAtlas = new TextureAtlas("firog.pack");
		this.skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
		this.console = new GUIConsole();
		console.setCommandExecutor(new CommandExecutor(){
        	/*public void toggleDebug() {
        		drawDebugBoxes = !drawDebugBoxes;
        	}*/
        	
        	public void connect(String address){
        		ScreenManager.getInstance().launchGameClient().connect(
        				address, SecurityUtility.getAdminToken());
        	}
        });
		this.console.setDisplayKeyID(Input.Keys.F1);
	}
	
	public static synchronized Resources getInstance(){
		if(resources == null)
			resources = new Resources();
		return resources;
	}
	
	public TextureAtlas getTruemoAtlas(){
		return this.truemoAtlas;
	}
	public TextureAtlas getFirogAtlas(){
		return this.firogAtlas;
	}
	
	public TextureAtlas getPjAtlas(String pj){
		switch (pj){
			case FIROG:
				return getFirogAtlas();
			default:
				return getTruemoAtlas();
		}
	}

	public Skin getSkin() {
		return skin;
	}
	
	public Console getConsole(){
		return this.console;
	}
	
}
