package SCENES;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import run.game.Jumper;

public class Hud implements Disposable{
	Viewport viewport;
	public Stage stage;
	Table table, btable;
	
	public Integer worldtimer;
	static Integer score;
	float timercounter;
	
	Label counttimelabel;
	static Label scorelabel;
	Label timerlabel;
	Label levellabel;
	Label worldlabel;
	Label mariolabel;
	
	BitmapFont font;

	boolean timeUp;
	
	public Hud(SpriteBatch batch){		
		font = new BitmapFont(Gdx.files.internal("fonts/Bungee_Inline.fnt"));
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        		
		worldtimer = 300;
		score = 0;
		timercounter = 0;
		
		viewport = new FitViewport(Jumper.screenW,Jumper.screenH,new OrthographicCamera());	
		stage = new Stage(viewport, batch);
		table = new Table();
		table.top();
		table.setFillParent(true);
		
		counttimelabel = new Label(String.format("%03d", worldtimer),style);
		scorelabel = new Label(String.format("%06d", score),style);
		timerlabel = new Label("Time",style);
		levellabel = new Label("1-1",style);
		worldlabel = new Label("World",style);
		mariolabel = new Label("Mario",style);
		//setting labels to table
		//share the same row x
		table.add(mariolabel).expandX().padTop(5);
		table.add(worldlabel).expandX().padTop(5);
		table.add(timerlabel).expandX().padTop(5);
		table.row();//next row
		table.add(scorelabel).expandX();
		table.add(levellabel).expandX();
		table.add(counttimelabel).expandX();
		
		stage.addActor(table);
		
		timeUp = false;
		
	}
	public void update(float dt){
		timercounter +=dt;
		if(timercounter >= 1){
			if(worldtimer > 0){
			worldtimer--;
			}else{ timeUp = true;}
			counttimelabel.setText(String.format("%03d", worldtimer));
			timercounter = 0;
		}
	}
	public static void addScore(int value){
		score += value;
		scorelabel.setText(String.format("%06d", score));
	}


	@Override
	public void dispose() {
		stage.dispose();
		
	}


	
}
