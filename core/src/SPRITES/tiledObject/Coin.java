package SPRITES.tiledObject;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

import MENU.Setting;
import SCENES.Hud;
import SCREEN.PlayScreen;
import SPRITES.Mario;
import SPRITES.items.ItemDef;
import SPRITES.items.Mushroom;
import run.game.Jumper;

public class Coin extends InteractiveTileObject{
	TiledMapTileSet tileset;
	int BLANK_COIN = 94;
	
	public Coin(PlayScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(Jumper.COIN_BIT);
		tileset = map.getTileSets().getTileSet("NES - Super Mario Bros - Tileset");
		
	}

	@Override
	public void onHeadHit(Mario mario) {
		// TODO Auto-generated method stub
//		Gdx.app.log("coin", "message");
		
		handle();
	}
	//change coin image and sounds
	public void handle(){
		if(getCell().getTile().getId() != BLANK_COIN){			
			if(object.getProperties().containsKey("mushroom")){
				screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y+16/Jumper.PPM), 
						Mushroom.class));
				if(Setting.isCheckedSound){
					jumper.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).stop();
				}else{
					jumper.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
				}
				Hud.addScore(100);
			}else{
				if(Setting.isCheckedSound){
					jumper.manager.get("audio/sounds/coin.wav", Sound.class).stop();
				}else{
					jumper.manager.get("audio/sounds/coin.wav", Sound.class).play();
				}
				Hud.addScore(300);
			}
			
		}else{
			if(Setting.isCheckedSound){
				jumper.manager.get("audio/sounds/bump.wav", Sound.class).stop();
			}else{
				jumper.manager.get("audio/sounds/bump.wav", Sound.class).play();
			}
			
		}
		getCell().setTile(tileset.getTile(BLANK_COIN));
	}

}
