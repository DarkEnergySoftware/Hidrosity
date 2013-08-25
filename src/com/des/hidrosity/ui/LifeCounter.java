package com.des.hidrosity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.des.hidrosity.characters.CharacterManager;
import com.des.hidrosity.player.Player;

public class LifeCounter {

	private Player player;
	
	private Texture texture;
	
	private FreeTypeFontGenerator fontGenerator;
	private BitmapFont font;
	
	public LifeCounter(Player player) {
		this.player = player;
		
		texture = CharacterManager.getCharacter().lifeCounterTexture;
		
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("res/ui/lifesFont.ttf"));
		font = fontGenerator.generateFont(14);
	}
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, 10, Gdx.graphics.getHeight() - 138 - texture.getHeight()*2, texture.getWidth()*2, texture.getHeight()*2);
		font.draw(spriteBatch, String.valueOf(player.getLives()), 78, Gdx.graphics.getHeight() - 158);
	}
	
}
