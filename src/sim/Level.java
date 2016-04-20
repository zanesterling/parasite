package Parasite.sim;

import Parasite.util.Vector2d;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level {
	public int[][] walls;

	private String fn;

	public Level(String fn) {
		this.fn = fn;
		reload();
	}

	public void reload() {
		try {
			Scanner sc = new Scanner(new File(fn));

			// opened level, let's read
			int width = sc.nextInt();
			int height = sc.nextInt();
			walls = new int[height][width];

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					walls[i][j] = sc.nextInt();
				}
			}

			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("err: file \"" + fn + "\" does not exist");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	// get wall at x,y world coords
	public int getWallAt(Vector2d pos) {
		return getWall((int)pos.x, (int)pos.y);
	}

	// set wall at x,y world coords
	public void setWallAt(Vector2d pos, int val) {
		setWall((int)pos.x, (int)pos.y, val);
	}

	// get wall at x,y wall coords
	public int getWall(int x, int y) {
		if (x < 0 || x > walls[0].length) return 0;
		if (y < 0 || y > walls.length)    return 0;

		return walls[y][x];
	}

	// get wall at x,y wall coords
	public void setWall(int x, int y, int val) {
		if (x < 0 || x > walls[0].length) return;
		if (y < 0 || y > walls.length)    return;

		walls[y][x] = val;
	}
}
