package Parasite;

public class Game {

	static final long TICK_LEN = 1000 / 60; // 60 FPS

	Simulation sim;
	UI ui;

	boolean running;

	public Game() {
		sim = new Simulation();
		ui = new UI();
	}

	public void run() {
		// TODO Implement Game.run()
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
}
