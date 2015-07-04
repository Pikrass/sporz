package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;

import java.util.Iterator;

public class ResetParalysis extends PreludeAction {
	public ResetParalysis(Game game) {
		super(game);
	}

	@Override
	protected void doAction() {
		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
			it.next().resetParalysis();
	}
}
