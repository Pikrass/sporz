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

	public abstract void ask(Game game, ElectCaptain action);
	public abstract void ask(Game game, MutantsActions action);

	public abstract void stopAsking(ElectCaptain action);
	public abstract void stopAsking(MutantsActions action);



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

		public void ask(Game game, ElectCaptain action) { }
		public void ask(Game game, MutantsActions action) { }
		public void stopAsking(ElectCaptain action) { }
		public void stopAsking(MutantsActions action) { }
	}
}
