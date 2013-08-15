package com.des.hidrosity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.des.hidrosity.player.Player;
import com.jakehorsfield.libld.Utils;

public class StationaryEnemy extends Enemy {

	public StationaryEnemy(Vector2 position, String textureName, Player player) {
		super(position, textureName, player);
		
		leftTexture = Utils.loadTexture("res/enemies/stationary enemy/left.png");
		rightTexture = Utils.loadTexture("res/enemies/stationary enemy/right.png");
	}

}
