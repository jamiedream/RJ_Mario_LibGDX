package SCREEN;

import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import SCENES.Hud;
import SPRITES.Mario;
import SPRITES.Mario.State;
import SPRITES.TOOLS.WorldContactListener;
import SPRITES.TOOLS.WorldCreator;
import SPRITES.enemies.Enemy;
import SPRITES.items.Item;
import SPRITES.items.ItemDef;
import SPRITES.items.Mushroom;
import run.game.Jumper;

public class PlayScreen implements Screen{
	Jumper jumper;
	float w,h;
	OrthographicCamera gameCam;
	Viewport gamePort;
	Hud hud;
	//tiled map
	TmxMapLoader maploader;
	TiledMap map;
	OrthogonalTiledMapRenderer render;
	//box2d
	World world;
	Box2DDebugRenderer b2dr;
	WorldCreator creator;
	//imagebutton
	Stage stage;
	Table lefttable, righttable;
	TextureRegion top, right, left, bottom;
	ImageButton itop, iright, ileft, ibottom;
	
	Mario player;	
	
	TextureAtlas atlas;
	Music music;
	
//	Goomba goomba;
	
	Array<Item> items;
	LinkedBlockingQueue<ItemDef> itemsToSpawn;
	
    

	public PlayScreen(Jumper jumper) {
		this.jumper = jumper;
		
		atlas = new TextureAtlas("marioANDenemies.atlas");
		w=Jumper.screenW/jumper.PPM;
		h=Jumper.screenH/jumper.PPM;
		
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(w, h);
		gamePort.setCamera(gameCam);
		hud = new Hud(jumper.batch);
		//tiled map
		maploader = new TmxMapLoader();
		map = maploader.load("level1-1.tmx");
		render = new OrthogonalTiledMapRenderer(map,1/jumper.PPM);
		gameCam.position.set(w/2, h/2, 0);
		//box2d
		world = new World(new Vector2(0,-10), true);
		b2dr = new Box2DDebugRenderer();
		creator = new WorldCreator(this);

		
		player = new Mario(this);
		world.setContactListener(new WorldContactListener());
//		goomba = new Goomba(this, 3f, .16f);
		items = new Array<Item>();
		itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
		//if mario still alive, then we have background music. 
		if(player.current != State.Dead && player.current != State.Victor){
			jumper.handleMusic();
		}

		
	}
	
	public void spawnItem(ItemDef idef){
		itemsToSpawn.add(idef);
	}
	public void handleSpawnItem(){
		if(!itemsToSpawn.isEmpty()){
			ItemDef idef = itemsToSpawn.poll();
			if(idef.type == Mushroom.class){
				items.add(new Mushroom(this, idef.position.x, idef.position.y));
			}
		}
	}
	
	public TextureAtlas getAtlas(){
		return atlas;
	}

