package Parasite.sim.entity;

import Parasite.sim.Simulation;
import Parasite.util.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Entity {

	protected Vector2d pos;
	public Vector2d getPosition() { return pos.clone(); }
	public void setPosition(Vector2d vec) { pos.set(vec); }

	public Vector2d vel = new Vector2d();
	public double rad;

	public double maxSpeed;
	protected double lookAngle;

	public Color bodyColor;
	public boolean isPossessable;

	public boolean dead;

	public Entity() { this(0, 0); }
	public Entity(double x, double y) { this(new Vector2d(x, y)); }

	public Entity(Vector2d pos) {
		this.pos = new Vector2d(pos);
		maxSpeed = 0.1;
		isPossessable = true;
		dead = false;
	}

	public abstract void render(Graphics2D g);
	public abstract void action();

	public double getLookAngle() { return lookAngle; }
	public void setLookAngle(double lookAngle) {
		if (lookAngle > Math.PI) lookAngle -= Math.PI * 2;
		this.lookAngle = lookAngle;
	}

	public double distTo(Entity entity) {
		return distTo(entity.getPosition());
	}
	public double distTo(Vector2d loc) {
		return pos
			.clone()
			.sub(loc)
			.length();
	}

	public void die() {
		dead = true;
	}
}
