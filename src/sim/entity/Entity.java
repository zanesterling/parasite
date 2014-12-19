package Parasite.sim.entity;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Entity {

	public double x, y;
	public double vx, vy;

	public double maxSpeed;
	public double lookAngle;

	public Entity() {
		this(0, 0);
	}

	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
		maxSpeed = 1;
	}

	public abstract void render(Graphics2D g);
	public abstract void action();
}
