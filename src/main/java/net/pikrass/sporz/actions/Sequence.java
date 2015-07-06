package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Sequencing;

public class Sequence extends PlayerAction<Sequence.Do> {
	private Do choice;
	private Player genet;

	public Sequence(Game game, Player genet) {
		super(game);
		this.genet = genet;
	}

	public void startAction() {
		choice = null;

		if(!genet.isAlive() || genet.isParalysed()) {
			done(true);
			return;
		}

		genet.ask(game, this);
	}

	public void choose(Player player, Do choice) {
		this.choice = choice;
		done(choice != null);
	}

	public void executeAction() {
		if(choice == null)
			return;

		genet.stopAsking(this);

		choice.run(game);
	}


	public class Do extends RunnableChoice {
		private Player target;
		public Do(Player target) {
			this.target = target;
		}

		@Override
		public void run(Game game) {
			Sequencing event = new Sequencing(genet, target);
			genet.notify(event);
		}
	}
}
