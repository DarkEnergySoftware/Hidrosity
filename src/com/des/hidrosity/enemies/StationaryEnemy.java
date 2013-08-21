package com.des.hidrosity.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.des.hidrosity.bullets.StationaryEnemyBullet;
import com.des.hidrosity.constants.EnemyConstants;
import com.des.hidrosity.player.Player;
import com.des.hidrosity.screens.GameScreen;
import com.des.hidrosity.ui.EnemyHealthBar;
import com.jakehorsfield.libld.Utils;

public class StationaryEnemy extends Enemy {

	private long lastTimeShot;

	private Array<StationaryEnemyBullet> bullets = new Array<StationaryEnemyBullet>();
	
	private EnemyHealthBar healthBar;
	
	private boolean shooting = false;
	private long timeStartedShooting;

	public StationaryEnemy(Vector2 position, String textureName, Player player) {
		super(position, textureName, player);
		loadTextures();
		
		healthBar = new EnemyHealthBar(this);
	}
	
	private void loadTextures() {
		leftTexture = Utils.loadTexture("res/enemies/stationary enemy/left.png");
		rightTexture = Utils.loadTexture("res/enemies/stationary enemy/right.png");
		shootLeftTexture = Utils.loadTexture("res/enemies/stationary enemy/shootLeft.png");
		shootRightTexture = Utils.loadTexture("res/enemies/stationary enemy/shootRight.png");
	}

	public void update(float delta) {
		super.update(delta);

		updateShooting();
	}
	
	private void updateShooting() {
		if (inRangeOfPlayer() && shouldShoot()) {
			shoot();
		}
		
		checkIfShouldStopShooting();
	}
	
	private void checkIfShouldStopShooting() {
		if (TimeUtils.millis() - timeStartedShooting > EnemyConstants.SHOOT_TIME) {
			shooting = false;
		}
	}
	
	protected void facePlayer() {
		if (shooting) {
			return;
		}
		
		super.facePlayer();
	}

	private void shoot() {
		if (currentDirection == Direction.Left) {
			shootBulletFromLeft();
		} else {
			shootBulletFromRight();
		}

		shooting = true;
		lastTimeShot = TimeUtils.millis();
		timeStartedShooting = TimeUtils.millis();
	}

	private boolean inRangeOfPlayer() {
		float distanceToPlayer = getDistanceToPlayer();

		if (distanceToPlayer < EnemyConstants.SHOOT_RANGE) {
			return true;
		}

		return false;
	}

	private float getDistanceToPlayer() {
		return (float) Math.sqrt(Math.pow(getX() - player.getX(), 2) + Math.pow(getY() - player.getY(), 2));
	}

	private void shootBulletFromRight() {
		StationaryEnemyBullet b = new StationaryEnemyBullet(new Vector2(getX() + getWidth(), getY()),
				"res/bullets/jettenBullet.png", 1, GameScreen.physicsWorld);
		bullets.add(b);
		setTexture(shootRightTexture);
	}

	private void shootBulletFromLeft() {
		StationaryEnemyBullet b = new StationaryEnemyBullet(new Vector2(getX() - getWidth(), getY() + 2),
				"res/bullets/jettenBullet.png", -1, GameScreen.physicsWorld);
		bullets.add(b);
		setTexture(shootLeftTexture);
	}

	private boolean shouldShoot() {
		if (TimeUtils.millis() - lastTimeShot > EnemyConstants.SHOOT_DELAY + MathUtils.random(-1000, 1000)) {
			return true;
		}

		return false;
	}

	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
		
		renderBullets(spriteBatch);
		renderHealthBar(spriteBatch);
	}
	
	private void renderHealthBar(SpriteBatch spriteBatch) {
		healthBar.render(spriteBatch);
	}

	private void renderBullets(SpriteBatch spriteBatch) {
		for (StationaryEnemyBullet b : bullets) {
			b.render(spriteBatch);
		}
	}

	public void hitByBullet() {
		health -= 5;
	}

	public void prepareForRemoval() {
		for (StationaryEnemyBullet seb : bullets) {
			GameScreen.physicsWorld.destroyBody(seb.getBody());
		}
		
		bullets.clear();
	}
}
