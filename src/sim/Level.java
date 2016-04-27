package Parasite.sim;

import Parasite.util.Vector2d;
import Parasite.util.Line;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
		if (x < 0 || x >= walls[0].length) return 0;
		if (y < 0 || y >= walls.length)    return 0;

		return walls[y][x];
	}

	// get wall at x,y wall coords
	public void setWall(int x, int y, int val) {
		if (x < 0 || x >= walls[0].length) return;
		if (y < 0 || y >= walls.length)    return;

		walls[y][x] = val;
	}

	public ArrayList<Vector2d> getWallVerts() {
		ArrayList<Vector2d> verts = new ArrayList<Vector2d>();
		for (int i = 0; i < walls.length; i++) {
			for (int j = 0; j < walls[0].length; j++) {
				if (walls[i][j] != 1) continue;
				verts.add(new Vector2d(j - 0.5, i - 0.5));
				verts.add(new Vector2d(j + 0.5, i - 0.5));
				verts.add(new Vector2d(j + 0.5, i + 0.5));
				verts.add(new Vector2d(j - 0.5, i + 0.5));
			}
		}

		return verts;
	}

	public ArrayList<Line> getWallEdges() {
		ArrayList<Line> edges = new ArrayList<Line>();
		for (int i = 0; i < walls.length; i++) {
			for (int j = 0; j < walls[0].length; j++) {
				if (getWall(i, j) != 1) continue;
				if (getWall(i - 1, j) != 1) {
					edges.add(new Line(j, i, 1, 0));
				}
				if (getWall(i, j - 1) != 1) {
					edges.add(new Line(j, i, 0, 1));
				}
				if (getWall(i + 1, j) != 1) {
					edges.add(new Line(j, i + 1, 1, 0));
				}
				if (getWall(i, j + 1) != 1) {
					edges.add(new Line(j + 1, i, 0, 1));
				}
			}
		}

		return edges;
	}
}
