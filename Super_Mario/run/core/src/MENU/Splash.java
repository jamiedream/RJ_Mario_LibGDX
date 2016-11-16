package MENU;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import SCREEN.PlayScreen;
import run.game.Jumper;

public class Splash implements Screen{
	private Texture textureSplash = new Texture("splash.jpg");
	private Image splashimage = new Image(textureSplash);
	private Stage stage;
	int w, h;
	Jumper jumper;
	GameMenu menu;
	PlayScreen screen;

	public Splash(Jumper jumper) {
		this.jumper = jumper;
		//width& height of splash.jpg
		w=1024;
		h=768;
		menu = new GameMenu(jumper);
		jumper.handleMusic();
	}

	@Override
	public void show() {
		stage = new Stage(new StretchViewport(w, h));
		stage.addActor(splashimage);
		
		splashimage.addAction(
				Actions.sequence(
				Actions.alpha(0),
				Actions.fadeIn(3.0f),
				Actions.delay(1),
				Actions.fadeOut(0.5f),
				Actions.run(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						((Game) Gdx.app.getApplicationListener()).setScreen(menu);
					}
				})
				));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		
	}

	@Override
	public void resize(int w, int h) {
		
		
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
		textureSplash.dispose();
		stage.dispose();
	}
	

}
