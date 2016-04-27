package Parasite.sim;

import Parasite.Game;
import Parasite.sim.Level;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;
import Parasite.sim.entity.ParasiteEntity;
import Parasite.sim.controller.Controller;
import Parasite.sim.controller.PlayerController;
import Parasite.sim.controller.GoonController;
import Parasite.sim.projectile.Projectile;
import Parasite.ui.UI;
import Parasite.ui.UIEvent;
import Parasite.util.Vector2d;
import Parasite.util.Line;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Simulation {
	// simulation constants
	public static final int WALL_WIDTH  = 1;
	public static final int WALL_HEIGHT = 1;

	// render constants
	public static final double RENDER_SCALE = 10;
	private static final Color WALL_COLOR = Color.BLACK;
	private static final Color BGRD_COLOR = Color.WHITE;
	private static final int WALL_COLORS_OFFSET = 1;
	private static final Color[] WALL_COLORS = new Color[]{
		Color.RED,
		Color.WHITE,
		Color.BLACK
	};

	// singleton stuff
	private static Simulation instance;
	public static Simulation getInstance() {
		if (instance == null) instance = new Simulation();
		return instance;
	}

	public ArrayList<Entity> entities;
	public ParasiteEntity parasite;
	private Entity focusedEntity;

	private ArrayList<Controller> controllers;
	private PlayerController playerController;

	public Level level;

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
		parasite = new ParasiteEntity(5, 5);
		entities.add(parasite);
		playerController = new PlayerController(parasite);
		controllers.add(playerController);

		// focus on parasite
		setFocusedEntity(parasite);

		// make goon to play with
		GoonEntity entity = new GoonEntity(30, 20);
		controllers.add(new GoonController(entity));
		entities.add(entity);
		entity = new GoonEntity(30, 30);
		controllers.add(new GoonController(entity));
		entities.add(entity);

		// make walls
		level = new Level("res/level3.lvl");
	}

	// run a game-tick in the world
	public void update() {
		// check for dead entities
		for (Controller controller : controllers) {
			controller.checkForDead();
		}

		// update entity behaviors
		for (Controller controller : controllers) {
			controller.update();
		}

		// move entities
		for (Entity entity : entities) {
			entity.setPosition(entity.getPosition().add(entity.vel));
			if (level.getWallAt(entity.getPosition()) != 0) {
				entity.setPosition(entity.getPosition().sub(entity.vel));
			}
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

		// clip to appropriate shape for vision occlusion
		Shape visionShape = VisionShape.getShape(
			level,
			focusedEntity.getPosition()
		);
		if (visionShape != null) {
			g.clip(visionShape);
		}
		g.setColor(Color.WHITE);
		g.fillRect(
			-ui.canvasWidth / 2, -ui.canvasHeight / 2,
			ui.canvasWidth, ui.canvasHeight
		);

		if (Game.DEBUG_STATE) {
			g.setColor(Color.RED);
			for (Line ray : VisionShape.rays) {
				g.drawLine(
					(int)ray.p1.x, (int)ray.p1.y,
					(int)ray.dim.x, (int)ray.dim.y
				);
			}
		}

		// center on the focused entity
		Vector2d focLoc = focusedEntity.getRenderPosition();
		g.translate(-focLoc.x, -focLoc.y);

		// render entities
		for (Entity entity : entities) {
			Vector2d pos = entity.getRenderPosition();
			g.translate(pos.x, pos.y);
			entity.render(g);
			g.translate(-pos.x, -pos.y);
		}

		// render projectiles
		for (Projectile projectile : projectiles) {
			Vector2d pos = projectile.pos.clone().scale(RENDER_SCALE);
			g.translate(pos.x, pos.y);
			projectile.render(g);
			g.translate(-pos.x, -pos.y);
		}

		// render walls
		g.scale(RENDER_SCALE, RENDER_SCALE);
		int[] wallRange = getWallRange();
		for (int i = wallRange[1]; i <= wallRange[3]; i++) {
			for (int j = wallRange[0]; j <= wallRange[2]; j++) {
				if (level.walls[i][j] == 0) continue;

				g.setColor(
					WALL_COLORS[level.walls[i][j] + WALL_COLORS_OFFSET]
				);
				g.fillRect(j, i, 1, 1);
			}
		}
		g.scale(1 / RENDER_SCALE, 1 / RENDER_SCALE);

		// translate back out
		g.translate(focLoc.x, focLoc.y);
		g.translate(-ui.canvasWidth / 2, - ui.canvasHeight / 2);

		if (Game.DEBUG_STATE) {
			// print wallRange debug info
			g.drawString("Wall range", 0, 10);
			g.drawString(wallRange[0] + " " + wallRange[1], 0, 20);
			g.drawString(wallRange[2] + " " + wallRange[3], 0, 30);

			// reload level to fix vision debugging junk
			level.reload();
		}
	}

	// get screen bounds in world coords
	private int[] getScreenBounds() {
		UI ui = UI.getInstance();
		Vector2d focLoc = focusedEntity.getPosition();

		int[] screenBounds = new int[4];
		screenBounds[0] = (int) focLoc.x - ui.canvasWidth / 2;
		screenBounds[1] = (int) -focLoc.y - ui.canvasHeight / 2;
		screenBounds[2] = (int) focLoc.x + ui.canvasWidth / 2;
		screenBounds[3] = (int) -focLoc.y + ui.canvasHeight / 2;

		return screenBounds;
	}

	// get range bounds of walls on screen
	private int[] getWallRange() {
		int[] screen = getScreenBounds();

		// get screen bounds in wall coords
		int[] wallRange = new int[4];
		wallRange[0] = Math.max(screen[0] / WALL_WIDTH, 0);
		wallRange[1] = Math.max(screen[1] / WALL_HEIGHT, 0);
		wallRange[2] = Math.min(
			screen[2] / WALL_WIDTH,
			level.walls[0].length - 1
		);
		wallRange[3] = Math.min(
			screen[3] / WALL_HEIGHT,
			level.walls.length - 1
		);

		return wallRange;
	}

	// find the shape of the player's vision intersected with screen
	private Shape getFocusedVisionShape(int[] wallRange) {
		ArrayList<int[]> borders = new ArrayList<int[]>();

		// add vision edges as borders
		int[] screenBounds = getScreenBounds();
		borders.add(new int[]{
			screenBounds[0], screenBounds[1],
			screenBounds[2], screenBounds[1]
		});
		borders.add(new int[]{
			screenBounds[2], screenBounds[1],
			screenBounds[2], screenBounds[3]
		});
		borders.add(new int[]{
			screenBounds[2], screenBounds[3],
			screenBounds[0], screenBounds[3]
		});
		borders.add(new int[]{
			screenBounds[0], screenBounds[3],
			screenBounds[0], screenBounds[1]
		});

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

	private void addVisibleSides(
		ArrayList<int[]> borders,
		int wallCoordX,
		int wallCoordY
	) {
		Vector2d focLoc = focusedEntity.getPosition();

		// check top and left walls
		int wallX = wallCoordX * WALL_WIDTH;
		int wallY = wallCoordY * WALL_HEIGHT;
		if (focLoc.x < wallX) {
			borders.add(new int[]{
				wallX, wallY,
				wallX, wallY + WALL_HEIGHT
			});
		}
		if (focLoc.y < wallY) {
			borders.add(new int[]{
				wallX, wallY,
				wallX + WALL_WIDTH, wallY
			});
		}

		// check bottom and right walls
		wallY += WALL_HEIGHT;
		if (focLoc.x > wallX + WALL_WIDTH) {
			borders.add(new int[]{
				wallX, wallY,
				wallX + WALL_WIDTH, wallY
			});
		}

		wallX += WALL_WIDTH;
		if (focLoc.x < wallX) {
			borders.add(new int[]{
				wallX, wallY - WALL_HEIGHT,
				wallX, wallY
			});
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
