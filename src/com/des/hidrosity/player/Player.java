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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.des.hidrosity.animation.AnimationLoader;
import com.des.hidrosity.bullets.PlayerBullet;
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

	private float timeSpentStanding;

	private long timeStartedWaiting;
	private long lastTimeShot;

	private Array<PlayerBullet> bullets = new Array<>();

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
		physicsBody.setLinearDamping(1f);

		Fixture mainFixture = physicsBody.createFixture(fixtureDef);

		PolygonShape feetShape = new PolygonShape();
		feetShape.setAsBox((getWidth()) * GameConstants.UNIT_SCALE, (getHeight() / 3) * GameConstants.UNIT_SCALE,
				new Vector2(0, (-getHeight() - getHeight() / 3) * GameConstants.UNIT_SCALE), 0f);

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

		animationRunningLeft.setPlayMode(Animation.LOOP_PINGPONG);
		animationRunningRight.setPlayMode(Animation.LOOP_PINGPONG);
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
		updateStandingTime();
		updateWaitingAnimation();
		updateNonPhysicsPosition();
		checkIfStateShouldBeStanding();
		checkIfStateShouldBeWaiting();
		checkIfStateShouldBeJumping();
		printDebug();
	}

	private void checkIfStateShouldBeJumping() {
		if (!canJump) {
			currentState = PlayerState.Jumping;

			if (currentDirection == PlayerDirection.Left) {
				currentAnimation = animationJumpingLeft;
			} else {
				currentAnimation = animationJumpingRight;
			}
		}
	}

	private void updateWaitingAnimation() {
		if (currentState != PlayerState.Waiting) {
			return;
		}
	}

	private void updateStandingTime() {
		if (currentState == PlayerState.Standing) {
			timeSpentStanding += Gdx.graphics.getDeltaTime();
		} else {
			timeSpentStanding = 0f;
		}
	}

	private void printDebug() {
		Logger.log(currentState + " " + currentDirection + " at " + physicsBody.getPosition() + " = ("
				+ physicsBody.getPosition().x / GameConstants.UNIT_SCALE + ", " + physicsBody.getPosition().y
				/ GameConstants.UNIT_SCALE + ")");
	}

	private void checkIfStateShouldBeWaiting() {
		if ((int) timeSpentStanding != 0 && (int) timeSpentStanding >= 5) {
			currentState = PlayerState.Waiting;

			if (currentDirection == PlayerDirection.Left) {
				currentAnimation = animationWaitingLeft;
			} else {
				currentAnimation = animationWaitingRight;
			}

			timeStartedWaiting = TimeUtils.millis();
		}
	}

	private void updateNonPhysicsPosition() {
		setX(physicsBody.getPosition().x / GameConstants.UNIT_SCALE);
		setY(physicsBody.getPosition().y / GameConstants.UNIT_SCALE);
	}

	private void checkIfStateShouldBeStanding() {
		if (physicsBody.getLinearVelocity().x >= -1f && physicsBody.getLinearVelocity().x <= 1f) {
			if (waitingAndAnimationNotFinished()) {
				return;
			}

			currentState = PlayerState.Standing;

			if (currentDirection == PlayerDirection.Left) {
				currentAnimation = animationStandingLeft;
			} else {
				currentAnimation = animationStandingRight;
			}
		}
	}

	private boolean waitingAndAnimationNotFinished() {
		return currentState == PlayerState.Waiting && TimeUtils.millis() - timeStartedWaiting < 1000f;
	}

	private void updateAnimationStateTime() {
		animationStateTime += Gdx.graphics.getDeltaTime();
	}

	public void render(SpriteBatch spriteBatch) {
		setCurrentFrame();

		renderPlayer(spriteBatch);
		renderBullets(spriteBatch);
	}
	
	private void renderBullets(SpriteBatch spriteBatch) {
		for (PlayerBullet b : bullets) {
			b.render(spriteBatch);
		}
	}
	
	private void renderPlayer(SpriteBatch spriteBatch) {
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

		if (movingTooFastLeft() == false) {
			physicsBody.applyLinearImpulse(new Vector2(-PlayerConstants.SPEED, 0f), physicsBody.getWorldCenter(), true);
		}
	}

	private boolean movingTooFastLeft() {
		if (physicsBody.getLinearVelocity().x < -PlayerConstants.MAX_VELOCITY) {
			return true;
		}

		return false;
	}

	public void moveRight() {
		currentAnimation = animationRunningRight;
		currentDirection = PlayerDirection.Right;
		currentState = PlayerState.Running;

		if (movingTooFastRight() == false) {
			physicsBody.applyLinearImpulse(new Vector2(PlayerConstants.SPEED, 0f), physicsBody.getWorldCenter(), true);
		}
	}

	private boolean movingTooFastRight() {
		if (physicsBody.getLinearVelocity().x > PlayerConstants.MAX_VELOCITY) {
			return true;
		}

		return false;
	}

	public void jump() {
		if (!canJump) {
			return;
		}

		physicsBody.applyLinearImpulse(new Vector2(0f, PlayerConstants.JUMP_FORCE), physicsBody.getWorldCenter(), true);
	}

	public void shoot() {
		if (shootingTooFast()) {
			return;
		}

		setShootingStateAndAnimation();

		if (currentDirection == PlayerDirection.Left) {
			createBulletFromLeft();
		} else {
			createBulletFromRight();
		}
		
		lastTimeShot = TimeUtils.millis();
	}

	private void createBulletFromRight() {
		PlayerBullet b = new PlayerBullet(new Vector2(getX(), getY()), "res/bullets/heroBullet.png", 1, gameWorld);
		bullets.add(b);
	}

	private void createBulletFromLeft() {
		PlayerBullet b = new PlayerBullet(new Vector2(getX(), getY()), "res/bullets/heroBullet.png", -1, gameWorld);
		bullets.add(b);
	}

	private void setShootingStateAndAnimation() {
		switch (currentState) {
		case Running:
			currentState = PlayerState.ShootingRunning;
			setAnimationToShootingRunning();
			break;
		case Jumping:
			currentState = PlayerState.ShootingJumping;
			setAnimationToShootingJumping();
			break;
		case Standing:
			currentState = PlayerState.ShootingStanding;
			setAnimationToShootingStanding();
			break;
		}
	}

	private void setAnimationToShootingStanding() {
		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = animationStandingShootingLeft;
		} else {
			currentAnimation = animationStandingShootingRight;
		}
	}

	private void setAnimationToShootingJumping() {
		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = animationJumpingShootingLeft;
		} else {
			currentAnimation = animationJumpingShootingRight;
		}
	}

	private void setAnimationToShootingRunning() {
		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = animationRunningShootingLeft;
		} else {
			currentAnimation = animationRunningShootingRight;
		}
	}

	private boolean shootingTooFast() {
		if (TimeUtils.millis() - lastTimeShot > PlayerConstants.SHOOT_DELAY) {
			return false;
		}

		return true;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public Body getPhysicsBody() {
		return physicsBody;
	}
}
