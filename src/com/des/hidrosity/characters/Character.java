package com.des.hidrosity.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public abstract class Character {

	public Animation animationStandingLeft;
	public Animation animationStandingShootingLeft;
	public Animation animationStandingRight;
	public Animation animationStandingShootingRight;
	public Animation animationWaitingLeft;
	public Animation animationWaitingRight;

	public Animation animationRunningLeft;
	public Animation animationRunningShootingLeft;
	public Animation animationRunningRight;
	public Animation animationRunningShootingRight;

	public Animation animationJumpingLeft;
	public Animation animationJumpingRight;
	public Animation animationJumpingShootingLeft;
	public Animation animationJumpingShootingRight;

	public Animation animationHurtLeft;
	public Animation animationHurtRight;

	public Animation animationDeathLeft;
	public Animation animationDeathRight;

	public Animation currentAnimation;

	public Texture lifeCounterTexture;
	public Texture inventoryScreen;

	public boolean hasSpawnAnimation = false;
	public boolean hasDeathAnimation = false;
    public boolean canJumpTwice = false;

	public Character() {
		loadStandingAnimations();
		loadWaitingAnimations();
		loadRunningAnimations();
		loadJumpingAnimations();
		loadShootingAnimations();
		loadHurtAnimations();
	}

	public abstract void loadStandingAnimations();

	public abstract void loadWaitingAnimations();

	public abstract void loadRunningAnimations();

	public abstract void loadJumpingAnimations();

	public abstract void loadShootingAnimations();

	public abstract void loadHurtAnimations();
}
