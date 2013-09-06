package com.des.hidrosity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.des.hidrosity.bullets.StationaryEnemyBullet;
import com.des.hidrosity.constants.EnemyConstants;
import com.des.hidrosity.player.Player;
import com.des.hidrosity.screens.GameScreen;
import com.jakehorsfield.libld.Utils;

public class StationaryEnemy extends Enemy {

	private long lastTimeShot;

	private Array<StationaryEnemyBullet> bullets = new Array<StationaryEnemyBullet>();

	private boolean shooting = false;
	private long timeStartedShooting;
    private long timeStartedHurting;

    private float animationStateTime;

    private Animation hurtLeftAnimation;
    private Animation hurtRightAnimation;
    private Animation currentAnimation;

    private TextureRegion currentFrame;

	public StationaryEnemy(Vector2 position, String textureName, Player player) {
		super(position, textureName, player);

        physicsBody.setType(BodyDef.BodyType.StaticBody);

        hurtLeftAnimation = new Animation(0.1f, new TextureRegion(new Texture("res/enemies/stationary enemy/left.png")),
                                                new TextureRegion(new Texture("res/enemies/stationary enemy/blank.png")));
        hurtRightAnimation = new Animation(0.1f, new TextureRegion(new Texture("res/enemies/stationary enemy/right.png")),
                new TextureRegion(new Texture("res/enemies/stationary enemy/blank.png")));

        hurtLeftAnimation.setPlayMode(Animation.LOOP);
        hurtRightAnimation.setPlayMode(Animation.LOOP);

		loadTextures();
	}

	private void loadTextures() {
		leftTexture = Utils
				.loadTexture("res/enemies/stationary enemy/left.png");
		rightTexture = Utils
				.loadTexture("res/enemies/stationary enemy/right.png");
		shootLeftTexture = Utils
				.loadTexture("res/enemies/stationary enemy/shootLeft.png");
		shootRightTexture = Utils
				.loadTexture("res/enemies/stationary enemy/shootRight.png");
	}

	@Override
	public void update(float delta) {
		super.update(delta);
        updateAnimations();
		updateShooting();
    }

    private void updateAnimations() {
        animationStateTime += Gdx.graphics.getDeltaTime();

        if (currentState == State.Hurt) {
            if (TimeUtils.millis() - timeStartedHurting > EnemyConstants.HURT_TIME) {
                currentState = State.Idle;
                return;
            }

            setHurtAnimationAndState();
        }
    }

	private void updateShooting() {
		if (inRangeOfPlayer() && shouldShoot()) {
			shoot();
		}

		checkIfShouldStopShooting();
	}

	private void checkIfShouldStopShooting() {
		if (TimeUtils.millis() - timeStartedShooting > EnemyConstants.SHOOT_TIME) {
			shooting = false;
		}
	}

	@Override
	protected void facePlayer() {
		if (shooting) {
			return;
		}

		super.facePlayer();
	}

	private void shoot() {
		if (currentDirection == Direction.Left) {
			shootBulletFromLeft();
		} else {
			shootBulletFromRight();
		}

		shooting = true;
		lastTimeShot = TimeUtils.millis();
		timeStartedShooting = TimeUtils.millis();
	}

	private boolean inRangeOfPlayer() {
		float distanceToPlayer = getDistanceToPlayer();

		if (distanceToPlayer < EnemyConstants.SHOOT_RANGE) {
			return true;
		}

		return false;
	}

	private float getDistanceToPlayer() {
		return (float) Math.sqrt(Math.pow(getX() - player.getX(), 2)
				+ Math.pow(getY() - player.getY(), 2));
	}

	private void shootBulletFromRight() {
		StationaryEnemyBullet b = new StationaryEnemyBullet(new Vector2(getX()
				+ getWidth(), getY() + getHeight()),
				"res/bullets/jettenBullet.png", 1, GameScreen.physicsWorld, this);
		bullets.add(b);
		setTexture(shootRightTexture);
	}

	private void shootBulletFromLeft() {
		StationaryEnemyBullet b = new StationaryEnemyBullet(new Vector2(getX()
				- getWidth(), getY() + getHeight()),
				"res/bullets/jettenBullet.png", -1, GameScreen.physicsWorld, this);
		bullets.add(b);
	}

    private void setHurtAnimationAndState() {
        if (currentDirection == Direction.Left) {
            currentAnimation = hurtLeftAnimation;
        } else {
            currentAnimation = hurtRightAnimation;
        }

        currentState = State.Hurt;
        Gdx.app.log("Stationary Enemy", "Time started hurting = " + timeStartedHurting);
    }

	private boolean shouldShoot() {
		if (TimeUtils.millis() - lastTimeShot > EnemyConstants.SHOOT_DELAY
				+ MathUtils.random(-1000, 1000)) {
			return true;
		}

		return false;
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
        if (currentState == State.Hurt) {
            setCurrentFrame();
        }

        super.render(spriteBatch);

		renderBullets(spriteBatch);
	}

    private void setCurrentFrame() {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationStateTime, true);
        setTexture(currentFrame.getTexture());
    }

	private void renderBullets(SpriteBatch spriteBatch) {
		for (StationaryEnemyBullet b : bullets) {
			b.render(spriteBatch);
		}
	}

	@Override
	public void hitByBullet() {
		health -= 100 / 3;
        Gdx.app.log("Stationary Enemy", "Hit by bullet");
        setHurtAnimationAndState();
        timeStartedHurting = TimeUtils.millis();
	}

	@Override
	public void prepareForRemoval() {
		for (StationaryEnemyBullet seb : bullets) {
			GameScreen.physicsWorld.destroyBody(seb.getBody());
		}

		bullets.clear();
	}
}
