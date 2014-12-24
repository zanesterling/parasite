package Parasite.sim.controller;

import Parasite.ui.UIEvent;
import Parasite.ui.EventCode;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.ParasiteEntity;

public class PlayerController extends Controller {

	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;

	private Entity mainHost;

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
			for (Entity entity : controlled) {
				entity.x += Math.cos(lookAngle) *
					        leaper.maxSpeed * 8;
				entity.y -= Math.sin(lookAngle) *
					        leaper.maxSpeed * 8;
			}

			// TODO detect collison, if so possess

			// if leap timed out, end leap
			if (System.currentTimeMillis() - leaper.leapStartTime >=
				leaper.leapMaxDuration) {
				leaper.leaping = false;
			}
		} else {
			for (Entity entity : controlled) {
				entity.vx = 0;
				entity.vy = 0;

				if (moveUp)    entity.vy += mainHost.maxSpeed;
				if (moveLeft)  entity.vx -= mainHost.maxSpeed;
				if (moveDown)  entity.vy -= mainHost.maxSpeed;
				if (moveRight) entity.vx += mainHost.maxSpeed;

				entity.x += entity.vx;
				entity.y += entity.vy;
			}
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
		}
	}
}
