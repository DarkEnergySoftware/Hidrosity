package com.des.hidrosity.bullets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.des.hidrosity.constants.BulletConstants;
import com.des.hidrosity.constants.GameConstants;
import com.jakehorsfield.libld.GameObject;

public abstract class Bullet extends GameObject {
	public boolean shouldBeRemoved = false;

	private int direction;

	protected World gameWorld;
	protected Body physicsBody;
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;
	protected Fixture fixture;
	
	private Object parent;

	public Bullet(Vector2 position, String textureName, int direction,
			World gameWorld, Object parent) {
		super(position, textureName);

		if (direction < -1 || direction > 1) {
			assert false : "Direction isn't -1 or 1";
		}

		this.gameWorld = gameWorld;
		this.direction = direction;
		this.parent = parent;

		createPhysicsBody();
		applyInitialImpulse();
	}

	private void applyInitialImpulse() {
		physicsBody.applyLinearImpulse(new Vector2(direction
				* BulletConstants.SPEED, 0f), physicsBody.getWorldCenter(),
				true);
	}

	private void createPhysicsBody() {
		bodyDef = new BodyDef();
		bodyDef.position.set(
				(getX() + (getWidth() * GameConstants.IMAGE_SCALE) / 2)
						* GameConstants.UNIT_SCALE,
				(getY() + (getHeight() * GameConstants.IMAGE_SCALE) / 2)
						* GameConstants.UNIT_SCALE);
		bodyDef.type = BodyType.DynamicBody;

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(((getWidth() * GameConstants.IMAGE_SCALE) / 2)
				* GameConstants.UNIT_SCALE,
				((getHeight() * GameConstants.IMAGE_SCALE) / 2)
						* GameConstants.UNIT_SCALE);

		fixtureDef = new FixtureDef();
		fixtureDef.friction = BulletConstants.FRICTION;
		fixtureDef.restitution = BulletConstants.RESTITUTION;
		fixtureDef.shape = polygonShape;
		fixtureDef.density = BulletConstants.DENSITY;

		physicsBody = gameWorld.createBody(bodyDef);
		physicsBody.setFixedRotation(true);
		physicsBody.setGravityScale(0f);

		fixture = physicsBody.createFixture(fixtureDef);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(getTexture(), physicsBody.getPosition().x
				/ GameConstants.UNIT_SCALE - getWidth(),
				physicsBody.getPosition().y / GameConstants.UNIT_SCALE
						- getHeight(), getTexture().getWidth()
						* GameConstants.IMAGE_SCALE, getTexture().getHeight()
						* GameConstants.IMAGE_SCALE);
	}

	public Body getBody() {
		return physicsBody;
	}
	
	public Object getParent() {
		return parent;
	}
}
