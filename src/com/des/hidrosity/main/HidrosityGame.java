package com.des.hidrosity.main;

import com.badlogic.gdx.Game;
import com.des.hidrosity.screens.SplashScreen;
import com.jakehorsfield.libld.Utils;

public class HidrosityGame extends Game {

	public void create() {
		setScreen(new SplashScreen());
	}

	public void render() {
		super.render();
	}
	
	public void pause() {
		super.pause();
	}
	
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	public void resume() {
		super.resume();
	}
	
	public void dispose() {
		super.dispose();
		
		Utils.disposeAllTextures();
		Utils.disposeAllMusic();
		Utils.disposeAllSounds();
	}

}
