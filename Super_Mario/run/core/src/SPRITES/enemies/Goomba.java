package SPRITES.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import MENU.Setting;
import SCREEN.PlayScreen;
import SPRITES.Mario;
import run.game.Jumper;

public class Goomba extends Enemy{
	BodyDef bdef;
	FixtureDef fdef;
	float stateTimer;
	Animation walkAnimate;
	Array<TextureRegion> frames;
	boolean setToDestroy;
	boolean Destroyed;
	Jumper jumper;

	public Goomba(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();
		for(int i = 0; i<2; i++){
			frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"),i*16,0,16,16));
		}
		walkAnimate = new Animation(0.1f, frames);
		stateTimer = 0;
		setBounds(getX(), getY(), 16/Jumper.PPM, 16/Jumper.PPM);
		setToDestroy = false;
		Destroyed = false;
	}
	
	public void update(float dt){
		stateTimer += dt;
		
		if(setToDestroy && !Destroyed){
			world.destroyBody(ebody);//destroy goomba shape
			Destroyed = true;
			//when goomba destroyed, get smash picture
			setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
			stateTimer = 0;//reset timer to know how long goomba dead
		}else if(!Destroyed){
			ebody.setLinearVelocity(velocity);
			setPosition(ebody.getPosition().x-getWidth()/2, ebody.getPosition().y - 7/Jumper.PPM);
			setRegion(walkAnimate.getKeyFrame(stateTimer, true));
		}
	}

	@Override
	protected void defineEnemy() {
		bdef = new BodyDef();
		//1 let mario show in center of the view, (getViewWidth/2)/(getViewWidth/400)/jumper.PPM
//		bdef.position.set(280/Jumper.PPM,32/Jumper.PPM);
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		ebody = world.createBody(bdef);
		
		fdef = new FixtureDef();
//		CircleShape circle = new CircleShape();
//		circle.setRadius(7/Jumper.PPM);
		//solved: when mario met goombas usually dead
//		EdgeShape ene = new EdgeShape();
//		ene.set(new Vector2(-3/Jumper.PPM, -3/Jumper.PPM),  new Vector2(3/Jumper.PPM, -3/Jumper.PPM));
        Vector2[] vertices = {
                new Vector2(-3.0f, 3.0f).scl(1 / Jumper.PPM),
                new Vector2(3.0f, 3.0f).scl(1 / Jumper.PPM),
                new Vector2(-2.0f, -3.0f).scl(1 / Jumper.PPM),
                new Vector2(2.0f, -3.0f).scl(1 / Jumper.PPM),
        };
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);
		fdef.shape = polygonShape;
		//default filter && colliding filter
		fdef.filter.categoryBits = Jumper.ENEMY_BIT;
		fdef.filter.maskBits = Jumper.GROUND_BIT | Jumper.BRICK_BIT | 
				Jumper.COIN_BIT | Jumper.OBJECT_BIT | Jumper.MARIO_BIT | Jumper.ENEMY_BIT ;
		ebody.createFixture(fdef).setUserData(this);
		
		//create head
//		PolygonShape head = new PolygonShape();
//		Vector2[] vertice = new Vector2[4];
//		vertice[0] = new Vector2(-3, 8).scl(1/Jumper.PPM);
//		vertice[1] = new Vector2(3, 8).scl(1/Jumper.PPM);
//		vertice[2] = new Vector2(-3, 3).scl(1/Jumper.PPM);
//		vertice[3] = new Vector2(3, 3).scl(1/Jumper.PPM);
//		head.set(vertice);
		
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-3/Jumper.PPM, 7/Jumper.PPM),  new Vector2(3/Jumper.PPM, 7/Jumper.PPM));
				
		fdef.shape = head;
		fdef.restitution = 0.2f;
		fdef.filter.categoryBits = Jumper.ENEMY_HEAD_BIT;
		ebody.createFixture(fdef).setUserData(this);//this = head
		
		polygonShape.dispose();
		head.dispose();
	}

	@Override
	public void hitOnHead(Mario mario) {
		setToDestroy = true;
		if(Setting.isCheckedSound){
			jumper.manager.get("audio/sounds/stomp.wav", Sound.class).stop();
		}else{
			jumper.manager.get("audio/sounds/stomp.wav", Sound.class).play();
		}
	}
	//if goomba has been destroyed and stateTimer<1, override the original draw
	public void draw(Batch batch){
		if(!Destroyed || stateTimer < 1){
			super.draw(batch);
		}
	}

	@Override
	public void hitByEnemy(Enemy enemy) {
		if(enemy instanceof Turtle && ((Turtle)enemy).current == Turtle.State.MOVING_SHELL){
			setToDestroy = true;
		}else reverseVelocity(true,false);
		
	}

}
