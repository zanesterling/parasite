package Parasite.sim.controller;

import Parasite.Game;
import Parasite.sim.Location;
import Parasite.ui.UIEvent;
import Parasite.ui.EventCode;
import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.ParasiteEntity;

import java.awt.Color;
import java.util.ArrayList;

public class PlayerController extends Controller {

	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;

	private Entity mainHost;

	// Debug vars
	private boolean colliding;

	public PlayerController(Entity entity) {
		super(entity);
		mainHost = entity;
	}

	public void update() {
		Entity mainHost = controlled.get(controlled.size() - 1);

		// isLeaping logic (lizards!)
		// check to see if mainHost is a leaper, to consolidate next if
		ParasiteEntity leaper = null;
		if (mainHost instanceof ParasiteEntity)
			leaper = (ParasiteEntity) mainHost;
		if (leaper != null && leaper.isLeaping) {
			double lookAngle = leaper.getLookAngle();

			// set isLeaping velocities
			mainHost.vx =  Math.cos(lookAngle) * leaper.maxSpeed * 8;
			mainHost.vy = -Math.sin(lookAngle) * leaper.maxSpeed * 8;

			// if leap timed out, end leap
			if (System.currentTimeMillis() - leaper.leapStartTime >=
				leaper.leapMaxDuration) {
				leaper.isLeaping = false;
			}
		} else {
			// update mainHost velocities like normal
			mainHost.vx = 0;
			mainHost.vy = 0;

			if (moveUp)    mainHost.vy += mainHost.maxSpeed;
			if (moveLeft)  mainHost.vx -= mainHost.maxSpeed;
			if (moveDown)  mainHost.vy -= mainHost.maxSpeed;
			if (moveRight) mainHost.vx += mainHost.maxSpeed;
		}

		// set entity velocities to mainHost velocities
		for (Entity entity : controlled) {
			entity.vx = mainHost.vx;
			entity.vy = mainHost.vy;
		}

		// detect collisons
		Simulation sim = Simulation.getInstance();
		ArrayList<Entity> allEntities = sim.entities;
		for (Entity entity : allEntities) {
			if (isCollidingWith(entity)) {
				collide(entity);
			}
		}

		// change bodyColor if colliding
		if (mainHost instanceof ParasiteEntity) {
			if (colliding) {
				mainHost.bodyColor = ParasiteEntity.COLLIDING_COLOR;
			} else {
				mainHost.bodyColor = ParasiteEntity.DEFAULT_COLOR;
			}
		}
	}

	public boolean isCollidingWith(Entity entity) {
		Location hostLoc = mainHost.getLocation();
		Location entLoc = entity.getLocation();
		double dx = entLoc.x - hostLoc.x;
		double dy = entLoc.y - hostLoc.y;
		double distSq = dx * dx + dy * dy;

		colliding = distSq < Math.pow(mainHost.rad + entity.rad, 2);
		return colliding;
	}

	public void collide(Entity entity) {
		colliding = true;
		// possession logic
		if (entity.isPossessable && mainHost instanceof ParasiteEntity) {
			ParasiteEntity parasite = (ParasiteEntity) mainHost;
			if (parasite.isLeaping) {
				// possess
				parasite.isLeaping = false;
				parasite.isPossessing = true;
				addEntity(entity);

				// we're done leaping: stop
				parasite.vx = 0;
				parasite.vy = 0;
			}
		}
	}

	public void addEntity(Entity entity) {
		// move entity to mainHost's center
		if (mainHost != null) {
			Location hostLoc = mainHost.getLocation();
			Location entLoc = entity.getLocation();

			entLoc.x = hostLoc.x;
			entLoc.y = hostLoc.y;
			entity.setLocation(entLoc);
			entity.vx = 0;
			entity.vy = 0;
		} else {
			System.out.println("mainHost was null");
		}

		entity.isPossessable = false;
		controlled.add(entity);
		mainHost = entity;
	}

	// exit the current host, assuming there is one
	public void popControl() {
		if (controlled.size() > 1) {
			mainHost.isPossessable = true;
			controlled.remove(controlled.size() - 1);

			mainHost = controlled.get(controlled.size() - 1);

			// if we just de-possessed, toggle mainHost.isPossessing
			if (mainHost instanceof ParasiteEntity)
				((ParasiteEntity) mainHost).isPossessing = false;
		}
	}

	public void processEvent(UIEvent e) {
		switch (e.eventCode) {
			case MOVE_UP:
				moveUp = true;
				break;
			case MOVE_LEFT:
				moveLeft = true;
				break;
			case MOVE_DOWN:
				moveDown = true;
				break;
			case MOVE_RIGHT:
				moveRight = true;
				break;
			case STOP_UP:
				moveUp = false;
				break;
			case STOP_LEFT:
				moveLeft = false;
				break;
			case STOP_DOWN:
				moveDown = false;
				break;
			case STOP_RIGHT:
				moveRight = false;
				break;
			case ACTION:
				mainHost.action();
				break;
			case LOOK:
				for (Entity entity : controlled) {
					entity.setLookAngle(e.dargs[0]); // look at that angle
				}
				break;
			case POP:
				popControl();
				break;
			case TOGGLE_DEBUG:
				Game.DEBUG_STATE = !Game.DEBUG_STATE;
				break;
		}
	}
}
