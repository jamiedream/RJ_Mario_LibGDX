package SPRITES.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import SCREEN.PlayScreen;
import SPRITES.Mario;
import run.game.Jumper;

public abstract class Item extends Sprite {
	PlayScreen screen;
	World world;
	boolean toDestroy;
	boolean destroyed;
	Vector2 velocity;
	public Body ibody;
	
	public Item(PlayScreen screen, float x, float y){
		this.screen = screen;
		this.world = screen.getWorld();
		setPosition(x, y);
		setBounds(getX(), getY(), 16/Jumper.PPM, 16/Jumper.PPM);
		defineItem();
		toDestroy = false;
		destroyed = false;
	}
	public abstract void defineItem();
	public abstract void use(Mario mario);//what will happen after mario take the item
	
	public void update(float dt){
		if(toDestroy && !destroyed){
			world.destroyBody(ibody);
			destroyed = true;
		}
	}
	//if the batch has been destroyed, then it is gone;
	public void draw(Batch batch){
		if(!destroyed){
			super.draw(batch);
		}
	}	
	public void destroy(){
		toDestroy = true;
	}
	public void reverseVelocity(boolean x, boolean y){
		if(x) velocity.x = -velocity.x;
		if(y) velocity.y = -velocity.y;
	}
	

}
