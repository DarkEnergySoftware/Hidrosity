package com.des.hidrosity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.des.hidrosity.characters.CharacterManager;
import com.des.hidrosity.player.Player;

public class LifeCounter {

	private Player player;
	
	private Texture texture;
	
	public LifeCounter(Player player) {
		this.player = player;
		
		texture = CharacterManager.getCharacter().lifeCounterTexture;
	}
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, 10, Gdx.graphics.getHeight() - 138 - texture.getHeight()*2, texture.getWidth()*2, texture.getHeight()*2);
	}
	
}
