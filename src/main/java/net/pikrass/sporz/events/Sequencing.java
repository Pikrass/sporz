package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class Sequencing extends Event {
	private Player origin;
	private Player target;
	private Genome result;

	public Sequencing(Player origin, Player target) {
		this.origin = origin;
		this.target = target;
		this.result = target.getGenome();
	}

	public Player getOrigin() {
		return origin;
	}

	public Player getTarget() {
		return target;
	}

	public Genome getResult() {
		return result;
	}
}
