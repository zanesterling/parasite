package Parasite.sim;

import Parasite.sim.controller.*;
import Parasite.ui.UIEvent;

import java.util.ArrayList;

public class Simulation {

	public ArrayList<Entity> entities;
	private ArrayList<Controller> controllers;
	private PlayerController playerController;
	private Entity focusedEntity;

	// TODO add world

	public Simulation() {
		entities = new ArrayList<Entity>();
		controllers = new ArrayList<Controller>();

		initWorld();
	}

	private void initWorld() {
		Entity playerEntity = new Entity();
		playerController = new PlayerController(playerEntity);

		entities.add(playerEntity);
		controllers.add(playerController);

		setFocusedEntity(playerEntity);
	}

	public void update() {
		for (Controller controller : controllers) {
			controller.update();
		}
	}

	public void processEvent(UIEvent e) {
		playerController.processEvent(e);
	}

	public Entity getFocusedEntity() {
		return focusedEntity;
	}

	public void setFocusedEntity(Entity entity) {
		focusedEntity = entity;
	}
}
