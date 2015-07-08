package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;

public interface Hackable {
	public void hack(Game game, Player hacker);
	public boolean isStillValid();
}
