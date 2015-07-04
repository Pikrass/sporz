package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;

public abstract class PreludeAction extends Action {
	public PreludeAction(Game game) {
		super(game);
	}

	@Override
	public void startAction() {
		doAction();
		done(true);
	}

	@Override
	protected void executeAction() {
	}

	protected abstract void doAction();
}
