package net.pikrass.sporz;

import net.pikrass.sporz.actions.*;
import net.pikrass.sporz.events.*;

import java.util.List;

public abstract class Player
{
	public static final Player NOBODY = new Nobody();

	private String name;
	private State state;
	private Genome genome;
	private Role role;
	private boolean paralysed;
	private boolean alive;

	public Player(String name) {
		this.name = name;
		this.state = State.HUMAN;
		this.genome = Genome.STANDARD;
		this.role = Role.ASTRONAUT;
		this.paralysed = false;
		this.alive = true;
	}

	public String getName() {
		return name;
	}

	public Attribution makeAttribution() {
		return new Attribution(this, role, state);
	}

	public Attribution makeAttribution(List<Player> group) {
		return new Attribution(this, role, state, group);
	}



	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}



	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Result mutate() {
		if(state == State.MUTANT)
			return Result.USELESS;

		if(genome == Genome.RESISTANT)
			return Result.FAIL;

		this.state = State.MUTANT;
		return Result.SUCCESS;
	}

	public Result heal() {
		if(state == State.HUMAN)
			return Result.USELESS;

		if(genome == Genome.HOST)
			return Result.FAIL;

		this.state = State.HUMAN;
		return Result.SUCCESS;
	}



	public Genome getGenome() {
		return genome;
	}

	public void setGenome(Genome genome) {
		this.genome = genome;
	}



	public void paralyse() {
		this.paralysed = true;
	}

	public void resetParalysis() {
		this.paralysed = false;
	}

	public boolean isParalysed() {
		return paralysed;
	}



	public boolean isAlive() {
		return alive;
	}

	public void kill() {
		this.alive = false;
	}



	public boolean isNobody() {
		return false;
	}



	public abstract void notifyRound(int num, RoundPeriod period);
	public abstract void notify(Attribution event);
	public abstract void notify(NewCaptain event);
	public abstract void notifyOrigin(Paralysis event);
	public abstract void notifyTarget(Paralysis event);
	public abstract void notifyOrigin(Mutation.NoResult event);
	public abstract void notifyTarget(Mutation event);
	public abstract void notify(Murder event);
	public abstract void notifyOrigin(Healing.NoResult event);
	public abstract void notifyTarget(Healing event);
	public abstract void notify(Psychoanalysis event);
	public abstract void notify(Sequencing event);
	public abstract void notify(MutantCount event);
	public abstract void notify(Psychoanalysis.Hacked event);
	public abstract void notify(Sequencing.Hacked event);
	public abstract void notify(MutantCount.Hacked event);
	public abstract void notify(SpyReport event);

	public abstract void ask(Game game, ElectCaptain action);
	public abstract void ask(Game game, MutantsActions action);
	public abstract void ask(Game game, DoctorsAction action);
	public abstract void ask(Game game, Psychoanalyse action);
	public abstract void ask(Game game, Sequence action);
	public abstract void ask(Game game, Count action);
	public abstract void ask(Game game, Hack action);
	public abstract void ask(Game game, Spy action);

	public abstract void stopAsking(ElectCaptain action);
	public abstract void stopAsking(MutantsActions action);
	public abstract void stopAsking(DoctorsAction action);
	public abstract void stopAsking(Psychoanalyse action);
	public abstract void stopAsking(Sequence action);
	public abstract void stopAsking(Count action);
	public abstract void stopAsking(Hack action);
	public abstract void stopAsking(Spy action);



	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Player))
			return false;

		return getName().equals(((Player)o).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}


	private static class Nobody extends Player {
		public Nobody() {
			super("");
		}
		public boolean isNobody() {
			return true;
		}

		public void paralyse() { }
		public Result mutate() { return Result.USELESS; }

		public void notifyRound(int num, RoundPeriod period) { }
		public void notify(Attribution event) { }
		public void notify(NewCaptain event) { }
		public void notifyOrigin(Paralysis event) { }
		public void notifyTarget(Paralysis event) { }
		public void notifyOrigin(Mutation.NoResult event) { }
		public void notifyTarget(Mutation event) { }
		public void notify(Murder event) { }
		public void notifyOrigin(Healing.NoResult event) { }
		public void notifyTarget(Healing event) { }
		public void notify(Psychoanalysis event) { }
		public void notify(Sequencing event) { }
		public void notify(MutantCount event) { }
		public void notify(Psychoanalysis.Hacked event) { }
		public void notify(Sequencing.Hacked event) { }
		public void notify(MutantCount.Hacked event) { }
		public void notify(SpyReport event) { }

		public void ask(Game game, ElectCaptain action) { }
		public void ask(Game game, MutantsActions action) { }
		public void ask(Game game, DoctorsAction action) { }
		public void ask(Game game, Psychoanalyse action) { }
		public void ask(Game game, Sequence action) { }
		public void ask(Game game, Count action) { }
		public void ask(Game game, Hack action) { }
		public void ask(Game game, Spy action) { }

		public void stopAsking(ElectCaptain action) { }
		public void stopAsking(MutantsActions action) { }
		public void stopAsking(DoctorsAction action) { }
		public void stopAsking(Psychoanalyse action) { }
		public void stopAsking(Sequence action) { }
		public void stopAsking(Count action) { }
		public void stopAsking(Hack action) { }
		public void stopAsking(Spy action) { }
	}
}
