package Parasite.sim.projectile;

import Parasite.sim.entity.Entity;
import Parasite.util.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends Projectile {

	public static double RAD = 0.2;
	private static double SPEED = 5;

	public Bullet(Vector2d pos, double angle) {
		super(pos, angle, SPEED);
		rad = RAD;
	}

	public void impact(Entity entity) {
		entity.die();
		die();
	}

	public void render(Graphics2D g) {
		g.rotate(angle);

		g.setColor(Color.RED);
		g.fillRect(-2, -2, 4, 4);

		g.rotate(-angle);
	}
}
