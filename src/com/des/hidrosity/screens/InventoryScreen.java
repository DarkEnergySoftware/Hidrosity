package com.des.hidrosity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jakehorsfield.libld.Utils;

public class InventoryScreen {

	private Texture texture;

	private boolean enabled = false;

	public InventoryScreen() {
		texture = Utils.loadTexture("res/ui/inventoryScreen.png");
	}

	public void show() {
		enabled = true;
	}

	public void hide() {
		enabled = false;
	}

	public void render(SpriteBatch spriteBatch) {
		if (enabled) {
			spriteBatch.draw(texture, Gdx.graphics.getWidth() / 2 - texture.getWidth() / 2, Gdx.graphics.getHeight()
					/ 2 - texture.getHeight() / 2);
		}
	}

}
