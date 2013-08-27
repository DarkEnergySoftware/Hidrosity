package com.des.hidrosity.main;

import com.badlogic.gdx.Game;
import com.des.hidrosity.screens.SplashScreen;
import com.jakehorsfield.libld.Utils;

public class HidrosityGame extends Game {

	@Override
	public void create() {
		setScreen(new SplashScreen());
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();

		Utils.disposeAllTextures();
		Utils.disposeAllMusic();
		Utils.disposeAllSounds();
	}

}
