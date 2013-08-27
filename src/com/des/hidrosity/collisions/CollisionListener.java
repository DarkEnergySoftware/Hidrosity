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

	private Object beginUserDataA;
	private Object beginUserDataB;

	private Object endUserDataA;
	private Object endUserDataB;

	public CollisionListener(Player player) {
		this.player = player;
	}

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) {
			return;
		}

		beginUserDataA = contact.getFixtureA().getUserData();
		beginUserDataB = contact.getFixtureB().getUserData();

		checkIfPlayerHitByBullet(contact);
		checkIfPlayerTouchesGround(contact);
		checkIfEnemyHitByBullet(contact);
		checkIfBulletHitsLevel(contact);
		checkIfEnemyBulletHitsLevel(contact);
	}

	@SuppressWarnings("unused")
	private void printDebugBeginContact() {
		System.out.println("[Begin Contact] " + beginUserDataA + ", "
				+ beginUserDataB);
	}

	private void checkIfEnemyBulletHitsLevel(Contact contact) {
		if (beginUserDataA instanceof StationaryEnemyBullet
				&& beginUserDataB.toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) beginUserDataA)) {
				GameScreen.bulletsToRemove.add((Bullet) beginUserDataA);
			}
		} else if (beginUserDataB instanceof StationaryEnemyBullet
				&& beginUserDataA.toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) beginUserDataB)) {
				GameScreen.bulletsToRemove.add((Bullet) beginUserDataB);
			}
		}
	}

	private void checkIfBulletHitsLevel(Contact contact) {
		if (beginUserDataA instanceof PlayerBullet
				&& beginUserDataB.toString().equals("level")) {
			if (notAlreadyRemoving((Bullet) beginUserDataA)) {
				GameScreen.bulletsToRemove.add((Bullet) beginUserDataA);
			}
		} else if (beginUserDataA.toString().equals("level")
				&& beginUserDataB instanceof PlayerBullet) {
			if (notAlreadyRemoving((Bullet) beginUserDataB)) {
				GameScreen.bulletsToRemove.add((Bullet) beginUserDataB);
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
		if (beginUserDataA instanceof PlayerBullet
				&& beginUserDataB instanceof Enemy) {
			((Enemy) beginUserDataB).hitByBullet();
		} else if (beginUserDataB instanceof PlayerBullet
				&& beginUserDataA instanceof Enemy) {
			((Enemy) beginUserDataA).hitByBullet();
		}
	}

	private void checkIfPlayerHitByBullet(Contact contact) {
		if (beginUserDataA instanceof Player
				&& beginUserDataB instanceof StationaryEnemyBullet) {
			player.hitByBullet((Enemy) ((Bullet) beginUserDataB).getParent());
			if (notAlreadyRemoving((Bullet) beginUserDataB)) {
				GameScreen.bulletsToRemove.add((Bullet) beginUserDataB);
			}
		} else if (beginUserDataB instanceof Player
				&& beginUserDataA instanceof StationaryEnemyBullet) {
			player.hitByBullet((Enemy) ((Bullet) beginUserDataA).getParent());
			if (notAlreadyRemoving((Bullet) beginUserDataA)) {
				GameScreen.bulletsToRemove.add((Bullet) beginUserDataA);
			}
		}
	}

	private void checkIfPlayerTouchesGround(Contact contact) {
		if (beginUserDataA.toString().equals("feet")
				&& beginUserDataB.toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				player.setCanJump(true);
			}

		} else if (beginUserDataB.toString().equals("feet")
				&& beginUserDataA.toString().equals("level")) {
			numPlayerCollisions++;

			if (numPlayerCollisions > 0) {
				player.setCanJump(true);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) {
			return;
		}

		endUserDataA = contact.getFixtureA().getUserData();
		endUserDataB = contact.getFixtureB().getUserData();

		checkIfPlayerLeavesGround();
	}

	@SuppressWarnings("unused")
	private void printDebugEndContact() {
		System.out.println("[End Contact] " + endUserDataA + ", "
				+ endUserDataB);
	}

	private void checkIfPlayerLeavesGround() {
		if (endUserDataA.toString().equals("feet")
				&& endUserDataB.toString().equals("level")) {
			numPlayerCollisions--;

			if (numPlayerCollisions <= 0) {
				player.setCanJump(false);
			}

		} else if (endUserDataB.toString().equals("feet")
				&& endUserDataA.toString().equals("level")) {
			numPlayerCollisions--;

			if (numPlayerCollisions <= 0) {
				player.setCanJump(false);
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
