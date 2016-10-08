package com.ivicaivancic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class Pticica extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;

	Texture gameover;

	Texture[] pticice;
    int flapstate = 0;
	float pticaY = 0;
	float brzina = 0;
	Circle birdCircle;
	int score;
	int scoringTube = 0;
	BitmapFont font;

	int gameState = 0;
	float gravitacija = 2;

	Texture topTube;
	Texture bottomTube;
	float gap = 1200;
	float maxTubeOffset;

	Random randomGenerator;
	float tubeBrzina = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangels;
	Rectangle[] bottomTubeRectangles;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		gameover = new Texture("over.png");
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		pticice = new Texture[4];
        pticice[0] = new Texture("p1.png");
        pticice[1] = new Texture("p2.png");
        pticice[2] = new Texture("p3.png");
        pticice[3] = new Texture("p4.png");



		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		topTubeRectangels = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];


		startGame();

	}


	public void startGame(){

		pticaY = Gdx.graphics.getHeight() / 2 - pticice[0].getHeight() / 2;

		for (int i = 0; i<numberOfTubes; i++){

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200 );

			tubeX[i] =  Gdx.graphics.getWidth()/2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangels[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}


	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score++;

				Gdx.app.log("Score" , String.valueOf(score));

				if (scoringTube < numberOfTubes - 1){
					scoringTube++;
				}else {
					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()){
				brzina = -30;
			}

			for (int i = 0; i<numberOfTubes; i++) {

				if (tubeX[i] < - topTube.getWidth()){

					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}else {
					tubeX[i] = tubeX[i] - tubeBrzina;


				}


				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangels[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());


			}


			if (pticaY > 0) {

				brzina = brzina + gravitacija;
				pticaY -= brzina;
			}else {
				gameState = 2;
			}



		} else if(gameState == 0){

			if(Gdx.input.justTouched()){

				gameState = 1;
			}
		}else if (gameState == 2){
			 batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2,Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2 );

			if (Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				brzina = 0;
			}

		}

		if (flapstate == 0) {
			flapstate = 1;
		} else if (flapstate == 1) {
			flapstate = 2;
		} else if (flapstate == 2) {
			flapstate = 3;
		} else if (flapstate == 3) {
			flapstate = 0;
		}


		batch.draw(pticice[flapstate], Gdx.graphics.getWidth() / 2 - pticice[flapstate].getWidth() / 2, pticaY);
		font.draw(batch, String.valueOf(score), 100 , 200);
		birdCircle.set(Gdx.graphics.getWidth() / 2, pticaY + pticice[flapstate].getHeight() / 2, pticice[flapstate].getWidth() / 2);


		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++){

			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle, topTubeRectangels[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){

				gameState = 2;

			}
		}
		batch.end();

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		pticice[flapstate].dispose();
	}
}
