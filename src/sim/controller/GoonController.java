package Parasite.sim.controller;

import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;

import java.awt.Color;
import java.util.Stack;
import java.util.ArrayList;

public class GoonController extends Controller {

	GoonEntity goon;
	Stack<AIState> states;

	public GoonController(Entity entity) {
		super(entity);
		states = new Stack<AIState>();
		states.push(AIState.IDLE);
	}

	public void update() {
		// don't control the goon if the player is
		if (!goon.isPossessable) return;

		switch(states.peek()) {
			case IDLE:
				goon.setLookAngle(goon.getLookAngle() + 0.02);
				break;
		}

		if (goon.canSee(Simulation.getInstance().parasite))
			goon.bodyColor = GoonEntity.CAN_SEE_COLOR;
		else
			goon.bodyColor = GoonEntity.DEFAULT_COLOR;
	}

	public void pushState(AIState state) {
		// if state was idle, replace it
		if (states.peek() == AIState.IDLE) states.pop();
		states.push(state); // otherwise just push
	}

	public void popState() {
		// if there's a state, pop it
		if (!states.empty()) states.pop();

		// if there's no state left, push the idle state
		if (states.empty()) states.push(AIState.IDLE);
	}

	public void addEntity(Entity entity) {
		if (!(entity instanceof GoonEntity)) {
			System.out.println("tried to add non-goon to GoonController");
			return;
		}

		if (controlled.size() > 0)
			controlled.clear();

		goon = (GoonEntity) entity;
		controlled.add(goon);
	}
}
