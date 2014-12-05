package Parasite.sim.controller;

import Parasite.sim.Simulation;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;

import java.awt.Color;
import java.util.Stack;
import java.util.ArrayList;

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

	public void update() {
		GoonEntity entity = (GoonEntity) controlledEntities.get(0);
		entity.setHeading(entity.heading + 0.02);

		if (entity.canSee(Simulation.getSimulation().parasite)) {
			entity.bodyColor = Color.YELLOW;
		} else {
			entity.bodyColor = new Color(0, 100, 255);
		}
	}

	public void addEntity(Entity entity) {
		if (entity instanceof GoonEntity) {
			controlledEntities.add(entity);
		} else {
			System.out.println("tried to add non-goon to AIController");
		}
	}
}
