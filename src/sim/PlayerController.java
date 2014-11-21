package Parasite.sim;

import Parasite.ui.UIEvent;

public class PlayerController extends Controller {

	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;

	public PlayerController(Entity entity) {
		super();
		addEntity(entity);
	}

	public void update() {
		for (Entity entity : controlledEntities) {
			if (moveUp)    entity.y += entity.speed;
			if (moveLeft)  entity.x -= entity.speed;
			if (moveDown)  entity.y -= entity.speed;
			if (moveRight) entity.x += entity.speed;
		}
	}

	public void processEvent(UIEvent e) {
		switch (e.eventCode) {
			case UIEvent.MOVE_UP:
				moveUp = true;
				break;
			case UIEvent.MOVE_LEFT:
				moveLeft = true;
				break;
			case UIEvent.MOVE_DOWN:
				moveDown = true;
				break;
			case UIEvent.MOVE_RIGHT:
				moveRight = true;
				break;
			case UIEvent.STOP_UP:
				moveUp = false;
				break;
			case UIEvent.STOP_LEFT:
				moveLeft = false;
				break;
			case UIEvent.STOP_DOWN:
				moveDown = false;
				break;
			case UIEvent.STOP_RIGHT:
				moveRight = false;
				break;
		}
	}
}
