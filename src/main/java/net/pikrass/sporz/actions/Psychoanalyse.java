package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Psychoanalysis;

public class Psychoanalyse extends PlayerAction<Psychoanalyse.Do> {
	private Do choice;
	private Player psy;

	public Psychoanalyse(Game game, Player psy) {
		super(game);
		this.psy = psy;
	}

	public void startAction() {
		choice = null;

		if(!psy.isAlive() || psy.isParalysed()) {
			done(true);
			return;
		}

		psy.ask(game, this);
	}

	public void choose(Player player, Do choice) {
		this.choice = choice;
		done(choice != null);
	}

	public void executeAction() {
		if(choice == null)
			return;

		psy.stopAsking(this);

		choice.run(game);
	}


	public class Do extends RunnableChoice {
		private Player target;
		public Do(Player target) {
			this.target = target;
		}

		@Override
		public void run(Game game) {
			Psychoanalysis event = new Psychoanalysis(psy, target);
			psy.notify(event);
		}
	}
}
