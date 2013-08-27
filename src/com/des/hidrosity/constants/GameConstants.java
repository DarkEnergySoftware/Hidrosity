package com.des.hidrosity.constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class GameConstants {

	public static final int WIDTH = 512;
	public static final int HEIGHT = 448;
	public static final int X_OFFSET = -Gdx.graphics.getWidth() / 2;
	public static final int Y_OFFSET = -Gdx.graphics.getHeight() / 2;

	public static final float GROUND_Y = -2.9150002f;

	public static final Vector2 GRAVITY = new Vector2(0f, -10f);

	public static final float UNIT_SCALE = 1 / 100f;
	public static final int IMAGE_SCALE = 2;

	public static final boolean DEBUG = true;
}
