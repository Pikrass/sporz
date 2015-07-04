package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class Murder extends Event
{
	public enum Origin {
		MUTANTS, DOCTORS
	}

	private Origin origin;
	private Player target;

	public Murder(Murder.Origin origin, Player target) {
		this.origin = origin;
		this.target = target;
	}

	public Origin getOrigin() {
		return origin;
	}

	public Player getTarget() {
		return target;
	}
}
