package net.pikrass.sporz;

import net.pikrass.sporz.actions.Psychoanalyse;
import net.pikrass.sporz.actions.Sequence;
import net.pikrass.sporz.actions.Count;
import net.pikrass.sporz.actions.Hack;
import net.pikrass.sporz.actions.Spy;
import net.pikrass.sporz.actions.Hackable;
import net.pikrass.sporz.actions.Spyable;
import net.pikrass.sporz.events.Attribution;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CustomRules extends Rules
{
	private Map<Role, Integer> roles;
	private int nbMutants;
	private int nbHosts;
	private int nbResistants;

	public CustomRules() {
		this.roles = new HashMap<Role, Integer>();
		this.initialRules();
	}

	public CustomRules(int nbPlayers) {
		this.roles = new HashMap<Role, Integer>();
		this.setNbPlayers(nbPlayers);
	}

	public void setNbPlayers(int nb) throws IllegalArgumentException {
		if(nb < 7)
			throw new IllegalArgumentException();

		this.initialRules();

		if(nb == 7)
			this.roles.put(Role.DOCTOR, 1);
		else if(nb > 8)
			this.roles.put(Role.TRAITOR, 1);
	}

	public Integer getRole(Role role) {
		return roles.get(role);
	}

	public void setRole(Role role, int num) {
		if(role == Role.ASTRONAUT)
			return;
		roles.put(role, num);
	}

	public void setMutants(int num) throws IllegalArgumentException {
		if(num < 1)
			throw new IllegalArgumentException();
		this.nbMutants = num;
	}

	public int getMutants() {
		return nbMutants;
	}

	public void setHosts(int num) {
		if(num < 0)
			return;
		this.nbHosts = num;
	}

	public int getHosts() {
		return nbHosts;
	}

	public void setResistants(int num) {
		if(num < 0)
			return;
		this.nbResistants = num;
	}

	public int getResistants() {
		return nbResistants;
	}

	public boolean isLyonRule() {
		return roles.get(Role.DOCTOR) == 1;
	}

	private void initialRules() {
		this.roles.clear();
		this.roles.put(Role.DOCTOR, 2);
		this.roles.put(Role.PSYCHOLOGIST, 1);
		this.roles.put(Role.GENETICIST, 1);
		this.roles.put(Role.COMPUTER_ENGINEER, 1);
		this.roles.put(Role.HACKER, 1);
		this.roles.put(Role.SPY, 1);
		this.roles.put(Role.TRAITOR, 0);
		this.nbMutants = 1;
		this.nbHosts = 1;
		this.nbResistants = 1;
	}


	public int minPlayers() {
		int roleCount = nbMutants;

		for(Map.Entry<Role, Integer> role : roles.entrySet()) {
			roleCount += role.getValue();
		}

		int genomeCount = roles.get(Role.DOCTOR)
			+ nbMutants + nbHosts + nbResistants;

		return Math.max(roleCount, genomeCount);
	}

	public int maxPlayers() {
		return Integer.MAX_VALUE;
	}



	private Player sample(List<Player> players) {
		int rem = players.size();
		return players.remove((int)Math.floor(Math.random()*rem));
	}

	private Player attribute(Game game, List<Player> players, Role role) {
		Player p = sample(players);
		p.setRole(role);

		Attribution a = p.makeAttribution();
		game.getMaster().notify(a);
		p.notify(a);

		return p;
	}

	protected void doApply(Game game) {
		ArrayList<Player> players = new ArrayList<Player>();

		for(Iterator<Player> it = game.playerIterator() ; it.hasNext() ; ) {
			players.add(it.next());
		}

		// Mutants
		for(int i=0 ; i < nbMutants ; ++i) {
			Player mutant = sample(players);
			mutant.setState(State.MUTANT);
			mutant.setGenome(Genome.HOST);
			Attribution mutA = mutant.makeAttribution();
			game.getMaster().notify(mutA);
			mutant.notify(mutA);
		}

		// Doctors
		List<Player> docs = new LinkedList<Player>();
		int nbDocs = roles.get(Role.DOCTOR);
		for(int i=0 ; i < nbDocs ; ++i) {
			Player doc = sample(players);

			doc.setRole(Role.DOCTOR);
			if(isLyonRule())
				doc.setGenome(Genome.RESISTANT);

			Attribution a = doc.makeAttribution(docs);
			game.getMaster().notify(a);
			doc.notify(a);

			docs.add(doc);
		}

		// Additionnal hosts and resistants
		ArrayList<Player> genPlayers = new ArrayList<Player>(players);
		for(int i=0 ; i < nbHosts ; ++i) {
			Player host = sample(genPlayers);
			host.setGenome(Genome.HOST);
		}
		for(int i=0 ; i < nbResistants ; ++i) {
			Player res = sample(genPlayers);
			res.setGenome(Genome.RESISTANT);
		}

		// Informative roles
		List<Hackable> hackables = new LinkedList<Hackable>();
		List<Spyable> spyables = new LinkedList<Spyable>();
		spyables.add(game.getMutantsActions());
		spyables.add(game.getDoctorsAction());

		int nbPsys = roles.get(Role.PSYCHOLOGIST),
			nbGens = roles.get(Role.GENETICIST),
			nbEngs = roles.get(Role.COMPUTER_ENGINEER);

		for(int i=0 ; i < nbPsys ; ++i) {
			Player p = attribute(game, players, Role.PSYCHOLOGIST);

			Psychoanalyse action = new Psychoanalyse("p"+(i+1), game, p);
			hackables.add(action);
			spyables.add(action);
			game.addInfoAction(action);
		}

		for(int i=0 ; i < nbGens ; ++i) {
			Player p = attribute(game, players, Role.GENETICIST);

			Sequence action = new Sequence("g"+(i+1), game, p);
			hackables.add(action);
			spyables.add(action);
			game.addInfoAction(action);
		}

		for(int i=0 ; i < nbEngs ; ++i) {
			Player p = attribute(game, players, Role.COMPUTER_ENGINEER);

			Count action = new Count("c"+(i+1), game, p);
			hackables.add(action);
			game.addInfoAction(action);
		}


		// Hackers
		int nbHackers = roles.get(Role.HACKER);

		for(int i=0 ; i < nbHackers ; ++i) {
			Player p = attribute(game, players, Role.HACKER);

			Hack action = new Hack(game, p);
			for(Hackable hackable : hackables)
				action.addChoice(hackable);
			game.addInfoAction(action);
		}


		// Spys
		int nbSpys = roles.get(Role.SPY);

		for(int i=0 ; i < nbSpys ; ++i) {
			Player p = attribute(game, players, Role.SPY);

			Spy action = new Spy(game, p);
			for(Spyable spyable : spyables)
				action.spyAction(spyable);
			game.addInfoAction(action);
		}


		// Other roles
		Map<Role, Integer> otherRoles = new HashMap<Role, Integer>(roles);
		otherRoles.remove(Role.DOCTOR);
		otherRoles.remove(Role.PSYCHOLOGIST);
		otherRoles.remove(Role.GENETICIST);
		otherRoles.remove(Role.COMPUTER_ENGINEER);
		otherRoles.remove(Role.HACKER);
		otherRoles.remove(Role.SPY);

		for(Map.Entry<Role, Integer> entry : otherRoles.entrySet()) {
			for(int i = entry.getValue() ; i > 0 ; --i) {
				attribute(game, players, entry.getKey());
			}
		}


		// Remaining players are astronauts
		for(Player p : players) {
			Attribution a = p.makeAttribution();
			game.getMaster().notify(a);
			p.notify(a);
		}
	}
}
