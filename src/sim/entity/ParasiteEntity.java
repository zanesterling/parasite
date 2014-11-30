package Parasite.sim.entity;

import Parasite.sim.entity.Entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class ParasiteEntity extends Entity {

	public void render(Graphics2D g) {
		g.rotate(heading);

		// fill main rect
		g.setColor(Color.RED);
		g.fillRect(-6, -6, 12, 12);

		// fill eye rect
		g.setColor(Color.BLACK);
		g.fillRect(0, -2, 6, 4);

		g.rotate(-heading);
	}
}
