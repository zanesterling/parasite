package Parasite.sim.controller;

import Parasite.sim.entity.Entity;
import Parasite.ui.UIEvent;
import Parasite.ui.EventCode;

public class PlayerController extends Controller {

	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;

	public PlayerController(Entity entity) {
		super(entity);
	}

	public void update() {
		for (Entity entity : controlled) {
			if (moveUp)    entity.y += entity.maxSpeed;
			if (moveLeft)  entity.x -= entity.maxSpeed;
			if (moveDown)  entity.y -= entity.maxSpeed;
			if (moveRight) entity.x += entity.maxSpeed;
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
			case POSSESS:
				System.out.println("Possessing");
				break;
		}
	}
}
