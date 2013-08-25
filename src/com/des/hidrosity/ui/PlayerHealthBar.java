package com.des.hidrosity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.des.hidrosity.player.Player;
import com.jakehorsfield.libld.Utils;

public class PlayerHealthBar {

	private Texture healthBarShellTexture;
	private Texture healthBarTexture;
	private Rectangle healthBarShellRect;
	private Rectangle healthBarRect;

	private Player player;

	public PlayerHealthBar(Player player) {
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
		healthBarShellRect.y = Gdx.graphics.getHeight() - healthBarShellRect.height - 10;

		System.out.println(healthBarShellRect);
		
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
		for (int i = 0; i < player.getHealth(); i++) {
			spriteBatch.draw(healthBarTexture, healthBarRect.x, healthBarRect.y + i * healthBarRect.height * 2,
					healthBarRect.width, healthBarRect.height);
		}
	}

	private void renderShell(SpriteBatch spriteBatch) {
		spriteBatch.draw(healthBarShellTexture, healthBarShellRect.x, healthBarShellRect.y, healthBarShellRect.width,
				healthBarShellRect.height);
	}
}
