package Parasite.sim.projectile;

import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;

import java.util.ArrayList;

public abstract class Projectile {

	private double x;
	private double y;
	private double vx;
	private double vy;
	private double rad;

	public Projectile(double x, double y, double vx, double vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
	}

	public void update() {
		x += vx;
		y += vy;

		ArrayList<Entity> entities = Simulation.getInstance().entities;
		for (Entity entity : entities) {
			if (quadrance(entity) < Math.pow(entity.rad + rad, 2)) {
				impact(entity);
			}
		}
	}

	// returns the square of the distance to the entity
	private double quadrance(Entity entity) {
		double dx = entity.x - x;
		double dy = entity.y - y;
		return dx*dx + dy*dy;
	}

	public abstract void impact(Entity entity);
}
