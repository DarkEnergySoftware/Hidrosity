package com.des.hidrosity.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.des.hidrosity.bullets.StationaryEnemyBullet;
import com.des.hidrosity.constants.EnemyConstants;
import com.des.hidrosity.player.Player;
import com.des.hidrosity.screens.PlayScreen;
import com.jakehorsfield.libld.Utils;

public class StationaryEnemy extends Enemy {

	private long lastTimeShot;
	
	private Array<StationaryEnemyBullet> bullets = new Array<StationaryEnemyBullet>();
	
	public StationaryEnemy(Vector2 position, String textureName, Player player) {
		super(position, textureName, player);
		
		leftTexture = Utils.loadTexture("res/enemies/stationary enemy/left.png");
		rightTexture = Utils.loadTexture("res/enemies/stationary enemy/right.png");
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (shouldShoot()) {
			shoot();
		}
	}
	
	private void shoot() {
		if (currentDirection == Direction.Left) {
			shootBulletFromLeft();
		} else {
			shootBulletFromRight();
		}
		
		lastTimeShot = TimeUtils.millis();
	}
	
	private void shootBulletFromRight() {
		StationaryEnemyBullet b = new StationaryEnemyBullet(new Vector2(getX() + getWidth(), getY() + getHeight() / 2), "res/bullets/jettenBullet.png", 1, PlayScreen.physicsWorld);
		bullets.add(b);
	}
	
	private void shootBulletFromLeft() {
		StationaryEnemyBullet b = new StationaryEnemyBullet(new Vector2(getX(), getY() + getHeight() / 2), "res/bullets/jettenBullet.png", -1, PlayScreen.physicsWorld);
		bullets.add(b);
	}
	
	private boolean shouldShoot() {
		if (TimeUtils.millis() - lastTimeShot > EnemyConstants.SHOOT_DELAY + MathUtils.random(-1000, 1000)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
		
		renderBullets(spriteBatch);
	}
	
	private void renderBullets(SpriteBatch spriteBatch) {
		for (StationaryEnemyBullet b : bullets) {
			b.render(spriteBatch);
		}
	}

}
