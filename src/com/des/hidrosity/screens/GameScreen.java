package com.des.hidrosity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.des.hidrosity.bullets.Bullet;
import com.des.hidrosity.collisions.CollisionListener;
import com.des.hidrosity.constants.GameConstants;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.enemies.Enemy;
import com.des.hidrosity.enemies.StationaryEnemy;
import com.des.hidrosity.levels.LevelManager;
import com.des.hidrosity.player.Player;
import com.des.hidrosity.ui.PlayerEnergyBar;
import com.des.hidrosity.ui.PlayerHealthBar;

public class GameScreen implements Screen {

	private SpriteBatch spriteBatch;
	private SpriteBatch uiBatch;
	private OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;

	public static World physicsWorld;

	private LevelManager levelManager;

	private Player player;

	private PlayerHealthBar playerHealthBar;
	private PlayerEnergyBar playerEnergyBar;

	public static Array<Bullet> bulletsToRemove = new Array<>();
	public static Array<Enemy> enemies = new Array<Enemy>();

	private FPSLogger fpsLogger;

	public void show() {
		setupRenderingStuff();
		createPhysicsWorld();
		createLevelManager();
		createPlayer();
		createEnemies();
		createUi();

		fpsLogger = new FPSLogger();
		Gdx.input.setInputProcessor(new Input());
	}

	private void createUi() {
		playerHealthBar = new PlayerHealthBar(player);
		playerEnergyBar = new PlayerEnergyBar(player);
	}

	private void createEnemies() {
		enemies = levelManager.getEnemiesInLevel(player);
	}

	private void createPlayer() {
		player = new Player(levelManager.getPlayerSpawnPosition(), "res/player/the hero/standing/right1.png",
				physicsWorld);
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

		if (Gdx.input.isKeyPressed(KeyConstants.PLAYER_SHOOT)) {
			player.shoot();
		}
	}

	private void update() {
		updatePlayer();
		checkIfPlayerDead();
		checkIfShouldResetPlayerPos();
		updateEnemies();
		removeDeadEnemies();
		removeBullets();
		updatePhysicsWorld();
		updateCameraPosition();
	}
	
	private void checkIfShouldResetPlayerPos() {
		if (player.getPhysicsBody().getPosition().y <= -2.9150002) {
			Vector2 spawnPosition = new Vector2();
			spawnPosition.x = levelManager.getPlayerSpawnPosition().x * GameConstants.UNIT_SCALE;
			spawnPosition.y = levelManager.getPlayerSpawnPosition().y * GameConstants.UNIT_SCALE;
			
			player.getPhysicsBody().setTransform(spawnPosition, 0f);
		}
	}

	private void removeDeadEnemies() {
		for (Enemy enemy : enemies) {
			if (enemy.dead) {
				physicsWorld.destroyBody(enemy.getBody());
				enemy.prepareForRemoval();
				enemies.removeValue(enemy, true);
			}
		}
	}

	private void removeBullets() {
		for (Bullet bullet : bulletsToRemove) {
			physicsWorld.destroyBody(bullet.getBody());
		}

		bulletsToRemove.clear();
	}

	private void checkIfPlayerDead() {
		if (player.dead) {
			((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen());
		}
	}

	private void updateEnemies() {
		for (Enemy e : enemies) {
			e.update(Gdx.graphics.getDeltaTime());
		}
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
		playerEnergyBar.render(uiBatch);
	}

	private void renderHealthBar() {
		playerHealthBar.render(uiBatch);
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
	
	class Input implements InputProcessor {

		public boolean keyDown(int keycode) {
			switch (keycode) {
			case KeyConstants.PLAYER_JUMP:
				player.jump();
				break;
			default:
				break;
			}
			
			return false;
		}

		public boolean keyUp(int keycode) {
			return false;
		}

		public boolean keyTyped(char character) {
			return false;
		}

		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		public boolean scrolled(int amount) {
			return false;
		}
		
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
