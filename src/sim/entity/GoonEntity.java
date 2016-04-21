package Parasite.sim.entity;

import Parasite.Game;
import Parasite.sim.Simulation;
import Parasite.sim.projectile.Bullet;
import Parasite.util.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;

public class GoonEntity extends Entity {

	public static final Color DEFAULT_COLOR = new Color(0,  100, 255);
	public static final Color CAN_SEE_COLOR = new Color(80, 200, 255);

	private double visionAngle = Math.PI * 2 / 3;
	private double angleDiff;

	public GoonEntity() { this(0, 0); }
	public GoonEntity(double x, double y) {
		super(x, y);
		bodyColor = DEFAULT_COLOR;
		rad = 0.075 * 12;
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

		if (Game.DEBUG_STATE) {
			g.setColor(Color.GREEN);
			g.drawString("" + angleDiff, 30, 0);

			Vector2d posDiff = Simulation
				.getInstance()
				.parasite
				.getRenderPosition()
				.sub(getRenderPosition());
			g.drawLine(
				0, 0,
				(int)(posDiff.x),
				(int)(posDiff.y)
			);
		}
	}

	public void face(Vector2d loc) {
		lookAngle = loc
			.clone()
			.sub(pos)
			.angle();
	}

	public void moveTowards(Vector2d loc) {
		face(loc);
		vel.set(Math.cos(lookAngle), Math.sin(lookAngle));
		vel.scale(maxSpeed);
	}

	public void stop() {
		vel.set(0, 0);
	}

	public boolean canSee(Vector2d loc) {
		// check if entity is in vision arc
		double angleToPlayer = loc
			.clone()
			.sub(pos)
			.angle();
		angleDiff = Math.abs(angleToPlayer - lookAngle);
		if (angleDiff > Math.PI) angleDiff = Math.PI * 2 - angleDiff;

		boolean inVisionArc = (angleDiff <= visionAngle / 2);
		if (!Game.DEBUG_STATE && !inVisionArc) return false;

		// check if walls occlude the loc
		return !bresenhamOcclusion(loc);
	}

	// returns true if walls occlude entity
	private boolean bresenhamOcclusion(Vector2d loc) {
		Vector2d posDiff = loc.clone().sub(pos);
		boolean yDiffBigger = Math.abs(posDiff.y) > Math.abs(posDiff.x);

		int tx = (int)pos.x;
		int ty = -(int)pos.y;
		int ex = (int)loc.x;
		int ey = -(int)loc.y;

		if (tx == ex && ty == ey) return true;

		Simulation sim = Simulation.getInstance();
		int minX, maxX, minY, maxY;
		if (yDiffBigger) {
			minY = (int)Math.min(ty, ey);
			maxY = (int)Math.max(ty, ey);
			if (minY == ey) {
				minX = ex;
				maxX = tx;
			} else {
				minX = tx;
				maxX = ex;
			}

			double slope = 1.0 * (maxX - minX) / (maxY - minY);
			for (int i = minY; i <= maxY; i++) {
				int xcor = minX + (int)((i - minY) * slope);
				if (sim.level.getWall(xcor, i) > 0) {
					return true;
				}
				if (Game.DEBUG_STATE) sim.level.setWall(xcor, i, -1);
			}
		} else {
			minX = (int)Math.min(tx, ex);
			maxX = (int)Math.max(tx, ex);
			if (minX == ex) {
				minY = ey;
				maxY = ty;
			} else {
				minY = ty;
				maxY = ey;
			}

			double slope = 1.0 * (maxY - minY) / (maxX - minX);
			for (int i = minX; i <= maxX; i++) {
				int ycor = minY + (int)((i - minX) * slope);
				if (sim.level.getWall(i, ycor) > 0) {
					return true;
				}
				if (Game.DEBUG_STATE) sim.level.setWall(i, ycor, -1);
			}
		}

		return false;
	}

	// action: shoot!
	public void action() {
		Vector2d bulletLoc = new Vector2d(
			Math.cos(lookAngle),
			Math.sin(lookAngle)
		);
		bulletLoc.scale(rad + Bullet.RAD);
		bulletLoc.add(this.pos);

		Bullet bullet = new Bullet(bulletLoc, -lookAngle);
		Simulation.getInstance().projectiles.add(bullet);
	}
}
