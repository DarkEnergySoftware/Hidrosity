package com.des.hidrosity.constants;

public class CollisionConstants {

	public static final short CATEGORY_PLAYER = 0x0001;
	public static final short CATEGORY_LEVEL = 0x0002;
	
	public static final short MASK_PLAYER = CATEGORY_PLAYER | CATEGORY_LEVEL;
	public static final short MASK_LEVEL = -1;
}
