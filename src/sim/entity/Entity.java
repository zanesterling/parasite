package Parasite.sim.entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class Entity {

	public double x, y;
	public double speed;
	public double heading;

	public Entity() {
		x = 0;
		y = 0;
		speed = 1;
	}

	public void render(Graphics2D g) {
		g.rotate(heading);
		g.setColor(Color.RED);
		g.fillRect(-5, -5, 10, 10);
		g.rotate(-heading);
	}
}
