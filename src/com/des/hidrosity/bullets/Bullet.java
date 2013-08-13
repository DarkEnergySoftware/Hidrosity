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

	private enum BulletDirection {
		Left, Right
	}

	private BulletDirection direction;

	private Body physicsBody;
	private World gameWorld;
	
	private boolean shouldBeRemoved = false;

	public Bullet(Vector2 position, String textureName, BulletDirection direction, World gameWorld) {
		super(position, textureName);

		this.direction = direction;
		this.gameWorld = gameWorld;

		createPhysicsBody();
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

	}
}
