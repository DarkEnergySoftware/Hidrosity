package com.des.hidrosity.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.des.hidrosity.enemies.Enemy;
import com.jakehorsfield.libld.Utils;

public class EnemyHealthBar {

	private Enemy enemy;

	private Texture texture;

	public EnemyHealthBar(Enemy enemy) {
		this.enemy = enemy;

		texture = Utils.loadTexture("res/ui/enemyHealthBar.png");
	}

	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, enemy.getX(), enemy.getY() + enemy.getHeight() * 2, enemy.getHealth(),
				texture.getHeight() / 4);
	}
}
