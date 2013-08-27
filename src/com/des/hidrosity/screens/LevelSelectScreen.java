package com.des.hidrosity.screens;

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
import com.des.hidrosity.tweens.SpriteTweenAccessor;
import com.jakehorsfield.libld.Utils;

public class LevelSelectScreen extends Menu implements Screen {

	public static int chosenLevel;

	public LevelSelectScreen() {
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
		return new Texture[] { Utils.loadTexture("res/menus/level menu/0.png"),
				Utils.loadTexture("res/menus/level menu/1.png"),
				Utils.loadTexture("res/menus/level menu/2.png"),
				Utils.loadTexture("res/menus/level menu/3.png"),
				Utils.loadTexture("res/menus/level menu/4.png"),
				Utils.loadTexture("res/menus/level menu/5.png"),
				Utils.loadTexture("res/menus/level menu/6.png"),
				Utils.loadTexture("res/menus/level menu/7.png"),
				Utils.loadTexture("res/menus/level menu/8.png"),
				Utils.loadTexture("res/menus/level menu/9.png"), };
	}

	@Override
	public void show() {
		MusicManager.LEVEL_SELECT_MUSIC.play();
		Gdx.input.setInputProcessor(new LevelInputProcessor());
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
		MusicManager.LEVEL_SELECT_MUSIC.stop();
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
		chosenLevel = currentMenuIndex;
		changeScreen(new GameScreen());
	}

	class LevelInputProcessor implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case KeyConstants.LEVEL_NEXT:
				moveDown();
				break;
			case KeyConstants.LEVEL_PREVIOUS:
				moveUp();
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
