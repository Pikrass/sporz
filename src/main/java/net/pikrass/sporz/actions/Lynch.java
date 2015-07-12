package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Lynching;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class Lynch extends PlayerAction<Lynch.Vote>
{
	private int nbPlayers;
	private Map<Player, Player> votes;
	private HashSet<Player> winners;

	public Lynch(Game game) {
		super(game);
	}

	public boolean isDraw() {
		return winners.size() > 1;
	}

	public Set<Player> getWinners() {
		return winners;
	}

	protected void startAction() {
		nbPlayers = game.getNbPlayers();
		votes = new HashMap<Player, Player>();

		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
			it.next().ask(game, this);
	}

	public void choose(Player player, Vote choice) {
		if(choice != null)
			votes.put(player, choice.getChoice());
		else
			votes.remove(player);

		if(votes.size() == nbPlayers) {
			findWinners();
			done(true);
		} else {
			done(false);
		}
	}

	protected void executeAction() {
		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
			it.next().stopAsking(this);

		Lynching event = new Lynching(votes, winners);

		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
			it.next().notify(event.getAnonymous());

		if(!isDraw())
			game.kill(winners.iterator().next());
	}

	private void findWinners() {
		Map<Player, Integer> counts = new HashMap<Player, Integer>();
		this.winners = new HashSet<Player>();

		int max = 0;

		for(Player votedFor : votes.values()) {
			if(votedFor == null)
				continue;

			Integer count = counts.get(votedFor);
			if(count == null)
				count = new Integer(0);

			if(count + 1 > max) {
				max = count + 1;
				winners.clear();
				winners.add(votedFor);
			} else if(count + 1 == max) {
				winners.add(votedFor);
			}

			counts.put(votedFor, count + 1);
		}
	}

	public class Vote {
		private Player player;

		public Vote(Player choice) {
			this.player = choice;
		}

		public Player getChoice() {
			return player;
		}
	}
}

