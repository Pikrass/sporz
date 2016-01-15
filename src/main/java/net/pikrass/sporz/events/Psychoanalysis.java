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

	public boolean hasResult() {
		return !target.isNobody();
	}

	public State getResult() {
		return result;
	}

	public Hacked getHacked(Player hacker) {
		return new Hacked(hacker);
	}

	public class Hacked {
		private Player hacker;

		private Hacked(Player hacker) {
			this.hacker = hacker;
		}
		public Player getHacker() {
			return hacker;
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
