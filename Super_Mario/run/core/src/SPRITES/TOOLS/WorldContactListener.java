package SPRITES.TOOLS;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import SPRITES.Mario;
import SPRITES.enemies.Enemy;
import SPRITES.items.Item;
import SPRITES.tiledObject.InteractiveTileObject;
import run.game.Jumper;

public class WorldContactListener implements ContactListener{
	Fixture fixA, fixB;//create 2 objects to test collision
	Fixture head, object;//judge which one is head and the other
	int cdef;
	
	@Override
	public void beginContact(Contact contact) {
		fixA = contact.getFixtureA();
		fixB = contact.getFixtureB();
		cdef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
//		if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
//			head = fixA.getUserData() == "head"? fixA:fixB;
//			object = head == fixA? fixB:fixA;
//			//if head hits coin or brick, return onHeadHit()	
//			if(object.getUserData() instanceof InteractiveTileObject){
//				((InteractiveTileObject)object.getUserData()).onHeadHit();
//			}
//		}
		//define what will happen when colliding
		switch(cdef){
		case Jumper.ENEMY_HEAD_BIT | Jumper.MARIO_BIT:
			if(fixA.getFilterData().categoryBits == Jumper.ENEMY_HEAD_BIT){
				((Enemy) fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
			}else{
				((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
			}
			break;
		case Jumper.ENEMY_BIT | Jumper.OBJECT_BIT:
			if(fixB.getFilterData().categoryBits == Jumper.ENEMY_BIT){
				((Enemy) fixB.getUserData()).reverseVelocity(true, false);
			}else{
				((Enemy) fixA.getUserData()).reverseVelocity(true, false);
			}
			break;
		case Jumper.ENEMY_BIT | Jumper.BRICK_BIT:
			if(fixB.getFilterData().categoryBits == Jumper.ENEMY_BIT){
				((Enemy) fixB.getUserData()).reverseVelocity(true, false);
			}else{
				((Enemy) fixA.getUserData()).reverseVelocity(true, false);
			}
			break;
		case Jumper.MARIO_BIT | Jumper.ENEMY_BIT:
			if(fixA.getFilterData().categoryBits == Jumper.MARIO_BIT){
				((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
			}else{
				((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
			}
			break;
		case Jumper.ENEMY_BIT | Jumper.ENEMY_BIT:
			((Enemy) fixB.getUserData()).hitByEnemy((Enemy) fixA.getUserData());
			((Enemy) fixA.getUserData()).hitByEnemy((Enemy) fixB.getUserData());
			break;
		case Jumper.ITEM_BIT | Jumper.OBJECT_BIT:
			if(fixB.getFilterData().categoryBits == Jumper.ITEM_BIT){
				((Item) fixB.getUserData()).reverseVelocity(true, false);
			}else{
				((Item) fixA.getUserData()).reverseVelocity(true, false);
			}
			break;
		case Jumper.ITEM_BIT | Jumper.MARIO_BIT:
			if(fixB.getFilterData().categoryBits == Jumper.ITEM_BIT){
				((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
			}else{
				((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
			}
			break;
		case Jumper.MARIO_HEAD_BIT | Jumper.BRICK_BIT:
		case Jumper.MARIO_HEAD_BIT | Jumper.COIN_BIT:
			if(fixA.getFilterData().categoryBits == Jumper.MARIO_HEAD_BIT){
				((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
			}else{
				((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
			}
			break;
		case Jumper.ENEMY_BIT | Jumper.WALL_BIT:
			if(fixB.getFilterData().categoryBits == Jumper.ENEMY_BIT){
				((Enemy) fixB.getUserData()).reverseVelocity(true, false);
			}else{
				((Enemy) fixA.getUserData()).reverseVelocity(true, false);
			}
			break;


		
		}
	}


	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
