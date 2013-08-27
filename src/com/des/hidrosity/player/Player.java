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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.des.hidrosity.bullets.PlayerBullet;
import com.des.hidrosity.characters.CharacterManager;
import com.des.hidrosity.characters.Jetten;
import com.des.hidrosity.constants.CollisionConstants;
import com.des.hidrosity.constants.GameConstants;
import com.des.hidrosity.constants.PlayerConstants;
import com.des.hidrosity.debug.Logger;
import com.des.hidrosity.screens.GameScreen;
import com.jakehorsfield.libld.GameObject;

public class Player extends GameObject {

	private enum PlayerState {
		Standing, Waiting, Running, Jumping, ShootingStanding, ShootingRunning, ShootingJumping, Spawning, Hurt
	};
	private PlayerState currentState;

	private enum PlayerDirection {
		Left, Right
	};
	private PlayerDirection currentDirection;
	
	private Body physicsBody;

	private Animation currentAnimation;
	private TextureRegion currentFrame;

	private float animationStateTime;
	private float timeSpentStanding;

	private long timeStartedWaiting;
	private long timeStartedShooting;
	private long lastTimeShot;

	private Array<PlayerBullet> bullets = new Array<PlayerBullet>();
	
	private boolean shooting = false;
	private boolean hurt = false;
	private boolean spawning = false;
	private boolean dead = false;
	private boolean canJump = true;
	
	private long timePlayerCreated;
	private long timeStartedHurting;
	
	private int lives = 3;
	private int health = 10;
	private int energy = 10;

	public Player(Vector2 position, String textureName) {
		super(position, textureName);

		setInitialStateAndDirection();
		loadAnimations();
		createPhysicsBody();
		
		timePlayerCreated = TimeUtils.millis();
	}

	private void createPhysicsBody() {
		createMainFixture();
		createFeetFixture();
	}
	
	private void createMainFixture() {
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
		fixtureDef.filter.categoryBits = CollisionConstants.PLAYER;
		fixtureDef.filter.maskBits = CollisionConstants.PLAYER_MASK;

		physicsBody = GameScreen.physicsWorld.createBody(bodyDef);
		physicsBody.setFixedRotation(true);
		physicsBody.setLinearDamping(1f);

		Fixture mainFixture = physicsBody.createFixture(fixtureDef);
		mainFixture.setUserData(this);
		
		polygonShape.dispose();
	}
	
	private void createFeetFixture() {
		PolygonShape feetShape = new PolygonShape();
		feetShape.setAsBox((getWidth() - 5) * GameConstants.UNIT_SCALE, (getHeight() / 3) * GameConstants.UNIT_SCALE,
				new Vector2(0, (-getHeight() - getHeight() / 3) * GameConstants.UNIT_SCALE), 0f);

		FixtureDef feetFixtureDef = new FixtureDef();
		feetFixtureDef.isSensor = true;
		feetFixtureDef.shape = feetShape;

		Fixture feetFixture = physicsBody.createFixture(feetFixtureDef);
		feetFixture.setUserData("feet");

		feetShape.dispose();
	}

	private void loadAnimations() {
		currentAnimation = CharacterManager.getCharacter().animationStandingRight;
	}

	private void setInitialStateAndDirection() {
		currentState = PlayerState.Standing;
		currentDirection = PlayerDirection.Right;
	}

	public void update(float delta) {
		updateTimes();
		updateAnimations();
		updateNonPhysicsPosition();
		updateStates();
		checkIfDead();
	}
	
	private void checkIfDead() {
		if (health <= 0) {
			if (lives <= 0) {
				dead = true;
				return;
			}
			
			lives--;
			health = 10;
		}
	}
	
	private void updateTimes() {
		updateShootingTime();
		updateStandingTime();
		updateAnimationStateTime();
	}
	
	private void updateShootingTime() {
		if (TimeUtils.millis() - timeStartedShooting < PlayerConstants.SHOOTING_TIME) {
			shooting = true;
		} else {
			shooting = false;
		}
	}
	
	private void updateAnimations() {
		updateWaitingAnimation();
		updateHurtAnimation();
	}
	
	private void updateHurtAnimation() {
		if (hurt) {
			if (hurtAnimationFinished()) {
				hurt = false;
				currentState = PlayerState.Standing;
				setAnimationToStanding();
				return;
			}
			
			setAnimationToHurt();
		}
	}
	
	private boolean hurtAnimationFinished() {
		if (TimeUtils.millis() - timeStartedHurting > PlayerConstants.HURT_TIME) {
			return true;
		}
		
		return false;
	}
	
