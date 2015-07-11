package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Psychoanalysis;
import net.pikrass.sporz.events.SpyReport;

public class Psychoanalyse extends PlayerAction<Psychoanalyse.Do>
	implements Hackable, Spyable
{
	private String name;
	private Do choice;
	private Player psy;

	public Psychoanalyse(String name, Game game, Player psy) {
		super(game);
		this.name = name;
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


	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isStillValid() {
		return psy.isAlive();
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
		private Psychoanalysis event;

		public Do(Player target) {
			this.target = target;
			this.event = new Psychoanalysis(psy, target);
		}

		@Override
		public void run(Game game) {
			psy.notify(event);
		}

		public void hack(Game game, Player hacker) {
			hacker.notify(event.getHacked());
		}

		public void spy(Player target, SpyReport report) {
			if(this.target.equals(target))
				report.addLine(report.new Line(
							SpyReport.LineType.PSYCHOANALYSIS,
							getName()
							));
		}
	}
}
