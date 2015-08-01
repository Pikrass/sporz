package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.SpyReport;

import java.util.LinkedList;
import java.util.List;

public class Spy extends PlayerAction<Spy.Do>
{
	private List<Spyable> actions;
	private Do choice;
	private Player spy;

	public Spy(Game game, Player spy) {
		super(game);
		this.spy = spy;
		this.actions = new LinkedList<Spyable>();
	}

	public void spyAction(Spyable action) {
		actions.add(action);
	}

	public void startAction() {
		choice = null;

		if(!spy.isAlive() || spy.isParalysed()) {
			done(true);
			return;
		}

		spy.ask(game, this);
	}

	public void stop() {
		spy.stopAsking(this);
	}

	public void choose(Player player, Do choice) {
		this.choice = choice;
		done(choice != null);
	}

	public void executeAction() {
		if(choice == null)
			return;

		stop();

		choice.run(game);
	}


	public class Do extends RunnableChoice {
		private Player target;

		public Do(Player target) {
			this.target = target;
		}

		@Override
		public void run(Game game) {
			if(target.isNobody())
				return;

			SpyReport event = new SpyReport(spy, target);
			for(Spyable action : actions)
				action.spy(target, event);

			game.getMaster().notify(event);
			spy.notify(event);
		}
	}
}


