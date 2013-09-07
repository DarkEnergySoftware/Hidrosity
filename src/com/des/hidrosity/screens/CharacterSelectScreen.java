package com.des.hidrosity.screens;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.des.hidrosity.audio.MusicManager;
import com.des.hidrosity.characters.CharacterManager;
import com.des.hidrosity.characters.Jetten;
import com.des.hidrosity.characters.TheHero;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.interfaces.Menu;
import com.des.hidrosity.screens.menus.MainMenuScreen;
import com.des.hidrosity.tweens.SpriteTweenAccessor;
import com.jakehorsfield.libld.Utils;

public class CharacterSelectScreen extends Menu implements Screen {

	public CharacterSelectScreen() {
		super(loadTextures());
	}

	private static Texture[] loadTextures() {
		return new Texture[] {
				Utils.loadTexture("res/menus/character menu/heroSelected.png"),
				Utils.loadTexture("res/menus/character menu/jettenSelected.png"),
				Utils.loadTexture("res/menus/character menu/start.png"),
				Utils.loadTexture("res/menus/character menu/back.png") };
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new CharacterInputProcessor());
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
		MusicManager.MENU_MUSIC.stop();
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
		if (backTextureSelected()) {
			changeScreen(new MainMenuScreen());
		} else if (heroTextureSelected()) {
			CharacterManager.setCharacter(new TheHero());
			changeScreen(new LevelSelectScreen());
		} else if (jettenTextureSelected()) {
			CharacterManager.setCharacter(new Jetten());
			changeScreen(new LevelSelectScreen());
		}
	}

	private boolean jettenTextureSelected() {
		if (currentTexture == menuTextures[1]) {
			return true;
		}

		return false;
	}

	private boolean heroTextureSelected() {
		if (currentTexture == menuTextures[0]) {
			return true;
		}

		return false;
	}

	private boolean backTextureSelected() {
		if (currentTexture == menuTextures[3]) {
			return true;
		}

		return false;
	}

	class CharacterInputProcessor implements InputProcessor {

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
