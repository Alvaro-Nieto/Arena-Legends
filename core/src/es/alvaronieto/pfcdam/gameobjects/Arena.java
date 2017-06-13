package es.alvaronieto.pfcdam.gameobjects;

import static es.alvaronieto.pfcdam.Util.Constants.PPM;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import es.alvaronieto.pfcdam.Util.BodyData;
import es.alvaronieto.pfcdam.Util.Constants;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Arena implements Disposable {
	
	private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private float mapWidth;
    private float mapHeight;
    private World world;
	private	java.util.Map<String, TiledMapTile> liquidTiles; 
	private ArrayList<TiledMapTileLayer.Cell>liquidCells;
	private float elapsedSinceAnimation = 0.0f;
	
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
        
        searchTiles(path);
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
        	body.setUserData(new BodyData(0, "WALL", 0));
        	shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);        	
        	fdef.shape = shape;        	
        	body.createFixture(fdef);
        }
	}
	
	private void searchTiles(String path){
		
		int numLayer = 0;
		
		if(path.equals(Constants.ARENA_LAVA)){
			numLayer = 1;
		}
		else{
			numLayer = 4;
		}
		
		TiledMapTileSet tileset =  map.getTileSets().getTileSet("terrain_atlas");
        
        liquidTiles = new HashMap<String, TiledMapTile>();
        
        for(TiledMapTile tile: tileset){
        	Object property = tile.getProperties().get("LiquidFrame");
        	if(property!=null){
        		liquidTiles.put(property.toString(), tile);
        	}
        }
        
        liquidCells = new ArrayList<TiledMapTileLayer.Cell>();
        
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(numLayer);
	
        for(int x = 0; x < layer.getWidth(); x++){
        	for(int y = 0; y < layer.getHeight(); y++){
        		TiledMapTileLayer.Cell cell = layer.getCell(x, y);    
        		if(cell!=null){
        			Object property = cell.getTile().getProperties().get("LiquidFrame");
        			if(property!=null){
        				liquidCells.add(cell);
        			}
        		}
        	}
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
	
	public void updateCells(){
		elapsedSinceAnimation += Gdx.graphics.getDeltaTime();
		if(elapsedSinceAnimation>0.5f){
			for(TiledMapTileLayer.Cell cell: liquidCells){
				String property = cell.getTile().getProperties().get("LiquidFrame").toString();
				Integer currentAnimationFrame = Integer.parseInt(property);
				currentAnimationFrame++;
				if(currentAnimationFrame>liquidTiles.size()){
					currentAnimationFrame = 1;
				}
		
				TiledMapTile newTile = liquidTiles.get(currentAnimationFrame.toString());
				cell.setTile(newTile);
			}
			elapsedSinceAnimation = 0.0f;
		}
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}
}
