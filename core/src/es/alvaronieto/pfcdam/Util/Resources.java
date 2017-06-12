package es.alvaronieto.pfcdam.Util;

import static es.alvaronieto.pfcdam.Util.Constants.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.LogLevel;

import es.alvaronieto.pfcdam.Screens.ScreenManager;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.gameobjects.Game;
import es.alvaronieto.pfcdam.gameobjects.GameRules;
import es.alvaronieto.pfcdam.gameobjects.Player;

public class Resources {

	private static Resources resources;
	private TextureAtlas truemoAtlas;
	private TextureAtlas firogAtlas;
	private TextureAtlas venetoAtlas;
	private Skin skin;
	private Console console;
	
	private Resources(){
		this.truemoAtlas = new TextureAtlas("truemo.pack");
		this.firogAtlas = new TextureAtlas("firog.pack");
		this.venetoAtlas = new TextureAtlas("veneto.pack");
		this.skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
		this.console = new GUIConsole();
		setCommandExecutor();
		this.console.setDisplayKeyID(Input.Keys.F1);
	}
	
	public static synchronized Resources getInstance(){
		if(resources == null)
			resources = new Resources();
		return resources;
	}
	
	private void setCommandExecutor() {
		console.setCommandExecutor(Commands.getInstance());
	}
	
	public TextureAtlas getTruemoAtlas(){
		return this.truemoAtlas;
	}
	public TextureAtlas getFirogAtlas(){
		return this.firogAtlas;
	}
	public TextureAtlas getVenetoAtlas(){
		return this.venetoAtlas;
	}
	
	public TextureAtlas getPjAtlas(String pj){
		switch (pj){
			case FIROG:
				return getFirogAtlas();
			case VENETO:
				return getVenetoAtlas();
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
