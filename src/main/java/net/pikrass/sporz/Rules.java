package net.pikrass.sporz;

public abstract class Rules {
	public abstract int minPlayers();
	public abstract int maxPlayers();

	public void apply(Game game) throws RulesException {
		if(game.getNbPlayers() < minPlayers() || game.getNbPlayers() > maxPlayers())
			throw new RulesException();

		doApply(game);
	}

	protected abstract void doApply(Game game);
}
