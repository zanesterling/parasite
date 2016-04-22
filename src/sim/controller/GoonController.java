package Parasite.sim.controller;

import Parasite.sim.Simulation;
import Parasite.sim.Personality;
import Parasite.sim.entity.Entity;
import Parasite.sim.entity.GoonEntity;
import Parasite.sim.entity.ParasiteEntity;
import Parasite.util.Vector2d;

import java.awt.Color;
import java.util.Stack;
import java.util.ArrayList;

public class GoonController extends Controller {

	private GoonEntity goon;
	private Stack<AIState> states;
	private Personality personality;

	private Vector2d lastSeen;

	// state var for non-curious folk
	private boolean seesAndNotChasing;

	public GoonController(Entity entity) {
		super(entity);
		states = new Stack<AIState>();
		states.push(new AIState(AIBehavior.IDLE));
		personality = new Personality();

		lastSeen = null;
	}

	public void update() {
		// don't control the goon if the player is
		if (!goon.isPossessable) return;
		ParasiteEntity parasite = Simulation.getInstance().parasite;
		boolean canSeePlayer = goon.canSee(parasite.getPosition());
		boolean seesParasite = canSeePlayer && !parasite.isPossessing;

		AIState state = states.peek();
		switch(state.behavior) {
			case IDLE:
				if (seesParasite) {
					// if we aren't chasing, don't bother
					if (seesAndNotChasing) {
						goon.face(parasite.getPosition());
						break;
					}

					if (Math.random() < personality.curiosity) {
						pushState(new AIState(AIBehavior.CHASE, parasite));
					} else {
						// not curious enough to chase
						seesAndNotChasing = true;
					}
				} else {
					// if we don't see, then we don't see
					seesAndNotChasing = false;
				}

				// debug rotation
				goon.setLookAngle(goon.getLookAngle() + 0.05);
				break;
			case CHASE:
				if (seesParasite) {
					// chase if you see the parasite
					goon.moveTowards(parasite.getPosition());
					lastSeen = parasite.getPosition();
				} else {
					// chase to last seen location if you lost him
					if (goon.distTo(lastSeen) > goon.maxSpeed)
						goon.moveTowards(lastSeen);
					else {
						goon.stop();
						popState();
					}
				}
				break;
		}

		if (canSeePlayer)
			goon.bodyColor = GoonEntity.CAN_SEE_COLOR;
		else
			goon.bodyColor = GoonEntity.DEFAULT_COLOR;
	}

	public void pushState(AIState state) {
		// if state was idle, replace it
		if (states.peek().behavior == AIBehavior.IDLE) states.pop();
		states.push(state); // otherwise just push
	}

	public void popState() {
		// if there's a state, pop it
		if (!states.empty()) states.pop();

		// if there's no state left, push the idle state
		if (states.empty()) states.push(new AIState(AIBehavior.IDLE));
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
