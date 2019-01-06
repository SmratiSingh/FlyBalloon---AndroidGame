package com.tripod.flyballoon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
//import com.sun.xml.internal.bind.CycleRecoverable;

import java.util.Random;

import static com.badlogic.gdx.Input.Peripheral.Vibrator;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background , topTube , bottomTube, gameover;
	Texture [] birds;
	int flapState=0;
	int gameState=0;
	float velocity = 0, birdY= 0, gravity=2;
	float gap = 700;
	float maxTubeOffset;
	int numberOfTubes =4;
	float [] tubeOffset = new float[numberOfTubes];
	Random randomGenerator;
	int score =0;
	int scoringTube=0;
	BitmapFont font;

	float [] tubeX = new float[numberOfTubes];
	float tubeVelocity=4;

	float distanceBetweenTubes;
	Circle birdCircle ;
//	ShapeRenderer shapeRenderer;
	Rectangle []topTubeRectangle ;
	Rectangle [] bottomTubeRectangle;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg1.png");
		gameover = new Texture("gameove.png");
		birds = new Texture[4];
		birds[0] = new Texture("ballon1.png");
		birds[1] = new Texture("ballon1.png");
		birds[2] = new Texture("ballon1.png");
		birds[3] = new Texture("ballon1.png");

		topTube = new Texture("topcactus.png");
		bottomTube = new Texture("bottom.png");
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];

		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().scale(5);
	//	shapeRenderer = new ShapeRenderer();


		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;

        randomGenerator = new Random();

	startGame();


	}

	public void startGame()
	{
		birdY = Gdx.graphics.getHeight() /2 - birds[0].getHeight()/2;
		//defining height & gap among 4 tubes
		for(int i =0 ; i<numberOfTubes;i++)
		{
			tubeOffset[i] = (randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight()- gap - 200);
			tubeX[i]=Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth()+i*distanceBetweenTubes ;
		}

	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if(gameState==1){

			if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2)
			{
				score++;

				Gdx.app.log("Score = ",String.valueOf(score));
				if(scoringTube< numberOfTubes-1)
				{
					scoringTube++;
				}
				else {
					scoringTube = 0;
				}

			}



			for(int i =0 ; i<numberOfTubes;i++)
			{

				if(tubeX[i]< - topTube.getWidth())
				{

					tubeX[i] = tubeX[i] + numberOfTubes* distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight()- gap - 200);
				}
				else
				{
					tubeX[i]=tubeX[i]-tubeVelocity;
				}


				batch.draw(topTube,tubeX[i],Gdx.graphics.getHeight()/2 +gap/2 +tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight()+tubeOffset[i]);

				topTubeRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 +gap/2 +tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight()+tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

				if(Intersector.overlaps(birdCircle,topTubeRectangle[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangle[i]))
				{
					gameState =2;
					Gdx.input.vibrate(new long[] { 0, 200, 200, 200}, -1);


				}



			}



			if(Gdx.input.justTouched())
			{
				velocity=-20;



			}


			// above bottom or if we tap
			if(birdY>0 ) {
				velocity = velocity + gravity; //
				birdY = birdY - velocity;
				//	Gdx.app.log("valu of birdY   "+birdY, "hi");
				//	Gdx.app.log("valu of velocity  "+velocity, "hi");
			}
			else {
				gameState =2;
			}
		} else if(gameState==0){

			if(Gdx.input.justTouched())
			{
				gameState = 1;



			}
		}
		else if(gameState==2)
		{
			batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2- gameover.getHeight()/2);

			if(Gdx.input.justTouched())
			{
				if(Gdx.input.justTouched())
				{
					gameState = 1;
					startGame();
					score =0;
					scoringTube=0;
					velocity = 0;
				}



			}
		}





		 if(flapState==0)
		 {
			 flapState =1;
		 }
		 if(flapState==1)
		 { flapState = 2;}
		 if (flapState == 2)
		 {flapState =3 ;}


		else {
			 flapState = 0;
		 }


		batch.draw(birds[flapState],Gdx.graphics.getWidth() /2-birds[flapState].getWidth()/2 , birdY);

		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+ birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

	//	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	//	shapeRenderer.setColor(Color.BLUE);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for(int i =0 ; i<numberOfTubes;i++) {

		//	shapeRenderer.setColor(Color.BROWN);
		//	shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 +gap/2 +tubeOffset[i],topTube.getWidth(),topTube.getHeight());
		//	shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight()+tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());







		}
	//	shapeRenderer.end();




	}


}
