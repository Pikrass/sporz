package net.pikrass.sporz;

import net.pikrass.sporz.actions.Psychoanalyse;
import net.pikrass.sporz.actions.Sequence;
import net.pikrass.sporz.actions.Count;
import net.pikrass.sporz.actions.Hack;
import net.pikrass.sporz.actions.Spy;
import net.pikrass.sporz.events.Attribution;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

public class StandardRules extends Rules {
	public int minPlayers() {
		return 8;
	}
	public int maxPlayers() {
		return Integer.MAX_VALUE;
	}

	protected void doApply(Game game) {
		ArrayList<Player> players = new ArrayList<Player>();

		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; ) {
			players.add(it.next());
		}

		int rem = game.getNbPlayers();

		// Initial mutant
		Player mutant = players.remove(rng.nextInt(rem--));
		mutant.setState(State.MUTANT);
		mutant.setGenome(Genome.HOST);
		Attribution mutA = mutant.makeAttribution();
		game.getMaster().notify(mutA);
		mutant.notify(mutA);

		// Doctors
		List<Player> docs = new LinkedList<Player>();
		docs.add(players.remove(rng.nextInt(rem--)));
		docs.add(players.remove(rng.nextInt(rem--)));
		for(Player doc : docs) {
			doc.setRole(Role.DOCTOR);
			Attribution a = doc.makeAttribution(docs);
			game.getMaster().notify(a);
			doc.notify(a);
		}

		// Additionnal host and resistant
		Player host = players.get(rng.nextInt(rem));
		host.setGenome(Genome.HOST);
		Player res = host;
		while(res == host)
			res = players.get(rng.nextInt(rem));
		res.setGenome(Genome.RESISTANT);

		// Informative roles
		Player psy = players.remove(rng.nextInt(rem--)),
			   gen = players.remove(rng.nextInt(rem--)),
			   eng = players.remove(rng.nextInt(rem--)),
			   hac = players.remove(rng.nextInt(rem--)),
			   spy = players.remove(rng.nextInt(rem--));

		psy.setRole(Role.PSYCHOLOGIST);
		gen.setRole(Role.GENETICIST);
		eng.setRole(Role.COMPUTER_ENGINEER);
		hac.setRole(Role.HACKER);
		spy.setRole(Role.SPY);

		Attribution psyA = psy.makeAttribution(),
					genA = gen.makeAttribution(),
					engA = eng.makeAttribution(),
					hacA = hac.makeAttribution(),
					spyA = spy.makeAttribution();

		game.getMaster().notify(psyA);
		game.getMaster().notify(genA);
		game.getMaster().notify(engA);
		game.getMaster().notify(hacA);
		game.getMaster().notify(spyA);

		psy.notify(psyA);
		gen.notify(genA);
		eng.notify(engA);
		hac.notify(hacA);
		spy.notify(spyA);

		Psychoanalyse psyAction = new Psychoanalyse("p1", game, psy);
		Sequence genAction = new Sequence("g1", game, gen);
		Count engAction = new Count("c1", game, eng);
		Hack hacktion = new Hack(game, hac);
		Spy spyAction = new Spy(game, spy);

		hacktion.addChoice(psyAction);
		hacktion.addChoice(genAction);
		hacktion.addChoice(engAction);

		spyAction.spyAction(game.getMutantsActions());
		spyAction.spyAction(game.getDoctorsAction());
		spyAction.spyAction(psyAction);
		spyAction.spyAction(genAction);

		game.addInfoAction(psyAction);
		game.addInfoAction(genAction);
		game.addInfoAction(engAction);
		game.addInfoAction(hacktion);
		game.addInfoAction(spyAction);

		// If there are enough players, let's add a traitor
		if(rem > 0) {
			Player traitor = players.remove(rng.nextInt(rem--));
			traitor.setRole(Role.TRAITOR);
			Attribution a = traitor.makeAttribution();
			game.getMaster().notify(a);
			traitor.notify(a);
		}

		// Notify the others about their attribution
		for(Player p : players) {
			Attribution a = p.makeAttribution();
			game.getMaster().notify(a);
			p.notify(a);
		}
	}
}
