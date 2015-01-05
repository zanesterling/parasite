package Parasite.sim.controller;

import Parasite.sim.entity.Entity;

public class AIState {

	public AIBehavior behavior;
	public double[] realVars;
	public Entity target;

	public AIState(AIBehavior behavior) {
		this.behavior = behavior;
	}

	public AIState(AIBehavior behavior, double[] realVars) {
		this.behavior = behavior;
		this.realVars = realVars;
	}

	public AIState(AIBehavior behavior, Entity target) {
		this.behavior = behavior;
		this.target = target;
	}
}
