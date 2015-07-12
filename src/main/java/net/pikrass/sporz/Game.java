package net.pikrass.sporz;

import net.pikrass.sporz.actions.Action;
import net.pikrass.sporz.actions.SwitchPeriod;
import net.pikrass.sporz.actions.ElectCaptain;
import net.pikrass.sporz.actions.MutantsActions;
import net.pikrass.sporz.actions.DoctorsAction;
import net.pikrass.sporz.actions.ResetParalysis;
import net.pikrass.sporz.actions.Lynch;
import net.pikrass.sporz.actions.SettleLynch;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class Game
{
	private boolean started;

	private Map<String, Player> players;
	private Player captain;

	private int round;
	private RoundPeriod period;

	private List<Phase> phases;
	private Phase captainPhase, mutantsPhase, doctorsPhase, infoPhase,
			dayPhase, settleLynchPhase;
	private PhaseIterator curPhase;

	private MutantsActions mutantsActions;
	private DoctorsAction doctorsAction;

	private Master master;

	public Game() {
		this.started = false;

		this.players = new HashMap<String, Player>();
		this.captain = null;

		this.round = 0;
		this.period = RoundPeriod.NIGHT;

		this.master = new UselessMaster();

		this.captainPhase = new Phase(this);
		this.mutantsPhase = new Phase(this);
		this.doctorsPhase = new Phase(this);
		this.infoPhase = new Phase(this);
		this.dayPhase = new Phase(this);
		this.settleLynchPhase = new Phase(this);

		this.phases = new LinkedList<Phase>();
		this.phases.add(mutantsPhase);
		this.phases.add(doctorsPhase);
		this.phases.add(infoPhase);
		this.phases.add(dayPhase);
		this.phases.add(settleLynchPhase);
		this.phases.add(captainPhase);

		this.mutantsActions = new MutantsActions(this);
		this.doctorsAction = new DoctorsAction(this);

		Lynch lynch = new Lynch(this);
		SettleLynch settle = new SettleLynch(this, lynch);

		mutantsPhase.addAction(new SwitchPeriod(this));
		mutantsPhase.addAction(this.mutantsActions);
		doctorsPhase.addAction(this.doctorsAction);
		dayPhase.addAction(new SwitchPeriod(this));
		dayPhase.addAction(new ResetParalysis(this));
		dayPhase.addAction(new ElectCaptain(this));
		dayPhase.addAction(lynch);
		settleLynchPhase.addAction(settle);
		captainPhase.addAction(new ElectCaptain(this));
	}

	public void start() {
		curPhase = new PhaseIterator(phases);

		started = true;
		loop();
	}

	private void loop() {
		Phase phase;

		// Go into DAY 0
		nextPeriod();

		// Skip all phases but the captain one for day 0
		while((phase = curPhase.next()) != this.captainPhase);

		while(true) {
			phase.run();
			phase = curPhase.next();
		}
	}

	public void addInfoAction(Action a) {
		infoPhase.addAction(a);
	}

	public MutantsActions getMutantsActions() {
		return mutantsActions;
	}

	public DoctorsAction getDoctorsAction() {
		return doctorsAction;
	}

	public void setMaster(Master master) {
		if(master == null)
			master = new UselessMaster();
		this.master = master;
	}

	public Master getMaster() {
		return master;
	}

	public void addPlayer(Player p) {
		players.put(p.getName(), p);
	}

	public Player getPlayer(String name) {
		return players.get(name);
	}

	public Iterator<Player> playerIterator() {
		return players.values().iterator();
	}

	public int getNbPlayers() {
		return players.size();
	}

	public Player getCaptain() {
		return captain;
	}

	public void setCaptain(Player player) {
		this.captain = player;
	}

	public void kill(Player player) {
		this.players.remove(player.getName());
		player.kill();
	}


	public void nextPeriod() {
		if(period == RoundPeriod.NIGHT) {
			period = RoundPeriod.DAY;
		} else {
			period = RoundPeriod.NIGHT;
			round++;
		}

		master.notifyRound(round, period);

		for(Player p : players.values())
			p.notifyRound(round, period);
	}

	private class PhaseIterator implements Iterator<Phase> {
		private List<Phase> phases;
		private Iterator<Phase> it;

		public PhaseIterator(List<Phase> phases) {
			this.phases = phases;
			this.it = phases.iterator();
		}

		public boolean hasNext() {
			return true;
		}

		public Phase next() {
			if(!it.hasNext())
				it = phases.iterator();
			return it.next();
		}

		public void remove() {
		}
	}
}
