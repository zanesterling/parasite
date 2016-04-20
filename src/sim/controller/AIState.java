package Parasite.sim.controller;

import Parasite.sim.entity.Entity;

public class AIState {

	public AIBehavior behavior;
	public Entity target;

	public AIState(AIBehavior behavior) {
		this.behavior = behavior;
	}

	public AIState(AIBehavior behavior, Entity target) {
		this.behavior = behavior;
		this.target = target;
	}
}
