package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.MutantCount;

import java.util.Iterator;

public class Count extends PlayerAction<Count.Do> implements Hackable {
	private Do choice;
	private Player engineer;

	public Count(Game game, Player engineer) {
		super(game);
		this.engineer = engineer;
	}

	public void startAction() {
		choice = null;

		if(!engineer.isAlive() || engineer.isParalysed()) {
			done(true);
			return;
		}

		engineer.ask(game, this);
	}

	public void choose(Player player, Do choice) {
		this.choice = choice;
		done(choice != null);
	}

	public void executeAction() {
		if(choice == null)
			return;

		engineer.stopAsking(this);

		choice.run(game);
	}


	@Override
	public boolean isStillValid() {
		return engineer.isAlive();
	}

	@Override
	public void hack(Game game, Player hacker) {
		Do choice = this.choice;

		// Pretend we purposedly did nothing
		if(choice == null)
			choice = new Do(false);

		choice.hack(game, hacker);
	}


	public class Do extends RunnableChoice {
		private MutantCount event;

		public Do(boolean count) {
			if(count) {
				int num = 0;
				for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; ) {
					if(it.next().getState() == State.MUTANT)
						num++;
				}

				this.event = new MutantCount(engineer, num);
			} else {
				this.event = new MutantCount(engineer);
			}
		}

		@Override
		public void run(Game game) {
			engineer.notify(event);
		}

		public void hack(Game game, Player hacker) {
			hacker.notify(event.getHacked());
		}
	}
}
