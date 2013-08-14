package com.des.hidrosity.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.des.hidrosity.player.Player;

public class CollisionListener implements ContactListener {

	private int numPlayerCollisions;

	public void beginContact(Contact contact) {
		checkIfPlayerTouchesGround(contact);
	}

	private void checkIfPlayerTouchesGround(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof Player
				&& contact.getFixtureB().getUserData().toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				((Player) contact.getFixtureB().getUserData()).setCanJump(true);
			}
		}

		if (contact.getFixtureB().getUserData() instanceof Player
				&& contact.getFixtureA().getUserData().toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				((Player) contact.getFixtureB().getUserData()).setCanJump(true);
			}
		}
	}

	public void endContact(Contact contact) {
		checkIfPlayerLeavesGround(contact);
	}

	private void checkIfPlayerLeavesGround(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof Player
				&& contact.getFixtureB().getUserData().toString().equals("level")) {
			numPlayerCollisions--;

			if (numPlayerCollisions <= 0) {
				((Player) contact.getFixtureA().getUserData()).setCanJump(false);
			}
		}

		if (contact.getFixtureB().getUserData() instanceof Player
				&& contact.getFixtureA().getUserData().toString().equals("level")) {
			numPlayerCollisions--;

			if (numPlayerCollisions <= 0) {
				((Player) contact.getFixtureB().getUserData()).setCanJump(false);
			}
		}
	}

	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
