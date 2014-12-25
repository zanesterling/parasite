package Parasite.sim.controller;

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

		// check to see if mainHost is a leaper, to consolidate next if
		ParasiteEntity leaper = null;
		if (mainHost instanceof ParasiteEntity)
			leaper = (ParasiteEntity) mainHost;

		if (leaper != null && leaper.leaping) {
			double lookAngle = leaper.getLookAngle();

			// move entities in leap direction
			mainHost.vx =  Math.cos(lookAngle) * leaper.maxSpeed * 8;
			mainHost.vy = -Math.sin(lookAngle) * leaper.maxSpeed * 8;

			// if leap timed out, end leap
			if (System.currentTimeMillis() - leaper.leapStartTime >=
				leaper.leapMaxDuration) {
				leaper.leaping = false;
			}
		} else {
			mainHost.vx = 0;
			mainHost.vy = 0;

			if (moveUp)    mainHost.vy += mainHost.maxSpeed;
			if (moveLeft)  mainHost.vx -= mainHost.maxSpeed;
			if (moveDown)  mainHost.vy -= mainHost.maxSpeed;
			if (moveRight) mainHost.vx += mainHost.maxSpeed;
		}

		for (Entity entity : controlled) {
			entity.vx = mainHost.vx;
			entity.vy = mainHost.vy;

			entity.x += entity.vx;
			entity.y += entity.vy;
		}

		// detect collisons
		Simulation sim = Simulation.getSimulation();
		ArrayList<Entity> allEntities = sim.entities;
		for (Entity entity : allEntities) {
			if (collidingWith(entity)) {
				collide(entity);
			}
		}

		if (mainHost instanceof ParasiteEntity) {
			if (colliding) {
				mainHost.bodyColor = new Color(255, 100, 100);
			} else {
				mainHost.bodyColor = Color.RED;
			}
		}

	}

	public boolean collidingWith(Entity entity) {
		double dx = entity.x - mainHost.x;
		double dy = entity.y - mainHost.y;
		double distSq = dx * dx + dy * dy;

		colliding = distSq < Math.pow(mainHost.rad + entity.rad, 2);
		return colliding;
	}

	public void collide(Entity entity) {
		// check if entity is already controlled
		for (Entity e : controlled)
			if (e == entity)
				return;

		colliding = true;
		// possession logic
		if (!entity.isPossessed && mainHost instanceof ParasiteEntity) {
			ParasiteEntity host = (ParasiteEntity) mainHost;
			if (host.leaping) {
				// possess
				host.leaping = false;
				entity.isPossessed = true;
				addEntity(entity);
			}
		}
	}

	public void addEntity(Entity entity) {
		// move entity to mainHost's center
		if (mainHost != null) {
			entity.x = mainHost.x;
			entity.y = mainHost.y;
		}

		controlled.add(entity);
		mainHost = entity;
	}

	// exit the current host, assuming there is one
	public void popControl() {
		if (controlled.size() > 1) {
			mainHost.isPossessed = false;
			controlled.remove(controlled.size() - 1);
			mainHost = controlled.get(controlled.size() - 1);
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
		}
	}
}
