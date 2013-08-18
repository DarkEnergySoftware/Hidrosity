package com.des.hidrosity.constants;

public class CollisionConstants {

	public static final short PLAYER = 0x0001;
	public static final short ENEMY = 0x0002;
	public static final short LEVEL = 0x0004;
	
	public static final short PLAYER_MASK = ENEMY | LEVEL;
	public static final short ENEMY_MASK = PLAYER | LEVEL ;
	public static final short LEVEL_MASK = -1;
}
