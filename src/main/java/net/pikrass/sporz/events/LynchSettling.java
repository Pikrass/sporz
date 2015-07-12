package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class LynchSettling extends Event {
	private Player origin;
	private Player target;

	public LynchSettling(Player origin, Player target) {
		this.origin = origin;
		this.target = target;
	}

	public Player getOrigin() {
		return origin;
	}

	public Player getTarget() {
		return target;
	}
}
