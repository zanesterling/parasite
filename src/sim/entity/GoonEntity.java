package Parasite.sim.entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class GoonEntity extends Entity {

	public static final Color DEFAULT_COLOR = new Color(0, 100, 255);

	private double visionAngle = Math.PI * 2 / 3;
	private double angleToPlayer;
	private double angleDiff;

	public GoonEntity() { this(0, 0); }
	public GoonEntity(int x, int y) {
		super(x, y);
		bodyColor = DEFAULT_COLOR;
		rad = 12;
	}

	public void render(Graphics2D g) {
		g.rotate(lookAngle);
		g.scale(0.75, 0.75);

		// fill main rect
		g.setColor(bodyColor);
		g.fillRect(-12, -12, 24, 24);

		// fill eye rect
		g.setColor(Color.BLACK);
		g.fillRect(0, -4, 12, 8);

		g.scale(1.33, 1.33);
		g.rotate(-lookAngle);

		if (Parasite.Game.DEBUG)
			g.drawString("" + angleDiff, 30, 0);
	}

	public boolean canSee(Entity entity) {
		angleToPlayer = Math.atan2(y - entity.y, entity.x - x);

		angleDiff = Math.abs(angleToPlayer - lookAngle);
		if (angleDiff > Math.PI) angleDiff = Math.PI * 2 - angleDiff;

		return angleDiff <= visionAngle / 2;
	}

	public void action() {}
}
