package Parasite.sim.controller;

import Parasite.sim.Entity;

import java.util.ArrayList;

public abstract class Controller {

	protected ArrayList<Entity> controlledEntities;

	public Controller() {
		controlledEntities = new ArrayList<Entity>();
	}

	public abstract void update();

	public void addEntity(Entity entity) {
		controlledEntities.add(entity);
	}
}
