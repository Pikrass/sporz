package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class MutantCount extends Event {
	private Player origin;
	private boolean hasResult;
	private int result;

	public MutantCount(Player origin, int count) {
		this.origin = origin;
		this.result = count;
		this.hasResult = true;
	}

	public MutantCount(Player origin) {
		this.origin = origin;
		this.hasResult = false;
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
		public boolean hasResult() {
			return MutantCount.this.hasResult();
		}
		public int getResult() {
			return MutantCount.this.getResult();
		}
	}
}
