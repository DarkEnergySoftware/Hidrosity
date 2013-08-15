package com.des.hidrosity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.des.hidrosity.constants.EnemyConstants;
import com.des.hidrosity.player.Player;
import com.jakehorsfield.libld.Utils;

public class StationaryEnemy extends Enemy {

	private long lastTimeShot;
	
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
		
	}
	
	private boolean shouldShoot() {
		if (TimeUtils.millis() - lastTimeShot > EnemyConstants.SHOOT_DELAY + MathUtils.random(-1000, 1000)) {
			return true;
		}
		
		return false;
	}

}
