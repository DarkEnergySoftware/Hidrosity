package com.des.hidrosity.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.des.hidrosity.bullets.StationaryEnemyBullet;
import com.des.hidrosity.debug.Logger;
import com.des.hidrosity.player.Player;

public class CollisionListener implements ContactListener {

	private int numPlayerCollisions;

	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getUserData() == null || contact.getFixtureB().getUserData() == null) {
			return;
		}

		System.out.println(contact.getFixtureA().getUserData() + " - " + contact.getFixtureB().getUserData());
		
		checkIfPlayerHitByBullet(contact);
		checkIfPlayerTouchesGround(contact);
	}

	private void checkIfPlayerHitByBullet(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof Player
				&& contact.getFixtureB().getUserData() instanceof StationaryEnemyBullet) {
			((Player) contact.getFixtureA().getUserData()).hitByBullet();
		}

		if (contact.getFixtureB().getUserData() instanceof Player
				&& contact.getFixtureA().getUserData() instanceof StationaryEnemyBullet) {
			((Player) contact.getFixtureB().getUserData()).hitByBullet();
		}
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
		if (contact.getFixtureA().getUserData() == null || contact.getFixtureB().getUserData() == null) {
			return;
		}

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