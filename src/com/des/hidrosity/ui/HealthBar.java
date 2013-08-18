package com.des.hidrosity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.des.hidrosity.player.Player;
import com.jakehorsfield.libld.Utils;

public class HealthBar {

	private Texture healthBarShellTexture;
	private Texture healthBarTexture;
	private Rectangle healthBarShellRect;
	private Rectangle healthBarRect;

	private Player player;

	public HealthBar(Player player) {
		this.player = player;

		loadTexturesAndSetPositions();
	}

	private void loadTexturesAndSetPositions() {
		healthBarTexture = Utils.loadTexture("res/ui/healthBar.png");
		healthBarShellTexture = Utils.loadTexture("res/ui/healthBarShell.png");

		healthBarShellRect = new Rectangle();
		healthBarShellRect.width = healthBarShellTexture.getWidth() * 2;
		healthBarShellRect.height = healthBarShellTexture.getHeight() * 2;
		healthBarShellRect.x = 10;
		healthBarShellRect.y = Gdx.graphics.getHeight() - healthBarShellRect.height;

		healthBarRect = new Rectangle();
		healthBarRect.x = healthBarShellRect.x + 8;
		healthBarRect.y = healthBarShellRect.y + 40;
		healthBarRect.width = healthBarTexture.getWidth() * 2;
		healthBarRect.height = healthBarTexture.getHeight() * 2;
	}

	public void render(SpriteBatch spriteBatch) {
		renderShell(spriteBatch);
		renderBar(spriteBatch);
	}

	private void renderBar(SpriteBatch spriteBatch) {
		spriteBatch.draw(healthBarTexture, healthBarRect.x, healthBarRect.y, healthBarRect.width, player.getHealth());
	}

	private void renderShell(SpriteBatch spriteBatch) {
		spriteBatch.draw(healthBarShellTexture, healthBarShellRect.x, healthBarShellRect.y,
				healthBarShellRect.width, healthBarShellRect.height);
	}
}
