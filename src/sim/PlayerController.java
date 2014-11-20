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
		// TODO implement PlayerController.update()
	}

	public void processEvent(UIEvent e) {
		switch (e.eventCode) {
			case UIEvent.MOVE_UP:
				moveUp = true;
			case UIEvent.MOVE_LEFT:
				moveLeft = true;
			case UIEvent.MOVE_DOWN:
				moveDown = true;
			case UIEvent.MOVE_RIGHT:
				moveRight = true;
			case UIEvent.STOP_UP:
				moveUp = false;
			case UIEvent.STOP_LEFT:
				moveLeft = false;
			case UIEvent.STOP_DOWN:
				moveDown = false;
			case UIEvent.STOP_RIGHT:
				moveRight = false;
		}
	}
}
