package Parasite.sim.entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class GoonEntity extends Entity {

	public GoonEntity() { super(); }
	public GoonEntity(int x, int y) { super(x, y); }

	public void render(Graphics2D g) {
		g.rotate(heading);
		g.scale(0.75, 0.75);

		// fill main rect
		g.setColor(new Color(0, 100, 255));
		g.fillRect(-12, -12, 24, 24);

		// fill eye rect
		g.setColor(Color.BLACK);
		g.fillRect(0, -4, 12, 8);

		g.scale(1.33, 1.33);
		g.rotate(-heading);
	}
}
