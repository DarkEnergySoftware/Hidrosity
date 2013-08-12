package com.des.hidrosity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.des.hidrosity.collisions.CollisionListener;
import com.des.hidrosity.constants.GameConstants;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.levels.LevelManager;
import com.des.hidrosity.player.Player;

public class PlayScreen implements Screen {

	private SpriteBatch spriteBatch;
	private OrthographicCamera camera;

	private World physicsWorld;
	private Box2DDebugRenderer debugRenderer;

	private LevelManager levelManager;

	private Player player;

	public void show() {
		setupRenderingStuff();
		createPhysicsWorld();
		createLevelManager();
		createPlayer();
	}

	private void createPlayer() {
		player = new Player(new Vector2(32 + GameConstants.X_OFFSET, 130 + GameConstants.Y_OFFSET),
				"res/player/the hero/standing/right1.png", physicsWorld);
	}

	private void createLevelManager() {
		levelManager = new LevelManager(physicsWorld, LevelSelectScreen.chosenLevel);
	}

	private void createPhysicsWorld() {
		physicsWorld = new World(GameConstants.GRAVITY, true);
		physicsWorld.setContactListener(new CollisionListener());
	}

	private void setupRenderingStuff() {
		spriteBatch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.update();

		debugRenderer = new Box2DDebugRenderer();
	}

	private void handleInput() {
		handlePlayerInput();
		handleCameraInput();
	}
	
	private void handleCameraInput() {
		if (Gdx.input.isKeyPressed(Keys.PLUS)) {
			camera.zoom -= 0.01f;
			camera.update();
		} else if (Gdx.input.isKeyPressed(Keys.MINUS)) {
			camera.zoom += 0.01f;
			camera.update();
		}
	}
	
	private void handlePlayerInput() {
		if (Gdx.input.isKeyPressed(KeyConstants.PLAYER_LEFT)) {
			player.moveLeft();
		} else if (Gdx.input.isKeyPressed(KeyConstants.PLAYER_RIGHT)) {
			player.moveRight();
		}
		
		if (Gdx.input.isKeyPressed(KeyConstants.PLAYER_JUMP)) {
			player.jump();
		}
	}

	private void update() {
		updatePlayer();
		updatePhysicsWorld();
		updateCameraPosition();
	}
	
	private void updateCameraPosition() {
		camera.position.x = player.getX();
		camera.update();
	}

	private void updatePhysicsWorld() {
		physicsWorld.step(1 / 60f, 8, 3);
	}
	
	private void updatePlayer() {
		player.update(Gdx.graphics.getDeltaTime());
	}

	public void render(float delta) {
		handleInput();
		update();

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		{
			renderLevel();
			renderPlayer();
		}
		spriteBatch.end();

		debugRenderer.render(physicsWorld, camera.projection);
	}
	
	private void renderPlayer() {
		player.render(spriteBatch);
	}

	private void renderLevel() {
		levelManager.renderLevel(spriteBatch);
	}

	public void resize(int width, int height) {
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}

}
