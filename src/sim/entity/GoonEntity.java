package Parasite.sim.entity;

import Parasite.Game;
import Parasite.sim.Location;
import Parasite.sim.Simulation;
import Parasite.sim.projectile.Bullet;

import java.awt.Color;
import java.awt.Graphics2D;

public class GoonEntity extends Entity {

	public static final Color DEFAULT_COLOR = new Color(0,  100, 255);
	public static final Color CAN_SEE_COLOR = new Color(80, 200, 255);

	private double visionAngle = Math.PI * 2 / 3;
	private double angleToPlayer;
	private double angleDiff;

	public GoonEntity() { this(0, 0); }
	public GoonEntity(int x, int y) {
		super(x, y);
		bodyColor = DEFAULT_COLOR;
		rad = 0.75 * 12;
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
			Entity parasite = Simulation.getInstance().parasite;
			Location parasLoc = parasite.getLocation();
			g.drawLine(0, 0, (int)(parasLoc.x - x), (int)(y - parasLoc.y));
		}
	}

	public void face(Location loc) {
		lookAngle = Math.atan2(y - loc.y, loc.x - x);
	}

	public void moveTowards(Location loc) {
		lookAngle = Math.atan2(y - loc.y, loc.x - x);
		vx = maxSpeed * Math.cos(lookAngle);
		vy = maxSpeed * Math.sin(-lookAngle);
	}

	public void stop() {
		vx = 0;
		vy = 0;
	}

	public boolean canSee(Location loc) {
		// check if entity is in vision arc
		angleToPlayer = Math.atan2(y - loc.y, loc.x - x);
		angleDiff = Math.abs(angleToPlayer - lookAngle);
		if (angleDiff > Math.PI) angleDiff = Math.PI * 2 - angleDiff;

		boolean inVisionArc = (angleDiff <= visionAngle / 2);
		if (!Game.DEBUG_STATE && !inVisionArc) return false;

		// check if walls occlude the loc
		return !bresenhamOcclusion(loc);
	}

	// returns true if walls occlude entity
	private boolean bresenhamOcclusion(Location loc) {
		int xDiff = (int) Math.abs((int)loc.x / Simulation.WALL_WIDTH -
		                           (int)x / Simulation.WALL_WIDTH);
		int yDiff = (int) Math.abs((int)loc.y / Simulation.WALL_HEIGHT -
		                           (int)y / Simulation.WALL_HEIGHT);
		boolean yDiffBigger = Math.abs(yDiff) > Math.abs(xDiff);

		int tx = (int)x / Simulation.WALL_WIDTH;
		int ex = (int)loc.x / Simulation.WALL_WIDTH;
		int ty = -(int)y / Simulation.WALL_HEIGHT;
		int ey = -(int)loc.y / Simulation.WALL_HEIGHT; 

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
				if (sim.getWall(xcor, i) > 0) {
					return true;
				}
				if (Game.DEBUG_STATE) sim.setWall(xcor, i, -1);
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
				if (sim.getWall(i, ycor) > 0) {
					return true;
				}
				if (Game.DEBUG_STATE) sim.setWall(i, ycor, -1);
			}
		}

		return false;
	}

	public double distTo(Location loc) {
		return Math.sqrt(Math.pow(loc.x - x, 2) + Math.pow(loc.y - y, 2));
	}

	// action: shoot!
	public void action() {
		double x = this.x + Math.cos(-lookAngle) * (rad + Bullet.RAD);
		double y = this.y + Math.sin(-lookAngle) * (rad + Bullet.RAD);

		Bullet bullet = new Bullet(x, y, -lookAngle);
		Simulation.getInstance().projectiles.add(bullet);
	}
}
