package Parasite.sim.projectile;

import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;
import Parasite.util.Vector2d;

import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Projectile {

	public Vector2d pos;
	protected Vector2d vel;
	protected double rad;
	protected double angle;

	public boolean dead;

	public Projectile(Vector2d pos, double angle, double speed) {
		this.pos = pos;
		this.angle = angle;

		vel = new Vector2d(Math.cos(angle), Math.sin(angle)).scale(speed);

		dead = false;
	}

	public void update() {
		pos.add(vel);

		ArrayList<Entity> entities = Simulation.getInstance().entities;
		for (Entity entity : entities) {
			if (quadrance(entity) < Math.pow(entity.rad + rad, 2)) {
				impact(entity);
			}
		}

		int wallType = Simulation.getInstance().level.getWallAt(pos);
		if (wallType > 0) die();
	}

	// returns the square of the distance to the entity
	private double quadrance(Entity entity) {
		return entity.getPosition()
			.sub(pos)
			.lengthSquared();
	}

	public abstract void render(Graphics2D g);
	public abstract void impact(Entity entity);

	public void die() {
		dead = true;
	}
}
