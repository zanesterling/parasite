package Parasite.sim;

import java.awt.Color;
import java.awt.Graphics2D;

public class Entity {

	public double x, y;
	public double speed;

	public Entity() {
		x = 0;
		y = 0;
		speed = 1;
	}

	public void render(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(-5, -5, 10, 10);
	}
}
