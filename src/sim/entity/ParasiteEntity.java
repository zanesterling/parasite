package Parasite.sim.entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class ParasiteEntity extends Entity {

	public boolean leaping;
	public long leapStartTime;
	public long leapMaxDuration;

	public ParasiteEntity() {
		super();
		leaping = false;
		leapMaxDuration = 200;
	}

	public ParasiteEntity(int x, int y) {
		super(x, y);
		leaping = false;
		leapMaxDuration = 200;
	}

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

	// action is: possession!
	public void action() {
		leaping = true;
		leapStartTime = System.currentTimeMillis();
	}

	public void setLookAngle(double lookAngle) {
		if (!leaping) super.setLookAngle(lookAngle);
	}
}
