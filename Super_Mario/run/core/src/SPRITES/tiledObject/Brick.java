package SPRITES.tiledObject;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import MENU.Setting;
import SCENES.Hud;
import SCREEN.PlayScreen;
import SPRITES.Mario;
import run.game.Jumper;

public class Brick extends InteractiveTileObject{

	public Brick(PlayScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(Jumper.BRICK_BIT);
	}
	
	@Override
	public void onHeadHit(Mario mario) {
		// TODO Auto-generated method stub
		if(mario.marioIsBig){
			setCategoryFilter(Jumper.DESTROYED_BIT);
			getCell().setTile(null);
			Hud.addScore(200);
			//sound
			if(Setting.isCheckedSound){
				jumper.manager.get("audio/sounds/breakblock.wav", Sound.class).stop();
			}else{
				jumper.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
			}
		}else{
			//sound
			if(Setting.isCheckedSound){
				jumper.manager.get("audio/sounds/bump.wav", Sound.class).stop();
			}else{
				jumper.manager.get("audio/sounds/bump.wav", Sound.class).play();
			}
		}
//		screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y-16/Jumper.PPM), 
//								Mushroom.class));
	}

}
