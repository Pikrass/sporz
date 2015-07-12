package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.Murder;
import net.pikrass.sporz.events.Healing;
import net.pikrass.sporz.events.SpyReport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DoctorsAction extends PlayerAction<DoctorsAction.DoctorChoice>
	implements Spyable
{
	private List<DoctorChoice> choices;
	private List<Player> doctors;

	public DoctorsAction(Game game) {
		super(game);
	}

	public void startAction() {
		choices = new LinkedList<DoctorChoice>();
		doctors = new LinkedList<Player>();

		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; ) {
			Player p = it.next();
			if(p.getRole() == Role.DOCTOR
					&& !p.isParalysed()
					&& p.getState() == State.HUMAN) {
				doctors.add(p);
				p.ask(game, this);
			}
		}

		if(doctors.size() == 0)
			done(true);
	}

	public void choose(Player player, DoctorChoice choice) {
		if(choice == null) {
			choices.clear();
			done(false);
		} else {
			if(choices.size() + 1 > choice.getAvailable())
				choices.clear();
			choices.add(choice);
			done(choices.size() == choice.getAvailable());
		}
	}

	public void executeAction() {
		if(doctors.size() == 0)
			return;

		for(DoctorChoice choice : choices)
			choice.run(game);
	}


	@Override
	public void spy(Player target, SpyReport report) {
		for(DoctorChoice choice : choices)
			choice.spy(target, report);
	}


	public abstract class DoctorChoice extends RunnableChoice
			implements Spyable {
		public abstract int getAvailable();
	}

	public class Kill extends DoctorChoice {
		private Player target;
		public Kill(Player target) {
			this.target = target;
		}

		@Override
		public int getAvailable() {
			return 1;
		}

		@Override
		public void run(Game game) {
			Murder event = new Murder(Murder.Origin.DOCTORS, target);

			game.getMaster().notify(event);

			for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; )
				it.next().notify(event);

			game.kill(target);
		}

		@Override
		public void spy(Player target, SpyReport report) {
		}
	}

	public class Heal extends DoctorChoice {
		private Player target;
		public Heal(Player target) {
			this.target = target;
		}

		@Override
		public int getAvailable() {
			return doctors.size();
		}

		@Override
		public void run(Game game) {
			Result res = target.heal();

			Healing event = new Healing(target, res);

			game.getMaster().notify(event);
			target.notifyTarget(event);

			for(Player doc : doctors)
				doc.notifyOrigin(event.getNoResult());
		}

		@Override
		public void spy(Player target, SpyReport report) {
			if(this.target.equals(target))
				report.addLine(report.new Line(SpyReport.LineType.HEALING));
		}
	}
}
