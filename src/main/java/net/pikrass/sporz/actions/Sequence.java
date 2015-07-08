package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Sequencing;

public class Sequence extends PlayerAction<Sequence.Do> implements Hackable
{
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


	@Override
	public boolean isStillValid() {
		return genet.isAlive();
	}

	@Override
	public void hack(Game game, Player hacker) {
		Do choice = this.choice;

		// Pretend we purposedly did nothing
		if(choice == null)
			choice = new Do(Player.NOBODY);

		choice.hack(game, hacker);
	}


	public class Do extends RunnableChoice {
		private Player target;
		private Sequencing event;

		public Do(Player target) {
			this.target = target;
			this.event = new Sequencing(genet, target);
		}

		@Override
		public void run(Game game) {
			genet.notify(event);
		}

		public void hack(Game game, Player hacker) {
			hacker.notify(event.getHacked());
		}
	}
}
