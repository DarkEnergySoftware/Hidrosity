package com.des.hidrosity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.des.hidrosity.audio.MusicManager;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.interfaces.Menu;
import com.des.hidrosity.screens.menus.MainMenuScreen;
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
				Utils.loadTexture("res/menus/character menu/back.png")
		};
	}

	public void show() {
		Gdx.input.setInputProcessor(new CharacterInputProcessor());
	}
	
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

	public void hide() {
		MusicManager.MENU_MUSIC.stop();
		Gdx.input.setInputProcessor(null);
	}
	
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void dispose() {}

	public void itemSelected() {
		if (backTextureSelected()) {
			changeScreen(new MainMenuScreen());
		} else {
			changeScreen(new LevelSelectScreen());
		}
	}
	
	private boolean backTextureSelected() {
		if (currentTexture == menuTextures[3]) {
			return true;
		}
		
		return false;
	}
	
	class CharacterInputProcessor implements InputProcessor {

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

		public boolean keyUp(int keycode) {
			return false;
		}

		public boolean keyTyped(char character) {
			return false;
		}

		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			return false;
		}

		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		public boolean scrolled(int amount) {
			return false;
		}
		
	}

}
