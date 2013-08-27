package com.des.hidrosity.audio;

import com.badlogic.gdx.audio.Music;
import com.jakehorsfield.libld.Utils;

public class MusicManager {

	public static final Music MENU_MUSIC = Utils
			.loadMusic("res/music/menuMusic.mp3");
	public static final Music TITLE_MUSIC = Utils
			.loadMusic("res/music/titleMusic.mp3");
	public static final Music LEVEL_SELECT_MUSIC = Utils
			.loadMusic("res/music/levelSelectMusic.mp3");

}
