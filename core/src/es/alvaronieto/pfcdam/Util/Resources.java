package es.alvaronieto.pfcdam.Util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Resources {

	private static Resources resources;
	private TextureAtlas atlas;
	
	private Resources(){
		this.atlas = new TextureAtlas("trueno.pack");
	}
	
	public static synchronized Resources getInstance(){
		if(resources == null)
			resources = new Resources();
		return resources;
	}
	
	public TextureAtlas getAtlas(){
		return this.atlas;
	}
}
