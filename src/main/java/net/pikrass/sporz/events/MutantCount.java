package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class MutantCount extends Event {
	private Player origin;
	private boolean hasResult;
	private int result;
	private Hacked hacked;

	public MutantCount(Player origin, int count) {
		this.origin = origin;
		this.result = count;
		this.hasResult = true;
		this.hacked = new Hacked();
	}

	public MutantCount(Player origin) {
		this.origin = origin;
		this.hasResult = false;
		this.hacked = new Hacked();
	}

	public Player getOrigin() {
		return origin;
	}

	public boolean hasResult() {
		return hasResult;
	}

	public int getResult() {
		return result;
	}

	public Hacked getHacked() {
		return hacked;
	}

	public class Hacked {
		private Hacked() {
		}
		public boolean hasResult() {
			return MutantCount.this.hasResult();
		}
		public int getResult() {
			return MutantCount.this.getResult();
		}
	}
}
