package Parasite.sim.projectile;

import Parasite.sim.entity.Entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends Projectile {

	private static double SPEED = 5;

	public Bullet(double x, double y, double angle) {
		super(x, y, Math.cos(angle) * SPEED, Math.sin(angle) * SPEED);
		rad = 2;
	}

	public void impact(Entity entity) {
		entity.die();
	}

	public void render(Graphics2D g) {
		g.rotate(angle);

		g.setColor(Color.RED);
		g.fillRect(-2, -2, 4, 4);

		g.rotate(-angle);
	}
}