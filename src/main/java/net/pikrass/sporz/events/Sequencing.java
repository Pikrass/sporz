package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class Sequencing extends Event {
	private Player origin;
	private Player target;
	private Genome result;
	private Hacked hacked;

	public Sequencing(Player origin, Player target) {
		this.origin = origin;
		this.target = target;
		this.result = target.getGenome();
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

	public Genome getResult() {
		return result;
	}

	public Hacked getHacked() {
		return hacked;
	}

	public class Hacked {
		private Hacked() {
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
