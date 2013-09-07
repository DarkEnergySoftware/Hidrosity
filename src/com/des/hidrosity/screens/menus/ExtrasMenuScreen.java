package com.des.hidrosity.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.interfaces.Menu;
import com.jakehorsfield.libld.Utils;

public class ExtrasMenuScreen extends Menu implements Screen {

	public ExtrasMenuScreen() {
		super(loadTextures());
	}

	private static Texture[] loadTextures() {
		return new Texture[] {
				Utils.loadTexture("res/menus/extras menu/bossRush.png"),
				Utils.loadTexture("res/menus/extras menu/back.png") };
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new ExtrasInputProcessor());
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
		}
	}

	private boolean backTextureSelected() {
		if (currentTexture == menuTextures[1]) {
			return true;
		}

		return false;
	}

	class ExtrasInputProcessor implements InputProcessor {

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
