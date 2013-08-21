package com.des.hidrosity.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.des.hidrosity.bullets.Bullet;
import com.des.hidrosity.bullets.PlayerBullet;
import com.des.hidrosity.bullets.StationaryEnemyBullet;
import com.des.hidrosity.enemies.Enemy;
import com.des.hidrosity.player.Player;
import com.des.hidrosity.screens.GameScreen;

public class CollisionListener implements ContactListener {

	private int numPlayerCollisions;

	public void beginContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) {
			return;
		}

		checkIfPlayerHitByBullet(contact);
		checkIfPlayerTouchesGround(contact);
		checkIfEnemyHitByBullet(contact);
		checkIfBulletHitsLevel(contact);
		checkIfEnemyBulletHitsLevel(contact);
	}

	private void checkIfEnemyBulletHitsLevel(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof StationaryEnemyBullet
				&& contact.getFixtureB().getUserData().toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) contact.getFixtureA().getUserData())) {
				GameScreen.bulletsToRemove.add((Bullet) contact.getFixtureA().getUserData());
			}
		}
		
		if (contact.getFixtureB().getUserData() instanceof StationaryEnemyBullet
				&& contact.getFixtureA().getUserData().toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) contact.getFixtureB().getUserData())) {
				GameScreen.bulletsToRemove.add((Bullet) contact.getFixtureB().getUserData());
			}
		}
	}

	private void checkIfBulletHitsLevel(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof PlayerBullet
				&& contact.getFixtureB().getUserData().toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) contact.getFixtureA().getUserData())) {
				GameScreen.bulletsToRemove.add((Bullet) contact.getFixtureA().getUserData());
			}
		}

		if (contact.getFixtureA().getUserData().toString().equals("level")
				&& contact.getFixtureB().getUserData() instanceof PlayerBullet) {
			if (notAlreadyRemoving((Bullet) contact.getFixtureB().getUserData())) {
				GameScreen.bulletsToRemove.add((Bullet) contact.getFixtureB().getUserData());
			}
		}
	}

	private boolean notAlreadyRemoving(Bullet bullet) {
		if (GameScreen.bulletsToRemove.contains(bullet, true)) {
			return false;
		}

		return true;
	}

	private void checkIfEnemyHitByBullet(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof PlayerBullet
				&& contact.getFixtureB().getUserData() instanceof Enemy) {
			((Enemy) contact.getFixtureB().getUserData()).hitByBullet();
		}

		if (contact.getFixtureB().getUserData() instanceof PlayerBullet
				&& contact.getFixtureA().getUserData() instanceof Enemy) {
			((Enemy) contact.getFixtureA().getUserData()).hitByBullet();
		}
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
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) {
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
