package com.racegame.tbvm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;


public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	Texture px;
	Texture leftImg, rightImg, Car, Obj, wall, carImg, StartButImg, RestartButImg;
	ShapeRenderer box;
	Rectangle buttonL, buttonR, MainCar, click;
	Rectangle StartB, RestartB;

	Array<Rectangle> Cars; //TODO Spawn carObj func X, score X, start menu, restart menu, pause & resume button
	BitmapFont font;

	Vector3 touchPos;
	public long lastDropTime;
	public long DropTimeFrequency = 0;//999_000_000;
	public long speed = 0;//125;
	long score;
	int level = 1;
	int lives = 6;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,1280, 720);
		px = new Texture("pixel.png");
		leftImg = new Texture("Left.png");
		rightImg = new Texture("Right.png");
		//Car = new Texture("Car.png");
		Obj = new Texture("RedCar.png");
		wall = new Texture("Wall.png");
		carImg = new Texture("Car.png");
		StartButImg = new Texture("Start.png");
		RestartButImg = new Texture("Restart.png");

		box = new ShapeRenderer();

		buttonL = new Rectangle(); //Buttons for car movement
		buttonR = new Rectangle();
		buttonL.width = buttonR.width =  70;
		buttonL.height = buttonR.height = 720;
		buttonL.x = 2000;//0;
		buttonL.y = 2000;//0;
		buttonR.x = 2000;//1280-70;
		buttonR.y = 2000;//0; A

		MainCar = new Rectangle();
		MainCar.width = 80;
		MainCar.height = 120;
		MainCar.x = 2000;//1280/2 + 20;
		MainCar.y = 2000;//8; A

		click = new Rectangle(); //Rectangle what is placed on click
		click.width = click.height = 1;

		Cars = new Array<Rectangle>();
		//spawnCar();

		StartB = new Rectangle();
		RestartB = new Rectangle();

		StartB.width = RestartB.width = 300;
		StartB.height = RestartB.height = 70;

		StartB.x = 500;
		StartB.y = 200;

		RestartB.x = 2000;
		RestartB.y = 2000;




	}


	public void Start() {
		DropTimeFrequency = 999_000_000;
		speed = 125;
		lives = 6;

		buttonL.x = 0;
		buttonL.y = 0;
		buttonR.x = 1280-70;
		buttonR.y = 0;

		MainCar.x = 1280/2 + 20;
		MainCar.y = 8;

		StartB.x = RestartB.x = 2000;
		StartB.y = RestartB.y = 2000;

		spawnCar();
	}

	public void spawnCar() {
		Rectangle Rcar = new Rectangle();
		Rcar.width = 50;
		Rcar.height = 100;
		int[] coordForX = new int[]{285, 910, 425, 545, 660, 780};
		int result = MathUtils.random(0, coordForX.length-1);
		Rcar.x = coordForX[result];
		Rcar.y = 800;
		Cars.add(Rcar);
		lastDropTime = TimeUtils.nanoTime();
	}
	public void LevelSetup() {
		speed += 25;
		DropTimeFrequency -= 9_000_000;
		level++;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(batch, "Score: " + score, 1100, 710);
		font.draw(batch, "Level: " + level, 1100, 680);
		font.draw(batch, "Lives: " + lives, 1100, 650);
		batch.draw(wall, 1280/2 - 710/2, 0);
		batch.draw(leftImg, buttonL.x, buttonL.y);
		batch.draw(rightImg, buttonR.x, buttonR.y);
		batch.draw(carImg, MainCar.x, MainCar.y);
		batch.draw(RestartButImg, RestartB.x, RestartB.y);
		batch.draw(StartButImg, StartB.x, StartB.y);

		for(Rectangle Redcar : Cars) {
				batch.draw(Obj, Redcar.x, Redcar.y);
		}

		batch.end();

		for(Rectangle i : Cars) {
			if(i.overlaps(MainCar)) {
				if(lives == 1) {
					speed = 0;
					Cars.clear();
					RestartB.x = 500;
					RestartB.y = 100;
				} //TODO Call Game Over FUNCTION
				lives--;
				Cars.clear();
				spawnCar();
			}
		}

		if(MainCar.x < 285 ) MainCar.x = 285;
		if(MainCar.x > 915) MainCar.x = 915;

		if(DropTimeFrequency < 10_000) DropTimeFrequency = 10_000;

		if(TimeUtils.nanoTime() - lastDropTime > DropTimeFrequency) spawnCar();



		if(Gdx.input.isTouched()) {
			touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			click.x = touchPos.x;
			click.y = touchPos.y;

			if(StartB.overlaps(click)) {
				Start();
			}
			if(RestartB.overlaps(click)) {
				Start();
			}

			if(buttonL.overlaps(click)) {
				MainCar.x -= 5;
			}
			if(buttonR.overlaps(click)) {
				MainCar.x += 5;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) MainCar.x -= 5 /* Gdx.graphics.getDeltaTime() */ ; //For PC control
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) MainCar.x += 5 /* Gdx.graphics.getDeltaTime() */;

		Iterator<Rectangle> iter = Cars.iterator();

		while(iter.hasNext()) {
			Rectangle machine = iter.next();
			machine.y -= speed * Gdx.graphics.getDeltaTime();
			if (machine.y + 120 < 0) {
				iter.remove();
				score++;
				System.out.println(score); //TEST
				if(score%50 == 0) LevelSetup();
				if(score%150 == 0) lives += 1;
			}
		}


	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
