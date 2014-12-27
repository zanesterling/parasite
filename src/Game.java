package Parasite;

import Parasite.ui.UI;
import Parasite.ui.UIEvent;
import Parasite.sim.Simulation;

public class Game {

	public static boolean DEBUG_STATE = false;

	private static final long TICK_LEN = 1000 / 60; // 60 FPS

	private Simulation sim;

	private boolean running;

	public Game() {
		sim = Simulation.getSimulation();

		UI.getInstance().setSimulation(sim);
	}

	public void run() {
		running = true;

		UI ui = UI.getInstance();
		while (running) {
			long startTime = System.currentTimeMillis();

			// check UI for ui events
			for (UIEvent e; (e = ui.getEvent()) != null;) {
				sim.processEvent(e);
			}

			// update simulation
			sim.update();

			// tell ui to render simulation
			ui.render();

			long endTime = System.currentTimeMillis();

			if (endTime - startTime > TICK_LEN) continue;

			// if the tick took less time than TICK_LEN to run, sleep
			try {
				Thread.sleep(TICK_LEN - (endTime - startTime));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
}
