package com.des.hidrosity.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class LevelManager {

	@SuppressWarnings("unused")
	private final World gameWorld;

	private Texture currentLevelTexture;
	private int currentLevelNumber;

	private LevelImageLoader levelLoader;
	private LevelCreator levelCreator;

	public LevelManager(World gameWorld, int startingLevel) {
		this.gameWorld = gameWorld;

		levelLoader = new LevelImageLoader();
		levelCreator = new LevelCreator(gameWorld);

		setLevel(startingLevel);
	}

	public void setLevel(int levelNumber) {
		this.currentLevelNumber = levelNumber;

		loadLevelImage();
		loadLevelData();
	}

	private void loadLevelImage() {
		currentLevelTexture = levelLoader.loadLevelImage(currentLevelNumber);
	}

	private void loadLevelData() {
		levelCreator.createLevelFromXMLData(currentLevelNumber);
	}

	public void renderLevel(SpriteBatch spriteBatch) {
		spriteBatch.draw(currentLevelTexture, -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
				currentLevelTexture.getWidth() * 2, currentLevelTexture.getHeight() * 2);
	}
}
