package net.pikrass.sporz;

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
		Player mutant = players.remove((int)Math.floor(Math.random()*rem--));
		mutant.setState(State.MUTANT);
		mutant.setGenome(Genome.HOST);
		mutant.notify(mutant.makeAttribution());

		// Doctors
		List<Player> docs = new LinkedList<Player>();
		docs.add(players.remove((int)Math.floor(Math.random()*rem--)));
		docs.add(players.remove((int)Math.floor(Math.random()*rem--)));
		for(Player doc : docs) {
			doc.setRole(Role.DOCTOR);
			doc.notify(doc.makeAttribution(docs));
		}

		// Additionnal host and resistant
		Player host = players.get((int)Math.floor(Math.random()*rem));
		host.setGenome(Genome.HOST);
		Player res = host;
		while(res == host)
			res = players.get((int)Math.floor(Math.random()*rem));
		res.setGenome(Genome.RESISTANT);

		// Informative roles
		Player psy = players.remove((int)Math.floor(Math.random()*rem--)),
			   gen = players.remove((int)Math.floor(Math.random()*rem--)),
			   eng = players.remove((int)Math.floor(Math.random()*rem--)),
			   hac = players.remove((int)Math.floor(Math.random()*rem--)),
			   spy = players.remove((int)Math.floor(Math.random()*rem--));

		psy.setRole(Role.PSYCHOLOGIST);
		gen.setRole(Role.GENETICIST);
		eng.setRole(Role.COMPUTER_ENGINEER);
		hac.setRole(Role.HACKER);
		spy.setRole(Role.SPY);
		psy.notify(psy.makeAttribution());
		gen.notify(gen.makeAttribution());
		eng.notify(eng.makeAttribution());
		hac.notify(hac.makeAttribution());
		spy.notify(spy.makeAttribution());

		// If there are enough players, let's add a traitor
		if(rem > 0) {
			Player traitor = players.remove((int)Math.floor(Math.random()*rem--));
			traitor.setRole(Role.TRAITOR);
			traitor.notify(traitor.makeAttribution());
		}

		// Notify the others about their attribution
		for(Player p : players)
			p.notify(p.makeAttribution());
	}
}
