package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class Psychoanalysis extends Event {
	private Player origin;
	private Player target;
	private State result;
	private Hacked hacked;

	public Psychoanalysis(Player origin, Player target) {
		this.origin = origin;
		this.target = target;
		this.result = target.getState();
		this.hacked = new Hacked();
	}

	public Player getOrigin() {
		return origin;
	}

	public Player getTarget() {
		return target;
	}

	public boolean hasResult() {
		return !target.isNobody();
	}

	public State getResult() {
		return result;
	}

	public Hacked getHacked() {
		return hacked;
	}

	public class Hacked {
		private Hacked() {
		}
		public Player getTarget() {
			return Psychoanalysis.this.getTarget();
		}
		public boolean hasResult() {
			return Psychoanalysis.this.hasResult();
		}
		public State getResult() {
			return Psychoanalysis.this.getResult();
		}
	}
}
