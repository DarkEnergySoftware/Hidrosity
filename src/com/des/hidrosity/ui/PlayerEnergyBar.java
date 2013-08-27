package com.des.hidrosity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.des.hidrosity.player.Player;
import com.jakehorsfield.libld.Utils;

public class PlayerEnergyBar {

	private Texture energyBarShellTexture;
	private Texture energyBarTexture;
	private Rectangle energyBarShellRect;
	private Rectangle energyBarRect;

	private Player player;

	public PlayerEnergyBar(Player player) {
		this.player = player;

		loadTexturesAndSetPositions();
	}

	private void loadTexturesAndSetPositions() {
		energyBarTexture = Utils.loadTexture("res/ui/energyBar.png");
		energyBarShellTexture = Utils.loadTexture("res/ui/energyBarShell.png");

		energyBarShellRect = new Rectangle();
		energyBarShellRect.width = energyBarShellTexture.getWidth() * 2;
		energyBarShellRect.height = energyBarShellTexture.getHeight() * 2;
		energyBarShellRect.x = 50;
		energyBarShellRect.y = Gdx.graphics.getHeight()
				- energyBarShellRect.height - 10;

		energyBarRect = new Rectangle();
		energyBarRect.x = energyBarShellRect.x + 8;
		energyBarRect.y = energyBarShellRect.y + 40;
		energyBarRect.width = energyBarTexture.getWidth() * 2;
		energyBarRect.height = energyBarTexture.getHeight() * 2;
	}

	public void render(SpriteBatch spriteBatch) {
		renderShell(spriteBatch);
		renderBar(spriteBatch);
	}

	private void renderBar(SpriteBatch spriteBatch) {
		for (int i = 0; i < player.getEnergy(); i++) {
			spriteBatch.draw(energyBarTexture, energyBarRect.x, energyBarRect.y
					+ i * energyBarRect.height * 2, energyBarRect.width,
					energyBarRect.height);
		}
	}

	private void renderShell(SpriteBatch spriteBatch) {
		spriteBatch.draw(energyBarShellTexture, energyBarShellRect.x,
				energyBarShellRect.y, energyBarShellRect.width,
				energyBarShellRect.height);
	}
}
