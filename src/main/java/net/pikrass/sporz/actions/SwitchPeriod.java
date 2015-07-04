package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;

public class SwitchPeriod extends PreludeAction {
	public SwitchPeriod(Game game) {
		super(game);
	}

	@Override
	protected void doAction() {
		game.nextPeriod();
	}
}
