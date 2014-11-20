package Parasite.sim;

import Parasite.ui.UIEvent;

import java.util.ArrayList;

public class Simulation {

	private ArrayList<Entity> entities;
	private ArrayList<Controller> controllers;
	private PlayerController playerController;

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

	public void processEvent(UIEvent e) {
		playerController.processEvent(e);
	}
}