	@Override
	public void show() {
		//where to leave imagebutton 
		stage = new Stage();        //** window is stage **//
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        lefttable = new Table();
        lefttable.bottom();
        lefttable.setFillParent(true);
        righttable = new Table();
        righttable.bottom();
        righttable.setFillParent(true);
        
		top = new TextureRegion(atlas.findRegion("arrow"));
		right = new TextureRegion(atlas.findRegion("arrow_r"));
		left = new TextureRegion(atlas.findRegion("arrow_l"));
		bottom = new TextureRegion(atlas.findRegion("arrow_b"));
		//resize button
		Sprite stop = new Sprite(top);
		int buttonsize = Gdx.graphics.getHeight()/6;
		stop.setSize(buttonsize, buttonsize);
		Sprite sright = new Sprite(right);
		sright.setSize(buttonsize, buttonsize);
		Sprite sleft = new Sprite(left);
		sleft.setSize(buttonsize, buttonsize);
		Sprite sbottom = new Sprite(bottom);
		sbottom.setSize(buttonsize, buttonsize);
		
		itop = new ImageButton(new SpriteDrawable(stop));
		iright = new ImageButton(new SpriteDrawable(sright));
		ileft = new ImageButton(new SpriteDrawable(sleft));
		ibottom = new ImageButton(new SpriteDrawable(sbottom));	
		if(player.current != State.Dead){
			//button event
			itop.addListener(new InputListener() {
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	player.mbody.applyLinearImpulse(new Vector2(0,4f), player.mbody.getWorldCenter(), true);
	                return true;
	            }
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {                
	            }
	        });
			iright.addListener(new InputListener() {
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	player.mbody.applyLinearImpulse(new Vector2(0.8f,0), player.mbody.getWorldCenter(), true);
	                return true;
	            }
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {                
	            }
	        });
			ileft.addListener(new InputListener() {
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	player.mbody.applyLinearImpulse(new Vector2(-0.8f,0), player.mbody.getWorldCenter(), true);
	                return true;
	            }
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {                
	            }
	        });
			ibottom.addListener(new InputListener() {
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	player.mbody.applyLinearImpulse(new Vector2(0,0), player.mbody.getWorldCenter(), true);
	                return true;
	            }
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {                
	            }
	        });
		}
		lefttable.add(itop).padBottom(5);
		lefttable.add(ibottom).padBottom(5);
		righttable.add(ileft).padBottom(5);
		righttable.add(iright).padBottom(5);
		stage.addActor(lefttable.align(Align.bottomLeft));
		stage.addActor(righttable.align(Align.bottomRight));
		
	
		
	}
	
	public void update(float dt){
		handleInput(dt);
		handleSpawnItem();

		gameCam.update();
		
		//setting camera position
		if(player.current != State.Dead && player.current != State.Victor){
			gameCam.position.x = player.mbody.getPosition().x;	
		}
		
		player.update(dt);
//		goomba.update(dt);
		for(Enemy enemy:creator.getEnemy()){
			enemy.update(dt);
			if(enemy.getX()<player.getX()+176/jumper.PPM){
				enemy.ebody.setActive(true);
			}
		}
		for(Item item:items){
			item.update(dt);
		}
		if(hud.worldtimer == 0){
			jumper.setScreen(new GameOver(jumper));
		}
		
		hud.update(dt);
		
		render.setView(gameCam);

	}
	public void handleInput(float dt){
		//keyboard action
		if(player.current != State.Dead && player.current != State.Victor){
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
				player.mbody.applyLinearImpulse(new Vector2(0,4f), player.mbody.getWorldCenter(), true);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				player.mbody.applyLinearImpulse(new Vector2(0,0), player.mbody.getWorldCenter(), true);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.mbody.getLinearVelocity().x >= -2){
				player.mbody.applyLinearImpulse(new Vector2(-0.1f,0), player.mbody.getWorldCenter(), true);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& player.mbody.getLinearVelocity().x <= 2){
				player.mbody.applyLinearImpulse(new Vector2(0.1f,0), player.mbody.getWorldCenter(), true);
			}	
		}

	}
	
	public boolean gameover(){
		if(player.current == State.Dead && player.getStateTimer()>2){
			return true;
		}
		return false;
	}
	
	public boolean completed(){
		if(player.current == State.Victor && player.getStateTimer()>2){
			return true;
		}
		return false;
	}
	

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//after reset screen render gameCam
		render.render();		
		//render b2dr
		b2dr.render(world, gameCam.combined);
		
		jumper.batch.setProjectionMatrix(gameCam.combined);
		
		jumper.batch.begin();
		player.draw(jumper.batch);//given mario to spritebatch
//		goomba.draw(jumper.batch);//given goomba to spritebatch
		for(Enemy enemy:creator.getEnemy()){
			enemy.draw(jumper.batch);
		}
		for(Item item:items){
			item.draw(jumper.batch);
		}
		jumper.batch.end();
		
		jumper.batch.setProjectionMatrix(hud.stage.getCamera().combined);	
		//top table
		hud.stage.act();
		hud.stage.draw();
		//controlled imagebutton
		stage.act();
		stage.draw();
		//gameover
		if(gameover()){
			jumper.setScreen(new GameOver(jumper));
			dispose();
		}
		//gameover
		if(completed()){
			jumper.setScreen(new CompleteScreen(jumper));
			dispose();
		}
		world.step(1/60f, 6, 2);
	}
	
	public TiledMap getMap(){
		return map;
	}
	public World getWorld(){
		return world;
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		map.dispose();
		render.dispose();
		b2dr.dispose();
		hud.dispose();
		
	}

}
