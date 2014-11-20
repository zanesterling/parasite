package Parasite.sim;

import java.util.ArrayList;

public class Simulation {

	ArrayList<Entity> entities;
	ArrayList<Controller> controllers;
	PlayerController playerController;

	// TODO add world

	public Simulation() {
		entities = new ArrayList<Entity>();
		controllers = new ArrayList<Controller>();
	}

	private void initWorld() {
		Entity playerEntity = new Entity();
		playerController = new PlayerController(playerEntity);

		entities.add(playerEntity);
	}

	public void update() {
		for (Controller controller : controllers) {
			controller.update();
		}
	}
}
