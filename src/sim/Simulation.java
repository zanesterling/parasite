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
import Parasite.sim.projectile.Projectile;

import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
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
	public static Simulation getInstance() {
		if (instance == null) instance = new Simulation();
		return instance;
	}

	// entity stuff
	public ArrayList<Entity> entities;
	public ParasiteEntity parasite;
	private Entity focusedEntity;

	// controller stuff
	private ArrayList<Controller> controllers;
	private PlayerController playerController;

	// wall stuff
	private int[][] walls;

	// projectile stuff
	public ArrayList<Projectile> projectiles;

	private Simulation() {
		entities = new ArrayList<Entity>();
		controllers = new ArrayList<Controller>();
		projectiles = new ArrayList<Projectile>();

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
		e = new GoonEntity(300, -300);
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
		// check for dead entities
		for (Controller controller : controllers) {
			controller.checkForDead();
		}

		// update entities
		for (Controller controller : controllers) {
			controller.update();
		}

		// update projectiles
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).dead) {
				projectiles.remove(i--);
				continue;
			}
			projectiles.get(i).update();
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

		// clip to appropriate shape for vision occlusion
		Shape focusedVision = getFocusedVisionShape(wallRange);

		// render entities
		for (Entity entity : entities) {
			g.translate(entity.x, -entity.y);
			entity.render(g);
			g.translate(-entity.x, entity.y);
		}

		// render projectiles
		for (Projectile projectile : projectiles) {
			g.translate(projectile.x, -projectile.y);
			projectile.render(g);
			g.translate(-projectile.x, projectile.y);
		}

		// render walls
		for (int i = wallRange[1]; i <= wallRange[3]; i++) {
			for (int j = wallRange[0]; j <= wallRange[2]; j++) {
				if (walls[i][j] == 0) continue;
				g.setColor(walls[i][j] == 1 ? WALL_COLOR : BGRD_COLOR);
				g.fillRect(j * WALL_WIDTH, i * WALL_HEIGHT,
				           WALL_WIDTH, WALL_HEIGHT);
			}
		}

		// translate back out
		g.translate(focusedEntity.x, -focusedEntity.y);
		g.translate(-ui.canvasWidth / 2, - ui.canvasHeight / 2);

		// print wallRange debug info
		if (Game.DEBUG_STATE) {
			g.drawString("Wall range", 0, 10);
			g.drawString(wallRange[0] + " " + wallRange[1], 0, 20);
			g.drawString(wallRange[2] + " " + wallRange[3], 0, 30);
		}
	}

	// get screen bounds in world coords
	private int[] getScreenBounds() {
		UI ui = UI.getInstance();

		int[] screenBounds = new int[4];
		screenBounds[0] = (int) focusedEntity.x - ui.canvasWidth / 2;
		screenBounds[1] = (int) -focusedEntity.y - ui.canvasHeight / 2;
		screenBounds[2] = (int) focusedEntity.x + ui.canvasWidth / 2;
		screenBounds[3] = (int) -focusedEntity.y + ui.canvasHeight / 2;

		return screenBounds;
	}

	// get range bounds of walls on screen
	private int[] getWallRange() {
		int[] screen = getScreenBounds();

		// get screen bounds in wall coords
		int[] wallRange = new int[4];
		wallRange[0] = Math.max(screen[0] / WALL_WIDTH, 0);
		wallRange[1] = Math.max(screen[1] / WALL_HEIGHT, 0);
		wallRange[2] = Math.min(screen[2] / WALL_WIDTH, walls[0].length);
		wallRange[3] = Math.min(screen[3] / WALL_HEIGHT, walls.length);

		return wallRange;
	}

	// find the shape of the player's vision intersected with screen
	private Shape getFocusedVisionShape(int[] wallRange) {
		ArrayList<int[]> borders = new ArrayList<int[]>();

		// add vision edges as borders
		int[] screenBounds = getScreenBounds();
		borders.add(new int[]{screenBounds[0], screenBounds[1],
		                      screenBounds[2], screenBounds[1]});
		borders.add(new int[]{screenBounds[2], screenBounds[1],
		                      screenBounds[2], screenBounds[3]});
		borders.add(new int[]{screenBounds[2], screenBounds[3],
		                      screenBounds[0], screenBounds[3]});
		borders.add(new int[]{screenBounds[0], screenBounds[3],
		                      screenBounds[0], screenBounds[1]});

		// for each visible wall:
		for (int i = wallRange[1]; i <= wallRange[3]; i++) {
			for (int j = wallRange[0]; j <= wallRange[2]; j++) {
				// add visible sides to list
				addVisibleSides(borders, j, i);
			}
		}

		// TODO generate vision shape from relevant walls
		return new Rectangle(0, 0, 0, 0);
	}

	private void addVisibleSides(ArrayList<int[]> borders,
	                             int wallCoordX, int wallCoordY) {
		// check top and left walls
		int wallX = wallCoordX * WALL_WIDTH;
		int wallY = wallCoordY * WALL_HEIGHT;
		if (focusedEntity.x < wallX) {
			borders.add(new int[]{wallX, wallY,
			                      wallX, wallY + WALL_HEIGHT});
		}
		if (focusedEntity.y < wallY) {
			borders.add(new int[]{wallX, wallY,
			                      wallX + WALL_WIDTH, wallY});
		}

		// check bottom and right walls
		wallY += WALL_HEIGHT;
		if (focusedEntity.x > wallX + WALL_WIDTH) {
			borders.add(new int[]{wallX, wallY,
			                      wallX + WALL_WIDTH, wallY});
		}

		wallX += WALL_WIDTH;
		if (focusedEntity.x < wallX) {
			borders.add(new int[]{wallX, wallY - WALL_HEIGHT,
			                      wallX, wallY});
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

	public int getWallAt(double x, double y) {
		int xcor = (int)(x / WALL_WIDTH);
		int ycor = (int)(-y / WALL_HEIGHT);

		if (xcor < 0 || xcor > walls[0].length) return 0;
		if (ycor < 0 || ycor > walls.length)    return 0;

		return walls[ycor][xcor];
	}
}
