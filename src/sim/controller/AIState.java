package Parasite.sim.controller;

import Parasite.sim.Location;
import Parasite.sim.entity.Entity;

public class AIState {

	public AIBehavior behavior;
	public Location[] locations;
	public Entity target;

	public AIState(AIBehavior behavior) {
		this.behavior = behavior;
	}

	public AIState(AIBehavior behavior, Location[] locations) {
		this.behavior = behavior;
		this.locations = locations;
	}

	public AIState(AIBehavior behavior, Entity target) {
		this.behavior = behavior;
		this.target = target;
	}
}
