package com.des.hidrosity.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.des.hidrosity.tweens.SpriteTweenAccessor;
import com.jakehorsfield.libld.Utils;

public class SplashScreen implements Screen {

	private SpriteBatch spriteBatch;

	private Texture splashScreenTexture;
	private Sprite splashScreenSprite;

	private TweenManager tweenManager;

	@Override
	public void show() {
		createTextureAndSprite();
		createFadeInTween();

		spriteBatch = new SpriteBatch();
	}

	private void createTextureAndSprite() {
		splashScreenTexture = Utils
				.loadTexture("res/menus/splash screen/splashScreen.png");
		splashScreenSprite = new Sprite(splashScreenTexture);
	}

	private void createFadeInTween() {
		tweenManager = new TweenManager();

		Tween.registerAccessor(Sprite.class, new SpriteTweenAccessor());
		Tween.set(splashScreenSprite, SpriteTweenAccessor.ALPHA).target(0f)
				.start(tweenManager);
		Tween.to(splashScreenSprite, SpriteTweenAccessor.ALPHA, 2f).target(1)
				.repeatYoyo(1, 1f).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						((Game) Gdx.app.getApplicationListener())
								.setScreen(new TitleScreen());
					}
				}).start(tweenManager);
	}

	@Override
	public void render(float delta) {
		handleInput();
		updateTween();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		{
			splashScreenSprite.draw(spriteBatch);
		}
		spriteBatch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new TitleScreen());
		}
	}

	private void updateTween() {
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
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

}
