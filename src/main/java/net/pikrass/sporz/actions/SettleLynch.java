package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.LynchSettling;

import java.util.Set;
import java.util.Iterator;

public class SettleLynch extends PlayerAction<SettleLynch.Do>
{
	private Do choice;
	private Lynch lynch;

	public SettleLynch(Game game, Lynch lynch) {
		super(game);
		this.lynch = lynch;
	}

	public Set<Player> getChoices() {
		return lynch.getWinners();
	}

	public void startAction() {
		if(!lynch.isDraw()) {
			done(true);
			return;
		}

		game.getCaptain().ask(game, this);
	}

	public void stop() {
		game.getCaptain().stopAsking(this);
	}

	public void choose(Player player, Do choice) {
		this.choice = choice;
		done(choice != null);
	}

	public void executeAction() {
		if(!lynch.isDraw())
			return;

		stop();

		choice.run(game);
	}


	public class Do extends RunnableChoice {
		private Player target;

		public Do(Player target) throws InvalidChoiceException {
			if(!getChoices().contains(target))
				throw new InvalidChoiceException();
			this.target = target;
		}

		@Override
		public void run(Game game) {
			LynchSettling event = new LynchSettling(game.getCaptain(), target);

			game.getMaster().notify(event);

			for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
				it.next().notify(event);

			game.kill(target);
		}
	}
}

