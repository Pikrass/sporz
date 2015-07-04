package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

public class Mutation extends Event
{
	private Player target;
	private Result result;
	private NoResult noResult;

	public Mutation(Player target, Result result) {
		this.target = target;
		this.result = result;
		this.noResult = new NoResult();
	}

	public Player getTarget() {
		return target;
	}

	public Result getResult() {
		return result;
	}

	public NoResult getNoResult() {
		return noResult;
	}

	public class NoResult {
		private NoResult() {
		}
		public Player getTarget() {
			return target;
		}
	}
}
