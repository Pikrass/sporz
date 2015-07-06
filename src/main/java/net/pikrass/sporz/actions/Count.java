package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.MutantCount;

import java.util.Iterator;

public class Count extends PlayerAction<Count.Do> {
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


	public class Do extends RunnableChoice {
		private boolean count;
		public Do(boolean count) {
			this.count = count;
		}

		@Override
		public void run(Game game) {
			if(!count)
				return;

			int num = 0;
			for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; ) {
				if(it.next().getState() == State.MUTANT)
					num++;
			}

			MutantCount event = new MutantCount(engineer, num);
			engineer.notify(event);
		}
	}
}
