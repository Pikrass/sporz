package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.NewCaptain;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class ElectCaptain extends PlayerAction<ElectCaptain.Vote>
{
	private int nbPlayers;
	private Map<Player, Player> votes;
	private Player winner;

	public ElectCaptain(Game game) {
		super(game);
	}

	protected void startAction() {
		if(game.getCaptain() != null) {
			done(true);
			return;
		}

		nbPlayers = game.getNbPlayers();
		votes = new HashMap<Player, Player>();

		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
			it.next().ask(game, this);
	}

	public void stop() {
		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
			it.next().stopAsking(this);
	}

	public void choose(Player player, Vote choice) {
		if(choice != null)
			votes.put(player, choice.getChoice());
		else
			votes.remove(player);

		if(votes.size() == nbPlayers) {
			winner = findWinner();
			done(winner != null);
		} else {
			done(false);
		}
	}

	protected void executeAction() {
		if(game.getCaptain() != null)
			return;

		stop();

		game.setCaptain(winner);

		NewCaptain event = new NewCaptain(votes, winner);

		game.getMaster().notify(event);
		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
			it.next().notify(event);
	}

	private Player findWinner() {
		Map<Player, Integer> counts = new HashMap<Player, Integer>();
		int max = 0;
		Player best = null;

		for(Player votedFor : votes.values()) {
			if(votedFor == null)
				continue;

			Integer count = counts.get(votedFor);
			if(count == null)
				count = new Integer(0);

			if(count + 1 > max) {
				max = count + 1;
				best = votedFor;
			} else if(count + 1 == max) {
				// Draw
				best = null;
			}

			counts.put(votedFor, count + 1);
		}

		return best;
	}

	public class Vote {
		private Player player;

		public Vote(Player choice) throws BlankVoteProhibitedException {
			if(choice != null && choice.isNobody())
				throw new BlankVoteProhibitedException();
			this.player = choice;
		}

		public Player getChoice() {
			return player;
		}
	}
}
