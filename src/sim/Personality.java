package Parasite.sim;

public class Personality {

	// personality measurements, restricted to [0, 1]
	public double chattiness;
	public double curiosity;
	public double antsiness;
	public double excitability;

	public Personality() {
		chattiness = Math.random();
		curiosity = Math.random();
		antsiness = Math.random();
		excitability = Math.random();
	}
}
