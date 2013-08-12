package com.des.hidrosity.levels;

import com.badlogic.gdx.graphics.Texture;
import com.jakehorsfield.libld.Utils;

public class LevelImageLoader {

	private final String LEVEL_IMAGE_DIR = "res/levels/images/";
	private final String IMAGE_PREFIX = "level";
	private final String IMAGE_FORMAT = ".png";
	
	public Texture loadLevelImage(int currentLevelNumber) {
		return Utils.loadTexture(LEVEL_IMAGE_DIR + IMAGE_PREFIX + currentLevelNumber + IMAGE_FORMAT);
	}

}
