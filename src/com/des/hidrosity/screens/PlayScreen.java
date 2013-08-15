package com.des.hidrosity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.des.hidrosity.collisions.CollisionListener;
import com.des.hidrosity.constants.GameConstants;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.enemies.Enemy;
import com.des.hidrosity.enemies.StationaryEnemy;
import com.des.hidrosity.levels.LevelManager;
import com.des.hidrosity.player.Player;

public class PlayScreen implements Screen {

	private SpriteBatch spriteBatch;
	private OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;

	public static World physicsWorld;

	private LevelManager levelManager;

	private Player player;

	private StationaryEnemy testEnemy;
	private Array<Enemy> enemies = new Array<Enemy>();

	public static Array<Body> bodiesToRemove = new Array<>();

	public void show() {
		setupRenderingStuff();
		createPhysicsWorld();
		createLevelManager();
		createPlayer();
		createEnemies();
	}

	private void createEnemies() {
		testEnemy = new StationaryEnemy(new Vector2(616 * 2 + GameConstants.X_OFFSET, 94 * 2 + GameConstants.Y_OFFSET),
				"res/enemies/stationary enemy/left.png", player);
		enemies.add(testEnemy);
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
			zoomInCamera();
		} else if (Gdx.input.isKeyPressed(Keys.MINUS)) {
			zoomOutCamera();
		}
	}

	private void zoomOutCamera() {
		camera.zoom += 0.005f;
		camera.update();
	}

	private void zoomInCamera() {
		camera.zoom -= 0.005f;
		camera.update();
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

		if (Gdx.input.isKeyPressed(KeyConstants.PLAYER_SHOOT)) {
			player.shoot();
		}
	}

	private void update() {
		updatePlayer();
		updateEnemies();
		updatePhysicsWorld();
		updateCameraPosition();
		removeBodies();
	}

	private void updateEnemies() {
		for (Enemy e : enemies) {
			e.update(Gdx.graphics.getDeltaTime());
		}
	}

	private void removeBodies() {
	}

	private void updateCameraPosition() {
		if (playerWithinCameraBounds()) {
			cameraFollowPlayer();
		}
	}

	private void cameraFollowPlayer() {
		camera.position.x = player.getX();
		camera.update();
	}

	private boolean playerWithinCameraBounds() {
		if (player.getX() > 0f && player.getX() < levelManager.getLevelWidth() - player.getWidth() - GameConstants.X_OFFSET) {
			return true;
		}

		return false;
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
			renderEnemies();
		}
		spriteBatch.end();

		renderDebugWorld();
	}

	private void renderDebugWorld() {
		debugRenderer.render(physicsWorld, camera.projection);
	}

	private void renderEnemies() {
		for (Enemy e : enemies) {
			e.render(spriteBatch);
		}
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
