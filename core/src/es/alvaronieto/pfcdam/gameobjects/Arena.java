package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.PPM;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class Arena implements Disposable {
	
	private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private float mapWidth;
    private float mapHeight;
    private World world;
	
	public Arena(String path, World world) {
		this.world = world;
		
		mapLoader = new TmxMapLoader();
		
        map = mapLoader.load(path);
        renderer = new OrthogonalTiledMapRenderer(map, 1  / PPM);
        
        MapProperties prop = map.getProperties();

        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        mapWidth = (mapWidth * tilePixelWidth) / PPM;
        mapHeight = (mapHeight * tilePixelHeight) / PPM;
        
        createBodies();    
	}
	
	private void createBodies(){
		BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;   
        
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
        	
        	Rectangle rect = ((RectangleMapObject)object).getRectangle();
        	
        	bdef.type = BodyDef.BodyType.StaticBody;
        	bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight()/2)/PPM);
        	body = world.createBody(bdef);
        	
        	shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);        	
        	fdef.shape = shape;        	
        	body.createFixture(fdef);
        }
	}
	
	

	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
	}

	public float getMapWidth() {
		return mapWidth;
	}

	public float getMapHeight() {
		return mapHeight;
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}
}
