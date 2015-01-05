package Parasite.sim.entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class ParasiteEntity extends Entity {

	public static final Color DEFAULT_COLOR = Color.RED;
	public static final Color COLLIDING_COLOR = new Color(255, 150, 150);

	// leap vars
	public boolean isLeaping;
	public long leapStartTime;
	public long leapMaxDuration;

	public ParasiteEntity() { this(0, 0); }

	public ParasiteEntity(int x, int y) {
		super(x, y);

		rad = 6;
		maxSpeed = 4;
		isLeaping = false;
		leapMaxDuration = 100;
		bodyColor = DEFAULT_COLOR;
	}

	public void render(Graphics2D g) {
		g.rotate(lookAngle);

		// fill main rect
		g.setColor(bodyColor);
		g.fillRect(-6, -6, 12, 12);

		// fill eye rect
		g.setColor(Color.BLACK);
		g.fillRect(0, -2, 6, 4);

		// draw legs
		g.setColor(bodyColor);
		if (isLeaping) {
			// reaching out
			g.fillRect( 6, -4, 5, 3);
			g.fillRect( 6,  1, 5, 3);
			g.fillRect(-9, -4, 3, 3);
			g.fillRect(-9,  1, 3, 3);
		} else {
			// retracted
			g.fillRect( 6, -4, 3, 3);
			g.fillRect( 6,  1, 3, 3);
			g.fillRect(-7, -4, 1, 3);
			g.fillRect(-7,  1, 1, 3);
		}

		g.rotate(-lookAngle);
	}

	// action is: possession!
	public void action() {
		if (!isLeaping) {
			isLeaping = true;
			leapStartTime = System.currentTimeMillis();
		}
	}

	public void setLookAngle(double lookAngle) {
		if (!isLeaping) super.setLookAngle(lookAngle);
	}
}
