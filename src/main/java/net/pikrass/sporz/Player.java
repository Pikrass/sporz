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

	public Player(String name) {
		this.name = name;
		this.state = State.HUMAN;
		this.genome = Genome.STANDARD;
		this.role = Role.ASTRONAUT;
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



	public void setGenome(Genome genome) {
		this.genome = genome;
	}



	public boolean isNobody() {
		return false;
	}

	public abstract void notifyRound(int num, RoundPeriod period);
	public abstract void notify(Attribution event);
	public abstract void notify(NewCaptain event);

	public abstract void ask(Game game, ElectCaptain action);
	public abstract void stopAsking(ElectCaptain action);


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

		public void notifyRound(int num, RoundPeriod period) { }
		public void notify(Attribution event) { }
		public void notify(NewCaptain event) { }

		public void ask(Game game, ElectCaptain action) { }
		public void stopAsking(ElectCaptain action) { }
	}
}
