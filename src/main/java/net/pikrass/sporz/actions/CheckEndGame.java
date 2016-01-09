package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;
import net.pikrass.sporz.events.EndGame;

import java.util.Iterator;

public class CheckEndGame extends PreludeAction {
	private boolean mutantsAreNextToPlay;

	public CheckEndGame(Game game, boolean mutantsAreNextToPlay) {
		super(game);
		this.mutantsAreNextToPlay = mutantsAreNextToPlay;
	}

	@Override
	protected void doAction() {
		boolean hasHuman = false;
		boolean hasMutant = false;
		boolean hasResistant = false;
		boolean hasHumanDoctor = false;
		int nbMutants = 0;

		Iterator<Player> itPlayer = game.playerIterator();
		while(itPlayer.hasNext()) {
			Player player = itPlayer.next();

			if(!player.isAlive())
				continue;

			if(player.getState() == State.HUMAN) {
				hasHuman = true;
			} else {
				hasMutant = true;
				nbMutants++;
			}

			if(player.getGenome() == Genome.RESISTANT)
				hasResistant = true;

			if(player.getRole() == Role.DOCTOR && player.getState() == State.HUMAN)
				hasHumanDoctor = true;
		}

		if(!hasHuman && !hasMutant) {
			game.end(new EndGame(EndGame.Winner.DRAW, EndGame.Reason.ANNIHILATION));
			return;
		}

		if(!hasHuman) {
			game.end(new EndGame(EndGame.Winner.MUTANTS, EndGame.Reason.ANNIHILATION));
			return;
		}

		if(!hasMutant) {
			game.end(new EndGame(EndGame.Winner.HUMANS, EndGame.Reason.ANNIHILATION));
			return;
		}

		if(!hasHumanDoctor) {
			int threshold = 1 + (mutantsAreNextToPlay ? 0 : 1) + (hasResistant ? 1 : 0);
			if(nbMutants >= threshold)
				game.end(new EndGame(EndGame.Winner.MUTANTS, EndGame.Reason.ASSURED_VICTORY));
		}
	}
}
