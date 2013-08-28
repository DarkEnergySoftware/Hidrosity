package com.des.hidrosity.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.des.hidrosity.constants.KeyConstants;
import com.des.hidrosity.interfaces.Menu;
import com.des.hidrosity.tweens.SpriteTweenAccessor;
import com.jakehorsfield.libld.Utils;

public class GameOverScreen extends Menu implements Screen {

	private SpriteBatch spriteBatch;
	private Texture texture;
	
	private Sprite sprite;
	
	private TweenManager tweenManager;
	
	public GameOverScreen() {
		super(loadTextures());
	}
	
	private static Texture[] loadTextures() {
		return new Texture[] {
				Utils.loadTexture("res/ui/gameOver1.png"),
				Utils.loadTexture("res/ui/gameOver2.png")
		};
	}

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
			renderBorder(spriteBatch);
			renderCurrentMenu(spriteBatch);
		}
		spriteBatch.end();
	}

	private void updateTween() {
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	public void dispose() {
	}
	
	public void itemSelected() {
		if (continueTextureSelected()) {
			changeScreen(new GameScreen());
		} else if (resetTextureSelected()) {
			changeScreen(new SplashScreen());
		}
	}
	
	private boolean resetTextureSelected() {
		return currentTexture == menuTextures[1];
	}

	private boolean continueTextureSelected() {
		return currentTexture == menuTextures[0];
	}

	class Input implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
			case KeyConstants.MENU_CONFIRM:
				itemSelected();
				break;
			case KeyConstants.MENU_UP:
				moveUp();
				break;
			case KeyConstants.MENU_DOWN:
				moveDown();
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

	public void resize(int width, int height) {
		
	}

	public void pause() {
		
	}

	public void resume() {
		
	}
}
