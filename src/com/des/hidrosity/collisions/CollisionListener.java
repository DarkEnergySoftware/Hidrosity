package com.des.hidrosity.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.des.hidrosity.bullets.Bullet;
import com.des.hidrosity.bullets.PlayerBullet;
import com.des.hidrosity.player.Player;
import com.des.hidrosity.screens.PlayScreen;

public class CollisionListener implements ContactListener {

	private int numPlayerCollisions;

	private Object fixtureAUserData;
	private Object fixtureBUserData;

	public void beginContact(Contact contact) {
		fixtureAUserData = contact.getFixtureA().getUserData();
		fixtureBUserData = contact.getFixtureB().getUserData();

		if (userDataIsNull(contact)) {
			return;
		}

		checkIfPlayerTouchesGround(contact);
	}

	private boolean userDataIsNull(Contact contact) {
		if (fixtureAUserData == null || fixtureBUserData == null) {
			return true;
		}

		return false;
	}

	private void checkIfPlayerTouchesGround(Contact contact) {
		if (fixtureAUserData instanceof Player && fixtureBUserData.toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				((Player) fixtureBUserData).setCanJump(true);
			}
		}

		if (fixtureBUserData instanceof Player && fixtureAUserData.toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				((Player) fixtureBUserData).setCanJump(true);
			}
		}
	}

	public void endContact(Contact contact) {
		if (userDataIsNull(contact)) {
			return;
		}

		checkIfPlayerLeavesGround(contact);
	}

	private void checkIfPlayerLeavesGround(Contact contact) {
		if (fixtureAUserData instanceof Player && fixtureBUserData.toString().equals("level")) {
			numPlayerCollisions--;

			if (playerCanJump()) {
				((Player) fixtureAUserData).setCanJump(false);
			}
		}

		if (fixtureBUserData instanceof Player && fixtureAUserData.toString().equals("level")) {
			numPlayerCollisions--;

			if (playerCanJump()) {
				((Player) fixtureBUserData).setCanJump(false);
			}
		}
	}

	private boolean playerCanJump() {
		if (numPlayerCollisions <= 0) {
			return true;
		}

		return false;
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
