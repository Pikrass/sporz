package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class NewCaptain extends Event
{
	private Map<Player, Player> votes;
	private Player winner;

	public NewCaptain(Map<Player, Player> votes, Player winner) {
		this.winner = winner;
		this.votes = new HashMap<Player, Player>(votes);
	}

	public Iterator<Map.Entry<Player, Player>> voteIterator() {
		return votes.entrySet().iterator();
	}

	public Player getWinner() {
		return winner;
	}
}
