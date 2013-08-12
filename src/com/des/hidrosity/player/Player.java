package com.des.hidrosity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.des.hidrosity.animation.AnimationLoader;
import com.des.hidrosity.constants.GameConstants;
import com.des.hidrosity.constants.PlayerConstants;
import com.des.hidrosity.debug.Logger;
import com.jakehorsfield.libld.GameObject;

public class Player extends GameObject {

	private enum PlayerState {
		Standing, Waiting, Walking, Running, Jumping, ShootingStanding, ShootingRunning, ShootingJumping
	};

	private enum PlayerDirection {
		Left, Right
	};

	private Body physicsBody;

	private Animation animationStandingLeft;
	private Animation animationStandingShootingLeft;
	private Animation animationStandingRight;
	private Animation animationStandingShootingRight;
	private Animation animationWaitingLeft;
	private Animation animationWaitingRight;

	private Animation animationRunningLeft;
	private Animation animationRunningShootingLeft;
	private Animation animationRunningRight;
	private Animation animationRunningShootingRight;

	private Animation animationJumpingLeft;
	private Animation animationJumpingRight;
	private Animation animationJumpingShootingLeft;
	private Animation animationJumpingShootingRight;

	private Animation animationHurtLeft;
	private Animation animationHurtRight;

	private Animation currentAnimation;
	private float animationStateTime;
	private TextureRegion currentFrame;

	private PlayerState currentState;
	private PlayerDirection currentDirection;

	private World gameWorld;
	
	private boolean canJump = true;

	public Player(Vector2 position, String textureName, World gameWorld) {
		super(position, textureName);
		this.gameWorld = gameWorld;

		setInitialStateAndDirection();
		loadAnimations();
		createPhysicsBody();
	}

	private void createPhysicsBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set((getX() + (getWidth() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE,
				(getY() + (getHeight() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE);
		bodyDef.type = BodyType.DynamicBody;

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(((getWidth() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE,
				(((getHeight() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = PlayerConstants.DENSITY;
		fixtureDef.friction = PlayerConstants.FRICTION;
		fixtureDef.restitution = PlayerConstants.RESTITUTION;

		physicsBody = gameWorld.createBody(bodyDef);
		physicsBody.setFixedRotation(true);

		Fixture mainFixture = physicsBody.createFixture(fixtureDef);
		mainFixture.setUserData(this);
		
		PolygonShape feetShape = new PolygonShape();
		feetShape.setAsBox(
				(getWidth() / 2) * GameConstants.UNIT_SCALE,
				(getHeight() / 4) * GameConstants.UNIT_SCALE,
				new Vector2(0, -getHeight() * GameConstants.UNIT_SCALE),
				0f);
		
		FixtureDef feetFixtureDef = new FixtureDef();
		feetFixtureDef.isSensor = true;
		feetFixtureDef.shape = feetShape;
		
		Fixture feetFixture = physicsBody.createFixture(feetFixtureDef);
		feetFixture.setUserData(this);

		feetShape.dispose();
		polygonShape.dispose();
	}

	private void loadAnimations() {
		loadStandingAnimations();
		loadWaitingAnimations();
		loadJumpingAnimations();
		loadShootingAnimations();
		loadHurtAnimations();
		loadRunningAnimations();

		currentAnimation = animationStandingRight;
	}

	private void loadRunningAnimations() {
		animationRunningLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/running/left.txt");
		animationRunningRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/running/right.txt");
	}

	private void loadHurtAnimations() {
		animationHurtLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/hurt/left.txt");
		animationHurtRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/hurt/right.txt");

		animationHurtLeft.setPlayMode(Animation.LOOP);
		animationHurtRight.setPlayMode(Animation.LOOP);
	}

	private void loadShootingAnimations() {
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

	private void loadJumpingAnimations() {
		animationJumpingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/jumping/left.txt");
		animationJumpingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/jumping/right.txt");

		animationJumpingLeft.setPlayMode(Animation.LOOP);
		animationJumpingRight.setPlayMode(Animation.LOOP);
	}

	private void loadWaitingAnimations() {
		animationWaitingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/waiting/left.txt");
		animationWaitingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/waiting/right.txt");

		animationWaitingLeft.setPlayMode(Animation.LOOP);
		animationWaitingRight.setPlayMode(Animation.LOOP);
	}

	private void loadStandingAnimations() {
		animationStandingLeft = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/standing/left.txt");
		animationStandingRight = AnimationLoader.loadAnimation(PlayerConstants.FRAME_DURATION,
				"res/player/the hero/standing/right.txt");

		animationStandingLeft.setPlayMode(Animation.LOOP);
		animationStandingRight.setPlayMode(Animation.LOOP);
	}

	private void setInitialStateAndDirection() {
		currentState = PlayerState.Standing;
		currentDirection = PlayerDirection.Right;
	}

	public void update(float delta) {
		updateAnimationStateTime();
		checkIfStateShouldBeIdle();
		updateNonPhysicsPosition();
//		printDebug();
	}

	private void printDebug() {
		Logger.log(currentState + " " + currentDirection + " at " + physicsBody.getPosition() + " = ("
				+ physicsBody.getPosition().x / GameConstants.UNIT_SCALE + ", " + physicsBody.getPosition().y
				/ GameConstants.UNIT_SCALE + ")");
	}

	private void updateNonPhysicsPosition() {
		setX(physicsBody.getPosition().x / GameConstants.UNIT_SCALE);
		setY(physicsBody.getPosition().y / GameConstants.UNIT_SCALE);
	}

	private void checkIfStateShouldBeIdle() {
		if (physicsBody.getLinearVelocity().x >= -1f && physicsBody.getLinearVelocity().x <= 1f) {
			currentState = PlayerState.Standing;

			if (currentDirection == PlayerDirection.Left) {
				currentAnimation = animationStandingLeft;
			} else {
				currentAnimation = animationStandingRight;
			}
		}
	}

	private void updateAnimationStateTime() {
		animationStateTime += Gdx.graphics.getDeltaTime();
	}

	public void render(SpriteBatch spriteBatch) {
		setCurrentFrame();

		spriteBatch.draw(currentFrame, physicsBody.getPosition().x / GameConstants.UNIT_SCALE - getWidth(),
				physicsBody.getPosition().y / GameConstants.UNIT_SCALE - getHeight(), currentFrame.getRegionWidth()
						* GameConstants.IMAGE_SCALE, currentFrame.getRegionHeight() * GameConstants.IMAGE_SCALE);
	}

	private void setCurrentFrame() {
		currentFrame = currentAnimation.getKeyFrame(animationStateTime, true);
	}

	public void moveLeft() {
		currentAnimation = animationRunningLeft;
		currentDirection = PlayerDirection.Left;
		currentState = PlayerState.Running;
		physicsBody.applyLinearImpulse(new Vector2(-PlayerConstants.SPEED, 0f), physicsBody.getWorldCenter(), true);
		Logger.log("moveLeft()");
	}

	public void moveRight() {
		currentAnimation = animationRunningRight;
		currentDirection = PlayerDirection.Right;
		currentState = PlayerState.Running;
		physicsBody.applyLinearImpulse(new Vector2(PlayerConstants.SPEED, 0f), physicsBody.getWorldCenter(), true);
		Logger.log("moveRight()");
	}

	public void jump() {
		if (!canJump) {
			return;
		}
		
		physicsBody.applyLinearImpulse(new Vector2(0f, PlayerConstants.JUMP_FORCE), physicsBody.getWorldCenter(), true);
		Logger.log("jump()");
	}
	
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}
}
