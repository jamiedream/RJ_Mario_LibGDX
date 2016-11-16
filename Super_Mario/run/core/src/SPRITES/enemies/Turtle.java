package SPRITES.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import SCREEN.PlayScreen;
import SPRITES.Mario;
import run.game.Jumper;

public class Turtle extends Enemy{
	public static int Left_Slide = -2;
	public static int Right_Slide = 2;
	public static enum State{WALKING, STANDING_SHELL, MOVING_SHELL, Dead};
	State current, previous;
	
	BodyDef bdef;
	FixtureDef fdef;
	float stateTimer;
	Animation walkAnimate;
	Array<TextureRegion> frames;
	boolean setToDestroy;
	boolean Destroyed;
	TextureRegion shell;
	float deadRotationDegree;
	
	public Turtle(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();
		for(int i=0; i<2; i++){
			frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), i*16, 0, 16, 24));
		}
		walkAnimate = new Animation( 0.2f, frames);		
		shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
		
		current = previous = State.WALKING;
		setBounds(getX(), getY(), 16/Jumper.PPM, 24/Jumper.PPM);
		stateTimer = 0;
		deadRotationDegree = 0;
	}
	
	protected void defineEnemy() {
		bdef = new BodyDef();
		//1 let mario show in center of the view, (getViewWidth/2)/(getViewWidth/400)/jumper.PPM
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		ebody = world.createBody(bdef);
		
		fdef = new FixtureDef();
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
				Jumper.COIN_BIT | Jumper.OBJECT_BIT | Jumper.MARIO_BIT | Jumper.ENEMY_BIT;
		ebody.createFixture(fdef).setUserData(this);
		
		//create head	
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
		if( current != State.STANDING_SHELL){
			current = State.STANDING_SHELL;
			velocity.x = 0;
		}else{
			kick(mario.getX() <= ebody.getPosition().x ? Right_Slide:Left_Slide);
		}
		
	}
	public void kick(int speed){
		velocity.x = speed;
		current = State.MOVING_SHELL;
	}
	public State getCurrentState(){
		return current;
	}
	@Override
	public void update(float dt) {
		setRegion(getFrame(dt));
		if(current == State.STANDING_SHELL && stateTimer > 5){
			current = State.WALKING;
			velocity.x = 1;
		}
		
		setPosition(ebody.getPosition().x - getWidth()/2, ebody.getPosition().y - 7/Jumper.PPM);
		if(current == State.Dead){
			deadRotationDegree += 3;
			rotate(deadRotationDegree);
			if(stateTimer > 5&& !Destroyed){
				world.destroyBody(ebody);
				Destroyed = true;
			}			
		}else  ebody.setLinearVelocity(velocity);
	}
	
	public TextureRegion getFrame(float dt){
		TextureRegion region;
		
		switch(current){
		case MOVING_SHELL:			
		case STANDING_SHELL:
			region = shell;
			break;
		case WALKING:
		default:
			region = walkAnimate.getKeyFrame(stateTimer, true);
			break;		
		}
		
		if(velocity.x > 0 && region.isFlipX() == false){
			region.flip(true, false);
		}
		if(velocity.x < 0 && region.isFlipX() == true){
			region.flip(true, false);
		}
		
		stateTimer = current == previous? stateTimer+dt:0;
		previous = current;
		return region;
	}

	@Override
	public void hitByEnemy(Enemy enemy) {
//		move stand walk
//		a v.s b
//		move move
//		move stand(k.b
//		move walk(k.b
//		stand move(k.a
//		stand stand 
//		stand walk
//		walk move(k.a 
//		walk stand
//		walk walk
		if(enemy instanceof Turtle){
			if(((Turtle)enemy).current == State.MOVING_SHELL && current != State.MOVING_SHELL){
				killed();
			}else if(current == State.MOVING_SHELL && ((Turtle)enemy).current == State.MOVING_SHELL){
				return;
			}else{ reverseVelocity(true,false);}			
			
		}else 
			if(current !=State.MOVING_SHELL){
			reverseVelocity(true,false);
		}
		
	}
	
	public void killed(){
		current = State.Dead;
		Filter filter = new Filter();
		//when turtle die, it will not having any collision.
		filter.maskBits = Jumper.NOTHING_BIT;
		for(Fixture fixture:ebody.getFixtureList()){
			fixture.setFilterData(filter);
		}
		ebody.applyLinearImpulse(new Vector2(0,2f), ebody.getWorldCenter(), true);
	}
	
	public void draw(Batch batch){
			if(!Destroyed){
				super.draw(batch);
			}
	}
	
}
