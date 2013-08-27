package com.des.hidrosity.screens.menus;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.des.hidrosity.audio.MusicManager;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.interfaces.Menu;
import com.des.hidrosity.screens.CharacterSelectScreen;
import com.des.hidrosity.tweens.SpriteTweenAccessor;
import com.jakehorsfield.libld.Utils;

public class MainMenuScreen extends Menu implements Screen {

	public MainMenuScreen() {
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
				Utils.loadTexture("res/menus/main menu/start.png"),
				Utils.loadTexture("res/menus/main menu/coop.png"),
				Utils.loadTexture("res/menus/main menu/extras.png"),
				Utils.loadTexture("res/menus/main menu/options.png"),
				Utils.loadTexture("res/menus/main menu/dlc.png"),
				Utils.loadTexture("res/menus/main menu/exit.png") };
	}

	@Override
	public void show() {
		MusicManager.MENU_MUSIC.play();
		Gdx.input.setInputProcessor(new MenuInputProcessor());
	}

	@Override
	public void render(float delta) {
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
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void itemSelected() {
		if (startTextureSelected()) {
			changeScreen(new CharacterSelectScreen());
		} else if (exitTextureSelected()) {
			Gdx.app.exit();
		} else if (optionsTextureSelected()) {
			changeScreen(new OptionsMenuScreen());
		} else if (extrasTextureSelected()) {
			changeScreen(new ExtrasMenuScreen());
		}
	}

	private boolean extrasTextureSelected() {
		if (currentTexture == menuTextures[2]) {
			return true;
		}

		return false;
	}

	private boolean optionsTextureSelected() {
		if (currentTexture == menuTextures[3]) {
			return true;
		}

		return false;
	}

	private boolean exitTextureSelected() {
		if (currentTexture == menuTextures[5]) {
			return true;
		}

		return false;
	}

	private boolean startTextureSelected() {
		if (currentTexture == menuTextures[0]) {
			return true;
		}

		return false;
	}

	@Override
	public void dispose() {
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

	class MenuInputProcessor implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case KeyConstants.MENU_UP:
				moveUp();
				break;
			case KeyConstants.MENU_DOWN:
				moveDown();
				break;
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
