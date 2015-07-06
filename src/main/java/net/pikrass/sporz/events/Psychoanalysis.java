package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class Psychoanalysis extends Event {
	private Player origin;
	private Player target;
	private State result;

	public Psychoanalysis(Player origin, Player target) {
		this.origin = origin;
		this.target = target;
		this.result = target.getState();
	}

	public Player getOrigin() {
		return origin;
	}

	public Player getTarget() {
		return target;
	}

	public State getResult() {
		return result;
	}
}
