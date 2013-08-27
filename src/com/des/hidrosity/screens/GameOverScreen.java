package com.des.hidrosity.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.des.hidrosity.tweens.SpriteTweenAccessor;
import com.jakehorsfield.libld.Utils;

public class GameOverScreen implements Screen {

	private SpriteBatch spriteBatch;
	private Texture texture;
	
	private Sprite sprite;
	
	private TweenManager tweenManager;

	public void show() {
		spriteBatch = new SpriteBatch();
		texture = Utils.loadTexture("res/menus/game over/texture.png");

		sprite = new Sprite(texture);
		sprite.setBounds(0, 0, texture.getWidth(), texture.getHeight());
		
		tweenManager = new TweenManager();
		
		createFadeInTween();

		Gdx.input.setInputProcessor(new Input());
	}

	private void createFadeInTween() {
		Tween.registerAccessor(Sprite.class, new SpriteTweenAccessor());
		Tween.set(sprite, SpriteTweenAccessor.ALPHA).target(0f)
				.start(tweenManager);
		Tween.to(sprite, SpriteTweenAccessor.ALPHA, 0.8f).target(1)
				.start(tweenManager);
	}

	public void render(float delta) {
		updateTween();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		{
			sprite.draw(spriteBatch);
		}
		spriteBatch.end();
	}

	private void updateTween() {
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}

	class Input implements InputProcessor {

		public boolean keyDown(int keycode) {
			switch (keycode) {
			case Keys.ENTER:
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen());
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
