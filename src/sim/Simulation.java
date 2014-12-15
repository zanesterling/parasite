package Parasite.sim;

import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;
import Parasite.sim.entity.ParasiteEntity;
import Parasite.sim.controller.Controller;
import Parasite.sim.controller.PlayerController;
import Parasite.sim.controller.AIController;
import Parasite.ui.UIEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Simulation {

	// singleton stuff
	private static Simulation instance;
	public static Simulation getSimulation() {
		if (instance == null) instance = new Simulation();
		return instance;
	}

	public ArrayList<Entity> entities;
	public ParasiteEntity parasite;

	private ArrayList<Controller> controllers;
	private PlayerController playerController;
	private Entity focusedEntity;
	private int[][] walls;

	private Simulation() {
		entities = new ArrayList<Entity>();
		controllers = new ArrayList<Controller>();

		initWorld();
	}

	// initialize entities, controllers, walls, etc.
	private void initWorld() {
		// init parasite, player controller
		parasite = new ParasiteEntity();
		entities.add(parasite);
		playerController = new PlayerController(parasite);
		controllers.add(playerController);

		// focus on parasite
		setFocusedEntity(parasite);

		// make goon to play with
		GoonEntity e = new GoonEntity(300, -200);
		controllers.add(new AIController(e));
		entities.add(e);

		// make walls
		loadLevel("res/level1.lvl");
	}

	private void loadLevel(String fn) {
		Scanner sc;
		try {
			sc = new Scanner(new File(fn));
		} catch (FileNotFoundException e) {
			System.out.println("could not load file: " + fn);
			e.printStackTrace();
			return;
		}

		// opened level, let's read
		int width = sc.nextInt();
		int height = sc.nextInt();
		walls = new int[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				walls[i][j] = sc.nextInt();
				//System.out.print(walls[i][j] + " ");
			}
			//System.out.println();
		}
	}

	// run a game-tick in the world
	public void update() {
		for (Controller controller : controllers) {
			controller.update();
		}
	}

	// process a UIEvent, deal with ramifications
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
