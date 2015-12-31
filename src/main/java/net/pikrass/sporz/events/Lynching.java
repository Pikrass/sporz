package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Lynching extends Event
{
	private Map<Player, Player> votes;
	private Map<Player, Integer> counts;
	private Set<Player> targets;
	private Anonymous anonymous;

	public Lynching(Map<Player, Player> votes, Set<Player> targets) {
		this.votes = votes;
		this.targets = targets;
		this.anonymous = new Anonymous();

		this.counts = new HashMap<Player, Integer>();

		for(Player p : votes.values()) {
			Integer prev = counts.get(p);
			if(prev == null)
				prev = 0;

			counts.put(p, prev + 1);
		}
	}

	public Iterator<Map.Entry<Player, Player>> voteIterator() {
		return votes.entrySet().iterator();
	}

	public Iterator<Map.Entry<Player, Integer>> countIterator() {
		return counts.entrySet().iterator();
	}

	public Player getTarget() {
		return targets.iterator().next();
	}

	public Set<Player> getTargets() {
		return targets;
	}

	public boolean isDraw() {
		return targets.size() > 1;
	}

	public Anonymous getAnonymous() {
		return anonymous;
	}

	public class Anonymous {
		private Anonymous() {
		}
		public Player getTarget() {
			return Lynching.this.getTarget();
		}
		public Set<Player> getTargets() {
			return Lynching.this.getTargets();
		}
		public Iterator<Map.Entry<Player, Integer>> countIterator() {
			return Lynching.this.countIterator();
		}
		public boolean isDraw() {
			return Lynching.this.isDraw();
		}
	}
}
