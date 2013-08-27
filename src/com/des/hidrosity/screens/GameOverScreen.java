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
<<<<<<< HEAD
	
	private Sprite sprite;
	
	private TweenManager tweenManager;

=======

	@Override
>>>>>>> death_animation
	public void show() {
		spriteBatch = new SpriteBatch();
		texture = Utils.loadTexture("res/menus/game over/texture.png");

<<<<<<< HEAD
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

=======
		Gdx.input.setInputProcessor(new Input());
	}

	@Override
>>>>>>> death_animation
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

<<<<<<< HEAD
	private void updateTween() {
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

=======
	@Override
>>>>>>> death_animation
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

<<<<<<< HEAD
	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void resume() {
	}

=======
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
>>>>>>> death_animation
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

<<<<<<< HEAD
=======
		@Override
>>>>>>> death_animation
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
