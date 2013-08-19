package com.des.hidrosity.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.des.hidrosity.animation.AnimationLoader;
import com.des.hidrosity.constants.PlayerConstants;

public class Jetten extends Character {

	public Animation animationAppear;
	
	public Jetten() {
		super();
		
		loadAppearAnimation();
	}
	
	private void loadAppearAnimation() {
		animationAppear = AnimationLoader.loadAnimation(1/3f, "res/player/jetten/appear/appear.txt");
	}
	
	public void loadStandingAnimations() {
		animationStandingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/standing/left.txt");
		animationStandingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/standing/right.txt");

		animationStandingLeft.setPlayMode(Animation.LOOP);
		animationStandingRight.setPlayMode(Animation.LOOP);
	}

	public void loadRunningAnimations() {
		animationRunningLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/running/left.txt");
		animationRunningRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/running/right.txt");

		animationRunningLeft.setPlayMode(Animation.LOOP_PINGPONG);
		animationRunningRight.setPlayMode(Animation.LOOP_PINGPONG);
	}

	public void loadJumpingAnimations() {
		animationJumpingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/jumping/left.txt");
		animationJumpingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/jumping/right.txt");

		animationJumpingLeft.setPlayMode(Animation.LOOP);
		animationJumpingRight.setPlayMode(Animation.LOOP);
	}

	public void loadHurtAnimations() {
		animationHurtLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/hurt/left.txt");
		animationHurtRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/hurt/right.txt");

		animationHurtLeft.setPlayMode(Animation.LOOP);
		animationHurtRight.setPlayMode(Animation.LOOP);
	}

	public void loadWaitingAnimations() {
		animationWaitingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/waiting/left.txt");
		animationWaitingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/waiting/right.txt");

		animationWaitingLeft.setPlayMode(Animation.LOOP);
		animationWaitingRight.setPlayMode(Animation.LOOP);
	}

	public void loadShootingAnimations() {
		animationStandingShootingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/shooting/left.txt");
		animationStandingShootingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/shooting/right.txt");
		animationRunningShootingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/shooting/runLeft.txt");
		animationRunningShootingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/shooting/runRight.txt");
		animationJumpingShootingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/shooting/jumpLeft.txt");
		animationJumpingShootingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/jetten/shooting/jumpRight.txt");

		animationStandingShootingLeft.setPlayMode(Animation.LOOP);
		animationStandingShootingRight.setPlayMode(Animation.LOOP);
		animationRunningShootingLeft.setPlayMode(Animation.LOOP);
		animationRunningShootingRight.setPlayMode(Animation.LOOP);
		animationJumpingShootingLeft.setPlayMode(Animation.LOOP);
		animationJumpingShootingRight.setPlayMode(Animation.LOOP);
	}

}
