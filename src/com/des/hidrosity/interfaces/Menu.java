package com.des.hidrosity.interfaces;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.des.hidrosity.audio.SoundManager;
import com.des.hidrosity.constants.GameConstants;
import com.jakehorsfield.libld.Utils;

public abstract class Menu {

	protected int currentMenuIndex;

	protected Texture[] menuTextures;
	protected Texture currentTexture;

	protected Texture borderTexture;

	protected SpriteBatch spriteBatch;

	protected TweenManager tweenManager;

	protected Sprite startingSprite;

	public Menu(Texture[] menuTextures) {
		this.spriteBatch = new SpriteBatch();
		this.currentMenuIndex = 0;
		this.menuTextures = menuTextures;
		this.currentTexture = menuTextures[currentMenuIndex];
		this.borderTexture = Utils.loadTexture("res/menus/border.png");
		this.tweenManager = new TweenManager();

		startingSprite = new Sprite(menuTextures[0]);
		startingSprite.setBounds(Gdx.graphics.getWidth() / 2f
				- GameConstants.WIDTH / 2f, Gdx.graphics.getHeight() / 2f
				- GameConstants.HEIGHT / 2f, GameConstants.WIDTH,
				GameConstants.HEIGHT);
	}

	protected void moveUp() {
		if (notAlreadyAtTop()) {
			currentMenuIndex--;
			updateMenuTexture();
			playBeepSound();
		}
	}

	private boolean notAlreadyAtTop() {
		return currentMenuIndex > 0;
	}

	protected void moveDown() {
		if (notAlreadyAtBottom()) {
			currentMenuIndex++;
			updateMenuTexture();
			playBeepSound();
		}
	}

	private boolean notAlreadyAtBottom() {
		return currentMenuIndex < menuTextures.length - 1;
	}

	private void playBeepSound() {
		SoundManager.MENU_BEEP_SOUND.play();
	}

	protected void renderBorder(SpriteBatch spriteBatch) {
		spriteBatch.draw(borderTexture, 0, 0);
	}

	protected void renderCurrentMenu(SpriteBatch spriteBatch) {
		updateTween();

		if (atStartTexture()) {
			startingSprite.draw(spriteBatch);
		} else {
			spriteBatch.draw(currentTexture, Gdx.graphics.getWidth() / 2
					- GameConstants.WIDTH / 2, Gdx.graphics.getHeight() / 2
					- GameConstants.HEIGHT / 2, GameConstants.WIDTH,
					GameConstants.HEIGHT);
		}
	}

	private boolean atStartTexture() {
		return currentTexture == menuTextures[0];
	}

	private void updateTween() {
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	protected void changeScreen(Screen theScreen) {
		((Game) Gdx.app.getApplicationListener()).setScreen(theScreen);
	}

	private void updateMenuTexture() {
		currentTexture = menuTextures[currentMenuIndex];
	}

	public abstract void itemSelected();

}
