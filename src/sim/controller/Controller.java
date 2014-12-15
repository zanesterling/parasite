package Parasite.sim.controller;

import Parasite.sim.entity.Entity;

import java.util.ArrayList;

public abstract class Controller {

	// list of all entities to be manipulated by this controller
	protected ArrayList<Entity> controlledEntities;

	public Controller() {
		controlledEntities = new ArrayList<Entity>();
	}

	public Controller(Entity entity) {
		this();
		addEntity(entity);
	}

	public abstract void update();

	public void addEntity(Entity entity) {
		controlledEntities.add(entity);
	}
}
