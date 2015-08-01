package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;

public abstract class Action {
	protected Game game;
	private Phase.Tracker tracker;

	public Action(Game game) {
		this.game = game;
		this.tracker = null;
	}

	public void start(Phase.Tracker tracker) {
		this.tracker = tracker;
		startAction();
	}

	protected void done(boolean ok) {
		this.tracker.done(ok);
	}

	public void execute() {
		executeAction();
		this.tracker = null;
	}

	protected abstract void startAction();
	protected abstract void executeAction();
	public abstract void stop();
}
