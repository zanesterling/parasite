package Parasite.sim.projectile;

import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;

import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Projectile {

	public double x;
	public double y;
	protected double vx;
	protected double vy;
	protected double rad;
	protected double angle;

	public boolean dead;

	public Projectile(double x, double y, double angle, double speed) {
		this.x = x;
		this.y = y;
		this.angle = angle;

		vx = Math.cos(angle) * speed;
		vy = Math.sin(angle) * speed;

		dead = false;
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

		int wallType = Simulation.getInstance().getWallAt(x, y);
		if (wallType != 0) die();
	}

	// returns the square of the distance to the entity
	private double quadrance(Entity entity) {
		double dx = entity.x - x;
		double dy = entity.y - y;
		return dx*dx + dy*dy;
	}

	public abstract void render(Graphics2D g);
	public abstract void impact(Entity entity);

	public void die() {
		dead = true;
	}
}
