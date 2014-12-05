package Parasite.sim.controller;

import Parasite.sim.entity.Entity;

import java.util.Stack;

public class AIController extends Controller {

	enum AIState {
		IDLE,
		LOOK,
		GOTO,
		CHASE,
		ATTACK,
		PATROL,
		PROTECT
	};

	Stack<AIState> states;

	public AIController(Entity entity) {
		super(entity);
		states = new Stack<AIState>();
	}

	public void update() {}
}
