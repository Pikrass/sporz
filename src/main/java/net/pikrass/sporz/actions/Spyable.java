package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.SpyReport;

public interface Spyable {
	public void spy(Player target, SpyReport report);
}
