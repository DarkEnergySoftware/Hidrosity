package com.des.hidrosity.screens;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.des.hidrosity.audio.MusicManager;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.interfaces.Menu;
import com.des.hidrosity.screens.menus.MainMenuScreen;
import com.des.hidrosity.tweens.SpriteTweenAccessor;
import com.jakehorsfield.libld.Utils;

public class TitleScreen extends Menu implements Screen {

	private final int IMAGE_CHANGE_DELAY = 500;
	private long lastTimeImageChanged;

	public TitleScreen() {
		super(loadTextures());

		createFadeInTween();
	}

	private void createFadeInTween() {
		Tween.registerAccessor(Sprite.class, new SpriteTweenAccessor());
		Tween.set(startingSprite, SpriteTweenAccessor.ALPHA).target(0f)
				.start(tweenManager);
		Tween.to(startingSprite, SpriteTweenAccessor.ALPHA, 0.8f).target(1)
				.start(tweenManager);
	}

	private static Texture[] loadTextures() {
		return new Texture[] {
				Utils.loadTexture("res/menus/title menu/image1.png"),
				Utils.loadTexture("res/menus/title menu/image2.png") };
	}

	@Override
	public void show() {
		MusicManager.TITLE_MUSIC.play();
		Gdx.input.setInputProcessor(new TitleInputProcessor());
	}

	private void update(float delta) {
		if (imageChangeDelayDone()) {
			changeImage();
		}
	}

	private void changeImage() {
		if (currentTexture == menuTextures[0]) {
			currentTexture = menuTextures[1];
		} else {
			currentTexture = menuTextures[0];
		}

		lastTimeImageChanged = TimeUtils.millis();
	}

	private boolean imageChangeDelayDone() {
		if (TimeUtils.millis() - lastTimeImageChanged > IMAGE_CHANGE_DELAY) {
			return true;
		}

		return false;
	}

	@Override
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		{
			renderBorder(spriteBatch);
			renderCurrentMenu(spriteBatch);
		}
		spriteBatch.end();
	}

	@Override
	public void hide() {
		MusicManager.TITLE_MUSIC.stop();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void itemSelected() {
		changeScreen(new MainMenuScreen());
	}

	class TitleInputProcessor implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case KeyConstants.MENU_CONFIRM:
				itemSelected();
				break;
			}
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			return false;
		}

	}
}
