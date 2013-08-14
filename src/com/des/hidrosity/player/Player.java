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
import com.des.hidrosity.bullets.PlayerBullet;
import com.des.hidrosity.characters.CharacterManager;
import com.des.hidrosity.characters.Jetten;
import com.des.hidrosity.constants.GameConstants;
import com.des.hidrosity.constants.PlayerConstants;
import com.des.hidrosity.debug.Logger;
import com.des.hidrosity.screens.PlayScreen;
import com.jakehorsfield.libld.GameObject;

public class Player extends GameObject {

	private enum PlayerState {
		Standing, Waiting, Walking, Running, Jumping, ShootingStanding, ShootingRunning, ShootingJumping, Spawning
	};

	private enum PlayerDirection {
		Left, Right
	};

	private Body physicsBody;

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
	
	private long timePlayerCreated;

	public Player(Vector2 position, String textureName, World gameWorld) {
		super(position, textureName);
		this.gameWorld = gameWorld;

		setInitialStateAndDirection();
		loadAnimations();
		createPhysicsBody();
		
		timePlayerCreated = TimeUtils.millis();
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
		feetShape.setAsBox((getWidth() - 5) * GameConstants.UNIT_SCALE, (getHeight() / 3) * GameConstants.UNIT_SCALE,
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
		currentAnimation = CharacterManager.getCharacter().animationStandingRight;
	}

	private void setInitialStateAndDirection() {
		currentState = PlayerState.Standing;
		currentDirection = PlayerDirection.Right;
	}

	public void update(float delta) {
		checkIfSpawning();
		updateAnimationStateTime();
		updateStandingTime();
		updateWaitingAnimation();
		updateNonPhysicsPosition();
		checkIfStateShouldBeStanding();
		checkIfStateShouldBeWaiting();
		checkIfStateShouldBeJumping();
		printDebug();
		updateBullets();
	}
	
	private void checkIfSpawning() {
		if (CharacterManager.getCharacter() instanceof Jetten == false) return;
		
		if (TimeUtils.millis() - timePlayerCreated < PlayerConstants.SPAWN_TIME) {
			System.out.println("Should be spawning");
			currentState = PlayerState.Spawning;
			currentAnimation = ((Jetten) CharacterManager.getCharacter()).animationAppear;
			currentDirection = PlayerDirection.Right;
		}
	}
	
	private void updateBullets() {
		for (PlayerBullet b : bullets) {
			if (b.shouldBeRemoved) {
				PlayScreen.bodiesToRemove.add(b.getBody());
			}
		}
	}

	private void checkIfStateShouldBeJumping() {
		if (!canJump) {
			currentState = PlayerState.Jumping;

			if (currentDirection == PlayerDirection.Left) {
				currentAnimation = CharacterManager.getCharacter().animationJumpingLeft;
			} else {
				currentAnimation = CharacterManager.getCharacter().animationJumpingRight;
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
				currentAnimation = CharacterManager.getCharacter().animationWaitingLeft;
			} else {
				currentAnimation = CharacterManager.getCharacter().animationWaitingRight;
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
				currentAnimation = CharacterManager.getCharacter().animationStandingLeft;
			} else {
				currentAnimation = CharacterManager.getCharacter().animationStandingRight;
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
		currentAnimation = CharacterManager.getCharacter().animationRunningLeft;
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
		currentAnimation = CharacterManager.getCharacter().animationRunningRight;
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
			currentAnimation = CharacterManager.getCharacter().animationStandingShootingLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationStandingShootingRight;
		}
	}

	private void setAnimationToShootingJumping() {
		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = CharacterManager.getCharacter().animationJumpingShootingLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationJumpingShootingRight;
		}
	}

	private void setAnimationToShootingRunning() {
		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = CharacterManager.getCharacter().animationRunningShootingLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationRunningShootingRight;
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
