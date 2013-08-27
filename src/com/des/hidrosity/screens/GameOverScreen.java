package com.des.hidrosity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jakehorsfield.libld.Utils;

public class GameOverScreen implements Screen {

	private SpriteBatch spriteBatch;
	private Texture texture;

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		texture = Utils.loadTexture("res/menus/game over/texture.png");

		Gdx.input.setInputProcessor(new Input());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		{
			spriteBatch.draw(texture, 0, 0);
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

	class Input implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case Keys.ENTER:
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen());
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
