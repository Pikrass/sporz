package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class MutantCount extends Event {
	private Player origin;
	private int result;

	public MutantCount(Player origin, int count) {
		this.origin = origin;
		this.result = count;
	}

	public Player getOrigin() {
		return origin;
	}

	public int getResult() {
		return result;
	}
}
