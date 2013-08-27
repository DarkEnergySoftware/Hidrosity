package com.des.hidrosity.levels;

import com.des.hidrosity.constants.GameConstants;

public class LevelBody {

	public float x;
	public float y;
	public float width;
	public float height;

	public LevelBody(float x, float y, float width, float height) {
		this.x = (x * GameConstants.IMAGE_SCALE + GameConstants.X_OFFSET)
				* GameConstants.UNIT_SCALE;
		this.y = (y * GameConstants.IMAGE_SCALE + GameConstants.Y_OFFSET)
				* GameConstants.UNIT_SCALE;
		this.width = (width * GameConstants.IMAGE_SCALE)
				* GameConstants.UNIT_SCALE;
		this.height = (height * GameConstants.IMAGE_SCALE)
				* GameConstants.UNIT_SCALE;
	}

	@Override
	public String toString() {
		return String.format("[LevelBody] X: %f Y: %f Width: %f Height: %f", x,
				y, width, height);
	}

}
