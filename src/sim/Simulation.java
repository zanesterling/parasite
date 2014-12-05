package Parasite.sim;

import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;
import Parasite.sim.entity.ParasiteEntity;
import Parasite.sim.controller.Controller;
import Parasite.sim.controller.PlayerController;
import Parasite.sim.controller.AIController;
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
		// init player entity, controller
		Entity playerEntity = new ParasiteEntity();
		playerController = new PlayerController(playerEntity);
		entities.add(playerEntity);
		controllers.add(playerController);

		// focus on player entity
		setFocusedEntity(playerEntity);

		GoonEntity e = new GoonEntity(300, -200);
		controllers.add(new AIController(e));
		entities.add(e);
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