	private void setAnimationToStanding() {
		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = CharacterManager.getCharacter().animationStandingLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationStandingRight;
		}
	}
	
	private void updateStates() {
		if (shooting) return;
		
		checkIfStateShouldBeStanding();
		checkIfStateShouldBeWaiting();
		checkIfStateShouldBeJumping();
		checkIfStateShouldBeSpawning();
	}
	
	private void checkIfStateShouldBeSpawning() {
		if (CharacterManager.getCharacter().hasSpawnAnimation == false) return;
		
		if (notFinishedSpawning()) {
			setStateToSpawning();
		} else {
			spawning = false;
		}
	}
	
	private void setStateToSpawning() {
		currentState = PlayerState.Spawning;
		currentAnimation = ((Jetten) CharacterManager.getCharacter()).animationAppear;
		currentDirection = PlayerDirection.Right;
		spawning = true;
	}
	
	private boolean notFinishedSpawning() {
		if (TimeUtils.millis() - timePlayerCreated < PlayerConstants.SPAWN_TIME) {
			return true;
		}
		
		return false;
	}
	
	private void checkIfStateShouldBeJumping() {
		if (!canJump) {
			setStateToJumping();
		}
	}
	
	private void setStateToJumping() {
		if (hurt) return;
		if (spawning) return;
		
		currentState = PlayerState.Jumping;

		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = CharacterManager.getCharacter().animationJumpingLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationJumpingRight;
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

	@SuppressWarnings("unused")
	private void printDebug() {
		Logger.log(currentState + " " + currentDirection + " at " + physicsBody.getPosition() + " = ("
				+ physicsBody.getPosition().x / GameConstants.UNIT_SCALE + ", " + physicsBody.getPosition().y
				/ GameConstants.UNIT_SCALE + ")" + " spawning????" + spawning);
	}

	private void checkIfStateShouldBeWaiting() {
		if ((int) timeSpentStanding != 0 && (int) timeSpentStanding >= 5) {
			setStateToWaiting();
		}
	}
	
	private void setStateToWaiting() {
		if (hurt) return;
		if (spawning) return;
		
		currentState = PlayerState.Waiting;

		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = CharacterManager.getCharacter().animationWaitingLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationWaitingRight;
		}

		timeStartedWaiting = TimeUtils.millis();
	}

	public void updateNonPhysicsPosition() {
		setX(physicsBody.getPosition().x / GameConstants.UNIT_SCALE);
		setY(physicsBody.getPosition().y / GameConstants.UNIT_SCALE);
	}

	private void checkIfStateShouldBeStanding() {
		if (spawning) return;
		
		if (physicsBody.getLinearVelocity().x >= -1f && physicsBody.getLinearVelocity().x <= 1f) {
			if (waitingAndAnimationNotFinished()) {
				return;
			}

			setStateToStanding();
		}
	}
	
	private void setStateToStanding() {
		if (hurt) return;
		if (spawning) return;
		
		currentState = PlayerState.Standing;

		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = CharacterManager.getCharacter().animationStandingLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationStandingRight;
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
		currentFrame = currentAnimation.getKeyFrame(animationStateTime, false);
	}

	public void moveLeft() {
		if (!hurt) {
			if (shooting) {
				moveLeftShooting();
			} else {
				currentAnimation = CharacterManager.getCharacter().animationRunningLeft;
				currentDirection = PlayerDirection.Left;
				currentState = PlayerState.Running;	
			}
		}
		
		if (movingTooFastLeft() == false) {
			physicsBody.applyLinearImpulse(new Vector2(-PlayerConstants.SPEED, 0f), physicsBody.getWorldCenter(), true);
		}
	}
	
	private void moveLeftShooting() {
		currentAnimation = CharacterManager.getCharacter().animationRunningShootingLeft;
		currentDirection = PlayerDirection.Left;
		currentState = PlayerState.ShootingRunning;
	}

	private boolean movingTooFastLeft() {
		if (physicsBody.getLinearVelocity().x < -PlayerConstants.MAX_VELOCITY) {
			return true;
		}

		return false;
	}

	public void moveRight() {
		if (!hurt) {
			if (shooting) {
				moveRightShooting();
			} else {
				currentAnimation = CharacterManager.getCharacter().animationRunningRight;
				currentDirection = PlayerDirection.Right;
				currentState = PlayerState.Running;
			}
		}

		if (movingTooFastRight() == false) {
			physicsBody.applyLinearImpulse(new Vector2(PlayerConstants.SPEED, 0f), physicsBody.getWorldCenter(), true);
		}
	}
	
	private void moveRightShooting() {
		currentAnimation = CharacterManager.getCharacter().animationRunningShootingRight;
		currentDirection = PlayerDirection.Right;
		currentState = PlayerState.ShootingRunning;
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
		createBullet();
		
		shooting = true;
		timeStartedShooting = TimeUtils.millis();
	}
	
	private void createBullet() {
		if (currentDirection == PlayerDirection.Left) {
			createBulletFromLeft();
		} else {
			createBulletFromRight();
		}
		
		lastTimeShot = TimeUtils.millis();
	}

	private void createBulletFromRight() {
		PlayerBullet b = new PlayerBullet(new Vector2(getX(), getY()), "res/bullets/heroBullet.png", 1, GameScreen.physicsWorld);
		bullets.add(b);
	}

	private void createBulletFromLeft() {
		PlayerBullet b = new PlayerBullet(new Vector2(getX(), getY()), "res/bullets/heroBullet.png", -1, GameScreen.physicsWorld);
		bullets.add(b);
	}

	@SuppressWarnings("incomplete-switch")
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
	
	public void hitByBullet() {
		health -= PlayerConstants.HEALTH_DECREASE;
		currentState = PlayerState.Hurt;
		setAnimationToHurt();
		hurt = true;
		timeStartedHurting = TimeUtils.millis();
		
		knockBack();
	}
	
	private void knockBack() {
		if (currentDirection == PlayerDirection.Left) {
			physicsBody.applyForceToCenter(new Vector2(PlayerConstants.KNOCKBACK_FORCE, 0), true);
		} else if (currentDirection == PlayerDirection.Right) {
			physicsBody.applyForceToCenter(new Vector2(-PlayerConstants.KNOCKBACK_FORCE, 0), true);
		}
	}
	
	private void setAnimationToHurt() {
		if (currentDirection == PlayerDirection.Left) {
			currentAnimation = CharacterManager.getCharacter().animationHurtLeft;
		} else {
			currentAnimation = CharacterManager.getCharacter().animationHurtRight;
		}
	}

	public Body getPhysicsBody() {
		return physicsBody;
	}
	
	public int getLives() {
		 return lives;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean isDead() {
		return dead;
	}
}
