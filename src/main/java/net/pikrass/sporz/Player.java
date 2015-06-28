package net.pikrass.sporz;

public abstract class Player
{
	public static final Player NOBODY = new Nobody();

	private String name;
	private State state;
	private Genome genome;
	private Role role;

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isNobody() {
		return false;
	}


	public abstract void notifyRound(int num, RoundPeriod period);


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
	}
}
