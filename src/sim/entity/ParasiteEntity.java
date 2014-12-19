package Parasite.sim.entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class ParasiteEntity extends Entity {

	public ParasiteEntity() { super(); }
	public ParasiteEntity(int x, int y) { super(x, y); }

	public void render(Graphics2D g) {
		g.rotate(lookAngle);

		// fill main rect
		g.setColor(Color.RED);
		g.fillRect(-6, -6, 12, 12);

		// fill eye rect
		g.setColor(Color.BLACK);
		g.fillRect(0, -2, 6, 4);

		g.rotate(-lookAngle);
	}
}
