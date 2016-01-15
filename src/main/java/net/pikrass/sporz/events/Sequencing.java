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

	public boolean hasResult() {
		return !target.isNobody();
	}

	public Genome getResult() {
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
			return Sequencing.this.getTarget();
		}
		public boolean hasResult() {
			return Sequencing.this.hasResult();
		}
		public Genome getResult() {
			return Sequencing.this.getResult();
		}
	}
}
