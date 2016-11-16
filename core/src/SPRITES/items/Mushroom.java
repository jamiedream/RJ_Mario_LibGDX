package SPRITES.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import SCREEN.PlayScreen;
import SPRITES.Mario;
import run.game.Jumper;

public class Mushroom extends Item{
	BodyDef bdef;
	FixtureDef fdef;
	public Mushroom(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		setRegion(new TextureRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16));
		velocity = new Vector2(0.5f, 0);
	}

	@Override
	public void defineItem() {
		bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		ibody = world.createBody(bdef);
		
		fdef = new FixtureDef();
		CircleShape circle = new CircleShape();
		circle.setRadius(6/Jumper.PPM);
		fdef.shape = circle;
		
		fdef.filter.categoryBits = Jumper.ITEM_BIT;
		fdef.filter.maskBits = Jumper.MARIO_BIT | Jumper.GROUND_BIT | Jumper.OBJECT_BIT |
				Jumper.COIN_BIT | Jumper.BRICK_BIT;
		ibody.createFixture(fdef).setUserData(this);
		circle.dispose();
		
	}

	@Override
	public void use(Mario mario) {
		destroy();
		mario.grow();
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		super.update(dt);
		setPosition(ibody.getPosition().x - getWidth()/2, ibody.getPosition().y - getHeight()/2);
		velocity.y = ibody.getLinearVelocity().y;
		ibody.setLinearVelocity(velocity);
		
	}
	

	

}
