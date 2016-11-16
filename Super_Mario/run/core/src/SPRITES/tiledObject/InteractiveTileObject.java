package SPRITES.tiledObject;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import SCREEN.PlayScreen;
import SPRITES.Mario;
import run.game.Jumper;

public abstract class InteractiveTileObject {
	World world;
	TiledMap map;
	TiledMapTile tile;
	Rectangle bounds;
	Body body;
	
	Jumper jumper;
	BodyDef bdef;
	FixtureDef fdef;
	PolygonShape pshape;
	
	public Fixture fixture;
	PlayScreen screen;
	MapObject object;
	
	public InteractiveTileObject(PlayScreen screen, MapObject object){
		this.object = object;
		this.screen = screen;
		this.world = screen.getWorld();
		map = screen.getMap();
		this.bounds = ((RectangleMapObject)object).getRectangle();
		bdef = new BodyDef();
		fdef = new FixtureDef();
		pshape = new PolygonShape();
		ObjectofMap(bounds);

		
	}
	//MapObject object
	public void ObjectofMap(Rectangle bounds){
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX()+bounds.getWidth()/2)/jumper.PPM, (bounds.getY()+bounds.getHeight()/2)/jumper.PPM);
		body = world.createBody(bdef);

		pshape.setAsBox(bounds.getWidth()/2/jumper.PPM, bounds.getHeight()/2/jumper.PPM);		
		
		fdef.shape = pshape;
		fixture = body.createFixture(fdef);
		
		
	}
	
	public abstract void onHeadHit(Mario mario);
	
	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}
	public TiledMapTileLayer.Cell getCell(){
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);
		
		return layer.getCell((int)(body.getPosition().x*Jumper.PPM/16), 
				(int)(body.getPosition().y*Jumper.PPM/16));
		
	}

}
