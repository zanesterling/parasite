package Parasite.sim.controller;

import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;

import java.util.ArrayList;

public abstract class Controller {

	// list of all entities to be manipulated by this controller
	protected ArrayList<Entity> controlled;

	public Controller() {
		controlled = new ArrayList<Entity>();
	}

	public Controller(Entity entity) {
		this();
		addEntity(entity);
	}

	public abstract void update();

	public void checkForDead() {
		Simulation sim = Simulation.getInstance();

		for (int i = 0; i < controlled.size(); i++) {
			if (controlled.get(i).dead) {
				sim.entities.remove(controlled.get(i));
				controlled.remove(i--);
			}
		}
	}

	public void addEntity(Entity entity) {
		controlled.add(entity);
	}
}
