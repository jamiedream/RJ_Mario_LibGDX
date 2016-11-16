package SPRITES.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import SCREEN.PlayScreen;
import SPRITES.Mario;

public abstract class Enemy extends Sprite{
	World world;
	PlayScreen screen;
	public Body ebody;
	Vector2 velocity;
	public Enemy(PlayScreen screen, float x, float y) {
		this.world = screen.getWorld();
		this.screen = screen;
		setPosition(x,y);
		defineEnemy();
		velocity = new Vector2(0.5f,-1);
		ebody.setActive(false);
	}
	protected abstract void defineEnemy();
	public abstract void hitOnHead(Mario mario);
	public void reverseVelocity(boolean x, boolean y){
		if(x) velocity.x = -velocity.x;
		if(y) velocity.y = -velocity.y;
	}
	public abstract void update(float dt);
	public abstract void hitByEnemy(Enemy enemy);

}
