package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Paralysis;
import net.pikrass.sporz.events.Mutation;
import net.pikrass.sporz.events.Murder;
import net.pikrass.sporz.events.SpyReport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MutantsActions extends PlayerAction<MutantsActions.MutantChoice>
	implements Spyable
{
	private enum ChoiceType {
		MUTATE_OR_KILL, PARALYSE
	}

	private MutantChoice choice1, choice2;
	private List<Player> mutants;

	public MutantsActions(Game game) {
		super(game);
	}

	public void startAction() {
		choice1 = null;
		choice2 = null;

		mutants = new LinkedList<Player>();

		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; ) {
			Player p = it.next();
			if(p.getState() == State.MUTANT) {
				mutants.add(p);
				p.ask(game, this);
			}
		}

		if(mutants.size() == 0)
			done(true);
	}

	public void choose(Player player, MutantChoice choice) {
		if(choice == null) {
			choice1 = null;
			choice2 = null;
		} else {
			if(choice.getType() == ChoiceType.MUTATE_OR_KILL)
				choice1 = choice;
			else
				choice2 = choice;
		}

		done(choice1 != null && choice2 != null);
	}

	public void executeAction() {
		if(mutants.size() == 0)
			return;
		choice1.run(game);
		choice2.run(game);
	}


	@Override
	public void spy(Player target, SpyReport report) {
		if(choice1 != null)
			choice1.spy(target, report);
		if(choice2 != null)
			choice2.spy(target, report);
	}


	public abstract class MutantChoice extends RunnableChoice
			implements Spyable {
		public abstract ChoiceType getType();
	}

	public class Kill extends MutantChoice {
		private Player target;
		public Kill(Player target) {
			this.target = target;
		}

		@Override
		public ChoiceType getType() {
			return ChoiceType.MUTATE_OR_KILL;
		}

		@Override
		public void run(Game game) {
			Murder event = new Murder(Murder.Origin.MUTANTS, target);

			game.getMaster().notify(event);

			for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
				it.next().notify(event);

			game.kill(target);
		}

		@Override
		public void spy(Player target, SpyReport report) {
		}
	}

	public class Mutate extends MutantChoice {
		private Player target;
		public Mutate(Player target) {
			this.target = target;
		}

		@Override
		public ChoiceType getType() {
			return ChoiceType.MUTATE_OR_KILL;
		}

		@Override
		public void run(Game game) {
			Result res = target.mutate();

			Mutation event = new Mutation(target, res);

			game.getMaster().notify(event);
			target.notifyTarget(event);

			for(Player mutant : mutants)
				mutant.notifyOrigin(event.getNoResult());
		}

		@Override
		public void spy(Player target, SpyReport report) {
			if(this.target.equals(target))
				report.addLine(report.new Line(SpyReport.LineType.MUTATION));
		}
	}

	public class Paralyse extends MutantChoice {
		private Player target;
		public Paralyse(Player target) {
			this.target = target;
		}

		@Override
		public ChoiceType getType() {
			return ChoiceType.PARALYSE;
		}

		@Override
		public void run(Game game) {
			target.paralyse();

			Paralysis event = new Paralysis(target);

			game.getMaster().notify(event);
			target.notifyTarget(event);

			for(Player mutant : mutants)
				mutant.notifyOrigin(event);
		}

		@Override
		public void spy(Player target, SpyReport report) {
			if(this.target.equals(target))
				report.addLine(report.new Line(SpyReport.LineType.PARALYSIS));
		}
	}
}
