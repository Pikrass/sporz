package net.pikrass.sporz;

import net.pikrass.sporz.events.*;

public class UselessMaster implements Master
{
	public void notifyRound(int num, RoundPeriod period) { }
	public void notify(Attribution event) { }
	public void notify(NewCaptain event) { }
	public void notify(Paralysis event) { }
	public void notify(Mutation event) { }
	public void notify(Murder event) { }
	public void notify(Healing event) { }
	public void notify(Psychoanalysis event) { }
	public void notify(Sequencing event) { }
	public void notify(MutantCount event) { }
	public void notify(Psychoanalysis.Hacked event) { }
	public void notify(Sequencing.Hacked event) { }
	public void notify(MutantCount.Hacked event) { }
	public void notify(SpyReport event) { }
	public void notify(Lynching event) { }
	public void notify(LynchSettling event) { }
}
