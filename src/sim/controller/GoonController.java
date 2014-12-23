package Parasite.sim.controller;

import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;

import java.awt.Color;
import java.util.Stack;
import java.util.ArrayList;

public class GoonController extends Controller {

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

	public void update() {
		GoonEntity entity = (GoonEntity) controlled.get(0);
		entity.setLookAngle(entity.getLookAngle() + 0.02);

		if (entity.canSee(Simulation.getSimulation().parasite)) {
			entity.bodyColor = new Color(80, 200, 255);
		} else {
			entity.bodyColor = new Color(0, 100, 255);
		}
	}

	public void addEntity(Entity entity) {
		if (!(entity instanceof GoonEntity))
			System.out.println("tried to add non-goon to AIController");
		if (controlled.size() > 0)
			controlled.clear();

		controlled.add(entity);
	}
}
