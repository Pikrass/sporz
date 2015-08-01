package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Sequencing;
import net.pikrass.sporz.events.SpyReport;

public class Sequence extends PlayerAction<Sequence.Do>
	implements Hackable, Spyable
{
	private String name;
	private Do choice;
	private Player genet;

	public Sequence(String name, Game game, Player genet) {
		super(game);
		this.name = name;
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

	public void stop() {
		genet.stopAsking(this);
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


	@Override
	public String getName() {
		return name;
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

	@Override
	public void spy(Player target, SpyReport report) {
		if(choice != null)
			choice.spy(target, report);
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
			game.getMaster().notify(event);
			genet.notify(event);
		}

		public void hack(Game game, Player hacker) {
			game.getMaster().notify(event.getHacked());
			hacker.notify(event.getHacked());
		}

		public void spy(Player target, SpyReport report) {
			if(this.target.equals(target))
				report.addLine(report.new Line(
							SpyReport.LineType.SEQUENCING,
							getName()
							));
		}
	}
}
