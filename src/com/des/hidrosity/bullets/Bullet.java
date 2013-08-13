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

	private Body physicsBody;
	private World gameWorld;

	public boolean shouldBeRemoved = false;

	private int direction;

	public Bullet(Vector2 position, String textureName, int direction, World gameWorld) {
		super(position, textureName);

		if (direction < -1 || direction > 1) {
			assert false : "Direction isn't -1 or 1";
		}

		this.gameWorld = gameWorld;
		this.direction = direction;

		createPhysicsBody();
		applyInitialImpulse();
	}

	private void applyInitialImpulse() {
		physicsBody.applyLinearImpulse(new Vector2(direction * BulletConstants.SPEED, 0f),
				physicsBody.getWorldCenter(), true);
	}

	private void createPhysicsBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set((getX() + (getWidth() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE,
				(getY() + (getHeight() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE);
		bodyDef.type = BodyType.DynamicBody;

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(((getWidth() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE,
				((getHeight() * GameConstants.IMAGE_SCALE) / 2) * GameConstants.UNIT_SCALE);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = BulletConstants.FRICTION;
		fixtureDef.restitution = BulletConstants.RESTITUTION;
		fixtureDef.shape = polygonShape;
		fixtureDef.density = BulletConstants.DENSITY;

		physicsBody = gameWorld.createBody(bodyDef);

		Fixture fixture = physicsBody.createFixture(fixtureDef);
		fixture.setUserData(this);
	}

	public void update(float delta) {

	}

	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(getTexture(), physicsBody.getPosition().x / GameConstants.UNIT_SCALE - getWidth(),
				physicsBody.getPosition().y / GameConstants.UNIT_SCALE - getHeight(), getTexture().getWidth()
						* GameConstants.IMAGE_SCALE, getTexture().getHeight() * GameConstants.IMAGE_SCALE);
	}
}
