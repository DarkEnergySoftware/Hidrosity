package com.des.hidrosity.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.des.hidrosity.animation.AnimationLoader;
import com.des.hidrosity.constants.PlayerConstants;
import com.jakehorsfield.libld.Utils;

public class TheHero extends Character {

	public TheHero() {
		super();
	}

	public void loadStandingAnimations() {
		animationStandingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/standing/left.txt");
		animationStandingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/standing/right.txt");

		animationStandingLeft.setPlayMode(Animation.LOOP);
		animationStandingRight.setPlayMode(Animation.LOOP);
		
		lifeCounterTexture = Utils.loadTexture("res/ui/heroLifeCounter.png");
	}

	public void loadRunningAnimations() {
		animationRunningLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/running/left.txt");
		animationRunningRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/running/right.txt");

		animationRunningLeft.setPlayMode(Animation.LOOP);
		animationRunningRight.setPlayMode(Animation.LOOP);
	}

	public void loadJumpingAnimations() {
		animationJumpingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/jumping/left.txt");
		animationJumpingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/jumping/right.txt");

		animationJumpingLeft.setPlayMode(Animation.LOOP);
		animationJumpingRight.setPlayMode(Animation.LOOP);
	}

	public void loadHurtAnimations() {
		animationHurtLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/hurt/left.txt");
		animationHurtRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/hurt/right.txt");

		animationHurtLeft.setPlayMode(Animation.LOOP);
		animationHurtRight.setPlayMode(Animation.LOOP);
	}

	public void loadWaitingAnimations() {
		animationWaitingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/waiting/left.txt");
		animationWaitingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/waiting/right.txt");

		animationWaitingLeft.setPlayMode(Animation.LOOP);
		animationWaitingRight.setPlayMode(Animation.LOOP);
	}

	public void loadShootingAnimations() {
		animationStandingShootingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/shooting/left.txt");
		animationStandingShootingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/shooting/right.txt");
		animationRunningShootingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/shooting/runLeft.txt");
		animationRunningShootingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/shooting/runRight.txt");
		animationJumpingShootingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/shooting/jumpLeft.txt");
		animationJumpingShootingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/shooting/jumpRight.txt");

		animationStandingShootingLeft.setPlayMode(Animation.LOOP);
		animationStandingShootingRight.setPlayMode(Animation.LOOP);
		animationRunningShootingLeft.setPlayMode(Animation.LOOP);
		animationRunningShootingRight.setPlayMode(Animation.LOOP);
		animationJumpingShootingLeft.setPlayMode(Animation.LOOP);
		animationJumpingShootingRight.setPlayMode(Animation.LOOP);
	}
	
}
