package MENU;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import run.game.Jumper;

public class Setting implements Screen{
	float w, h;
	float switcher_W, switcher_H;
	Jumper jumper;
	OrthographicCamera camera;
	TextureAtlas atlas;	
	TextureRegion main, topic, function, volume, music, sound, musicon, musicoff, musicnow, musicoppsite,
					soundon, soundoff, soundnow, soundoppsite;
	Stage stage;
	ImageButton imusicon, isoundon, iok, icancel;
	TextureRegionDrawable  musicnowDrawable, musicoppsiteDrawable, soundnowDrawable, soundoppsiteDrawable;
	ImageButtonStyle okDrawable, okhoverDrawable, okcheckDrawable;
	Skin skin;
	public static boolean isChecked, isCheckedSound;
	Viewport viewport;
	public Setting(Jumper jumper) {
		this.jumper = jumper;
		w=jumper.screenW;
		h=jumper.screenH;
		switcher_W = w/3;
		switcher_H = h/6;
		//menu ui
		camera = new OrthographicCamera(w,h);		
		atlas = new TextureAtlas("setting.atlas");
		main = new TextureRegion(atlas.findRegion("bg2"));
		topic = new TextureRegion(atlas.findRegion("topic"));
		function = new TextureRegion(atlas.findRegion("function"));
		volume = new TextureRegion(atlas.findRegion("volume"));
		
		music = new TextureRegion(atlas.findRegion("music"));
		sound = new TextureRegion(atlas.findRegion("sound"));
		
		musicoff = new TextureRegion(atlas.findRegion("musicoff"));
		musicon = new TextureRegion(atlas.findRegion("musicon"));
		musicnow = new TextureRegion(isChecked? musicoff:musicon);
		musicnowDrawable = new TextureRegionDrawable(musicnow);
		musicoppsite = new TextureRegion(!isChecked? musicoff:musicon);
		musicoppsiteDrawable = new TextureRegionDrawable(musicoppsite);
        imusicon = new ImageButton(musicnowDrawable, musicoppsiteDrawable, musicoppsiteDrawable);
        imusicon.setSize(switcher_H/2, switcher_H/2);
        imusicon.setPosition(w/2-switcher_H/2, h/2+switcher_H*0.3f);
        
        soundoff = new TextureRegion(atlas.findRegion("musicoff"));
        soundon = new TextureRegion(atlas.findRegion("musicon"));
        soundnow = new TextureRegion(isCheckedSound? soundoff:soundon);
		soundnowDrawable = new TextureRegionDrawable(soundnow);
		soundoppsite = new TextureRegion(!isCheckedSound? soundoff:soundon);
		soundoppsiteDrawable = new TextureRegionDrawable(soundoppsite);
        isoundon = new ImageButton(soundnowDrawable, soundoppsiteDrawable, soundoppsiteDrawable);
        isoundon.setSize(switcher_H/2, switcher_H/2);
        isoundon.setPosition(w/2-switcher_H/2,h/2-switcher_H*0.9f);
                
        skin = new Skin(atlas);
		okDrawable = new ImageButtonStyle();
		okDrawable.up = skin.getDrawable("ok");  //Set image for not pressed button 
		okDrawable.down = skin.getDrawable("ok_hover");  //Set image for pressed
		okDrawable.over = skin.getDrawable("ok_check");  //set image for mouse over		
        iok = new ImageButton(okDrawable);     
        iok.setSize(switcher_W*0.6f, switcher_H);
        iok.setPosition(w*0.7f, h*0.15f);
		
		stage = new Stage(new StretchViewport(w, h));
		stage.clear();
		stage.addActor(imusicon);
		stage.addActor(isoundon);
		stage.addActor(iok);
		
		
	}


	@Override
	public void show() {		
		imusicon.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {           	
            	isChecked = !isChecked;
                return true;
            }
        });	
		isoundon.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {           	
            	isCheckedSound = !isCheckedSound;
                return true;
            }
        });	
		iok.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	((Game) Gdx.app.getApplicationListener()).setScreen(new GameMenu(jumper));
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {                
            }
        });
		
		//for stage interactive effect
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Jumper.batch.setProjectionMatrix(camera.combined);
		Jumper.batch.begin();
		Jumper.batch.draw(main, -w/2, -h/2, w, h);
		Jumper.batch.draw(function, -w/2, -h/2, w, h*11/12);
		Jumper.batch.draw(volume, -w*5/11, -h*3/7, w*9/10, h*3/4);
		Jumper.batch.draw(topic, 0, h/2-switcher_H*1.5f, switcher_W*1.5f, switcher_H*1.5f);
		Jumper.batch.draw(music, -switcher_W, 0, switcher_W, switcher_H);
		Jumper.batch.draw(sound, -switcher_W, -switcher_H*1.2f, switcher_W, switcher_H);
		Jumper.batch.end();
		
		stage.act();
		stage.draw();
		
//		musicon.setRegion(isChecked? musicoff:musicon);
//		soundon.setRegion(isCheckedSound? soundoff:soundon);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
