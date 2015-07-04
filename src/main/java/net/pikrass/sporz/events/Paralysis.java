package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class Paralysis extends Event
{
	private Player target;

	public Paralysis(Player target) {
		this.target = target;
	}

	public Player getTarget() {
		return target;
	}
}

