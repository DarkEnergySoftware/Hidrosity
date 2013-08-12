package com.des.hidrosity.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Hidrosity";
		config.width = 1024;
		config.height = 640;
		config.resizable = false;
		config.useGL20 = true;
		new LwjglApplication(new HidrosityGame(), config);
	}
	
}
