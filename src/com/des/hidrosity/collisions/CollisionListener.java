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

	private Player player;

	private Object userDataA;
	private Object userDataB;

	public CollisionListener(Player player) {
		this.player = player;
	}

	public void beginContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) {
			return;
		}

		userDataA = contact.getFixtureA().getUserData();
		userDataB = contact.getFixtureB().getUserData();

		System.out.println("[Begin Contact] " + userDataA + ", " + userDataB);

		checkIfPlayerHitByBullet(contact);
		checkIfPlayerTouchesGround(contact);
		checkIfEnemyHitByBullet(contact);
		checkIfBulletHitsLevel(contact);
		checkIfEnemyBulletHitsLevel(contact);
	}

	private void checkIfEnemyBulletHitsLevel(Contact contact) {
		if (userDataA instanceof StationaryEnemyBullet && userDataB.toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) userDataA)) {
				GameScreen.bulletsToRemove.add((Bullet) userDataA);
			}
		} else if (userDataB instanceof StationaryEnemyBullet && userDataA.toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) userDataB)) {
				GameScreen.bulletsToRemove.add((Bullet) userDataB);
			}
		}
	}

	private void checkIfBulletHitsLevel(Contact contact) {
		if (userDataA instanceof PlayerBullet && userDataB.toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) userDataA)) {
				GameScreen.bulletsToRemove.add((Bullet) userDataA);
			}
		} else if (userDataA.toString().equals("level") && userDataB instanceof PlayerBullet) {
			if (notAlreadyRemoving((Bullet) userDataB)) {
				GameScreen.bulletsToRemove.add((Bullet) userDataB);
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
		if (userDataA instanceof PlayerBullet && userDataB instanceof Enemy) {
			((Enemy) userDataB).hitByBullet();
		} else if (userDataB instanceof PlayerBullet && userDataA instanceof Enemy) {
			((Enemy) userDataA).hitByBullet();
		}
	}

	private void checkIfPlayerHitByBullet(Contact contact) {
		if (userDataA instanceof Player && userDataB instanceof StationaryEnemyBullet) {
			player.hitByBullet();
			if (notAlreadyRemoving((Bullet) userDataB)) {
				GameScreen.bulletsToRemove.add((Bullet) userDataB);
			}
		} else if (userDataB instanceof Player && userDataA instanceof StationaryEnemyBullet) {
			player.hitByBullet();
			if (notAlreadyRemoving((Bullet) userDataA)) {
				GameScreen.bulletsToRemove.add((Bullet) userDataA);
			}
		}
	}

	private void checkIfPlayerTouchesGround(Contact contact) {
		if (userDataA.toString().equals("feet") && userDataB.toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				player.setCanJump(true);
			}

		} else if (userDataB.toString().equals("feet") && userDataA.toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				player.setCanJump(true);
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
		if (contact.getFixtureA().getUserData().toString().equals("feet")
				&& contact.getFixtureB().getUserData().toString().equals("level")) {
			numPlayerCollisions--;

			if (numPlayerCollisions <= 0) {
				player.setCanJump(false);
			}
		} else if (contact.getFixtureB().getUserData().toString().equals("feet")
				&& contact.getFixtureA().getUserData().toString().equals("level")) {
			numPlayerCollisions--;

			if (numPlayerCollisions <= 0) {
				player.setCanJump(false);
			}
		}
	}

	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
