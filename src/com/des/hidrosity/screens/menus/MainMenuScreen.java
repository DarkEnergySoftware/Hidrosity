package com.des.hidrosity.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.des.hidrosity.audio.MusicManager;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.interfaces.Menu;
import com.des.hidrosity.screens.CharacterSelectScreen;
import com.jakehorsfield.libld.Utils;

public class MainMenuScreen extends Menu implements Screen {

	public MainMenuScreen() {
		super(loadTextures());
	}
	
	private static Texture[] loadTextures() {
		return new Texture[] {
				Utils.loadTexture("res/menus/main menu/start.png"),
				Utils.loadTexture("res/menus/main menu/coop.png"),
				Utils.loadTexture("res/menus/main menu/extras.png"),
				Utils.loadTexture("res/menus/main menu/options.png"),
				Utils.loadTexture("res/menus/main menu/dlc.png"),
				Utils.loadTexture("res/menus/main menu/exit.png")
		};
	}

	public void show() {
		MusicManager.MENU_MUSIC.play();
		Gdx.input.setInputProcessor(new MenuInputProcessor());
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
		Gdx.input.setInputProcessor(null);
	}
	
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
	
	public void dispose() {}
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	
	class MenuInputProcessor implements InputProcessor {

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
