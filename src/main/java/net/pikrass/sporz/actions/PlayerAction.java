package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;

public abstract class PlayerAction<C> extends Action {
	public PlayerAction(Game game) {
		super(game);
	}

	protected abstract void choose(Player player, C choice);
}
