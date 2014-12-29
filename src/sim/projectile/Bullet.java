package Parasite.sim.projectile;

import Parasite.sim.entity.Entity;

public class Bullet extends Projectile {

	public Bullet(double x, double y, double vx, double vy) {
		super(x, y, vx, vy);
	}

	public void impact(Entity entity) {
		entity.die();
	}
}
