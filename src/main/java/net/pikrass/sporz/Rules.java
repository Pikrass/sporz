package net.pikrass.sporz;

import java.util.Random;

public abstract class Rules {
	protected Random rng;

	public Rules() {
		this.rng = new Random();
	}

	public void seedRNG(long seed) {
		this.rng = new Random(seed);
	}

	public abstract int minPlayers();
	public abstract int maxPlayers();

	public void apply(Game game) throws RulesException {
		if(game.getNbPlayers() < minPlayers() || game.getNbPlayers() > maxPlayers())
			throw new RulesException();

		doApply(game);
	}

	protected abstract void doApply(Game game);
}
