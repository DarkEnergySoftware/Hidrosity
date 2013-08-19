package com.des.hidrosity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
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
import com.des.hidrosity.ui.PlayerEnergyBar;
import com.des.hidrosity.ui.PlayerHealthBar;

public class PlayScreen implements Screen {

	private SpriteBatch spriteBatch;
	private SpriteBatch uiBatch;
	private OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;

	public static World physicsWorld;

	private LevelManager levelManager;

	private Player player;

	private StationaryEnemy testEnemy;
	private StationaryEnemy testEnemy2;
	private Array<Enemy> enemies = new Array<Enemy>();
	
	private PlayerHealthBar healthBar;
	private PlayerEnergyBar energyBar;

	public static Array<Body> bodiesToRemove = new Array<>();
	
	private FPSLogger fpsLogger;
	
	public void show() {
		setupRenderingStuff();
		createPhysicsWorld();
		createLevelManager();
		createPlayer();
		createEnemies();
		createUi();
		
		fpsLogger = new FPSLogger();
	}
	
	private void createUi() {
		healthBar = new PlayerHealthBar(player);
		energyBar = new PlayerEnergyBar(player);
	}

	private void createEnemies() {
		testEnemy = new StationaryEnemy(new Vector2(616 * 2 + GameConstants.X_OFFSET, 94 * 2 + GameConstants.Y_OFFSET),
				"res/enemies/stationary enemy/left.png", player);
		testEnemy2 = new StationaryEnemy(new Vector2(1616 * 2 + GameConstants.X_OFFSET, 94 * 2 + GameConstants.Y_OFFSET),
				"res/enemies/stationary enemy/left.png", player);
		enemies.add(testEnemy);
		enemies.add(testEnemy2);
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
		uiBatch = new SpriteBatch();

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
		checkIfPlayerDead();
		updateEnemies();
		updatePhysicsWorld();
		updateCameraPosition();
		removeBodies();
	}

	private void checkIfPlayerDead() {
		if (player.getHealth() <= 0) {
			((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen());
		}
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
		if (player.getX() > 0f
				&& player.getX() < levelManager.getLevelWidth() - player.getWidth() * 2 - GameConstants.X_OFFSET * 2) {
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

		renderGame();
		renderUi();

		renderDebugWorld();
		
		fpsLogger.log();
	}

	private void renderUi() {
		uiBatch.begin();
		{
			renderHealthBar();
			renderEnergyBar();
		}
		uiBatch.end();
	}
	
	private void renderEnergyBar() {
		energyBar.render(uiBatch);
	}

	private void renderHealthBar() {
		healthBar.render(uiBatch);
	}

	private void renderGame() {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		{
			renderLevel();
			renderPlayer();
			renderEnemies();
		}
		spriteBatch.end();
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
