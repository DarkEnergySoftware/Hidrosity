package com.des.hidrosity.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.des.hidrosity.constants.CollisionConstants;

public class PlayerBullet extends Bullet {

	public PlayerBullet(Vector2 position, String textureName, int direction, World gameWorld) {
		super(position, textureName, direction, gameWorld);
		
		fixture.setUserData(this);
	}
}
