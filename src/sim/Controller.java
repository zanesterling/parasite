package Parasite.sim;

import java.util.ArrayList;

public abstract class Controller {

	private ArrayList<Entity> controlledEntities;

	public Controller() {
		controlledEntities = new ArrayList<Entity>();
	}

	public abstract void update();

	public void addEntity(Entity entity) {
		controlledEntities.add(entity);
	}
}
