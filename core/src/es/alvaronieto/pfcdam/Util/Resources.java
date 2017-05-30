package es.alvaronieto.pfcdam.Util;

import java.text.SimpleDateFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources {

	private static Resources resources;
	private TextureAtlas truemoAtlas;
	private Skin skin;
	
	private Resources(){
		this.truemoAtlas = new TextureAtlas("truemo.pack");
		this.skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
	}
	
	public static synchronized Resources getInstance(){
		if(resources == null)
			resources = new Resources();
		return resources;
	}
	
	public TextureAtlas getTruemoAtlas(){
		return this.truemoAtlas;
	}

	public Skin getSkin() {
		return skin;
	}
	
}
