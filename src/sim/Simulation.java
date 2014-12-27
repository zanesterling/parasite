package Parasite.sim;

import Parasite.Game;
import Parasite.ui.UI;
import Parasite.ui.UIEvent;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;
import Parasite.sim.entity.ParasiteEntity;
import Parasite.sim.controller.Controller;
import Parasite.sim.controller.PlayerController;
import Parasite.sim.controller.GoonController;

import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Scanner;
import java.util.ArrayList;

public class Simulation {

	private static final int WALL_WIDTH  = 40;
	private static final int WALL_HEIGHT = 40;
	private static final Color WALL_COLOR = Color.BLACK;
	private static final Color BGRD_COLOR = Color.WHITE;

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
		controllers.add(new GoonController(e));
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

	public void render(Graphics2D g) {
		UI ui = UI.getInstance();

		// shift origin to center-screen
		g.translate(ui.canvasWidth / 2, ui.canvasHeight / 2);

		// center on the focused entity
		g.translate(-focusedEntity.x, focusedEntity.y);

		// get wall range to render
		int[] wallRange = getWallRange();

		// render walls
		for (int i = wallRange[1]; i < wallRange[3]; i++) {
			for (int j = wallRange[0]; j < wallRange[2]; j++) {
				g.setColor(walls[i][j] == 1 ? WALL_COLOR : BGRD_COLOR);
				g.fillRect(j * WALL_WIDTH, i * WALL_HEIGHT,
				           WALL_WIDTH, WALL_HEIGHT);
			}
		}

		for (Entity entity : entities) {
			g.translate(entity.x, -entity.y);
			entity.render(g);
			g.translate(-entity.x, entity.y);
		}

		g.translate(focusedEntity.x, -focusedEntity.y);
		g.translate(-ui.canvasWidth / 2, - ui.canvasHeight / 2);

		if (Game.DEBUG_STATE) {
			g.drawString("Wall range", 0, 10);
			g.drawString(wallRange[0] + " " + wallRange[1], 0, 20);
			g.drawString(wallRange[2] + " " + wallRange[3], 0, 30);
		}
	}

	// get range bounds of walls on screen
	public int[] getWallRange() {
		UI ui = UI.getInstance();

		// get screen bounds in world coords
		int minX = (int) focusedEntity.x - ui.canvasWidth / 2;
		int minY = (int) -focusedEntity.y - ui.canvasHeight / 2;
		int maxX = (int) focusedEntity.x + ui.canvasWidth / 2;
		int maxY = (int) -focusedEntity.y + ui.canvasHeight / 2;

		// get screen bounds in wall coords
		int[] wallRange = new int[4];
		wallRange[0] = Math.max(minX / WALL_WIDTH, 0);
		wallRange[1] = Math.max(minY / WALL_HEIGHT, 0);
		wallRange[2] = Math.min(maxX / WALL_WIDTH + 1, walls[0].length);
		wallRange[3] = Math.min(maxY / WALL_HEIGHT + 1, walls.length);

		return wallRange;
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
