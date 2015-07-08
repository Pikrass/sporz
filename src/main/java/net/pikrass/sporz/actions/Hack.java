package net.pikrass.sporz.actions;

import net.pikrass.sporz.*;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Hack extends PlayerAction<Hack.Do>
{
	private Map<String, Hackable> possibleChoices;
	private String lastChoice;
	private Do choice;
	private Player hacker;

	public Hack(Game game, Player hacker) {
		super(game);
		this.hacker = hacker;
		this.possibleChoices = new HashMap<String, Hackable>();
		this.lastChoice = null;
	}

	public void addChoice(String label, Hackable action) {
		possibleChoices.put(label, action);
	}

	public Set<String> getChoices() {
		clean();
		return possibleChoices.keySet();
	}

	public String getLastChoice() {
		return lastChoice;
	}

	private void clean() {
		for(Iterator<Map.Entry<String, Hackable>> it =
				possibleChoices.entrySet().iterator() ; it.hasNext() ; ) {
			Map.Entry<String, Hackable> choice = it.next();
			if(!choice.getValue().isStillValid())
				it.remove();
		}
	}

	private boolean noPossibleChoice() {
		int size = possibleChoices.size();

		if(size == 0)
			return true;
		if(size > 1)
			return false;

		// Was the only choice used last time?
		return possibleChoices.keySet().iterator().next().equals(lastChoice);
	}

	public void startAction() {
		choice = null;

		if(!hacker.isAlive() || hacker.isParalysed()) {
			done(true);
			return;
		}

		clean();

		if(noPossibleChoice()) {
			done(true);
			return;
		}

		hacker.ask(game, this);
	}

	public void choose(Player player, Do choice) {
		this.choice = choice;
		done(choice != null);
	}

	public void executeAction() {
		if(choice == null) {
			lastChoice = null;
			return;
		}

		choice.run(game);

		lastChoice = choice.getLabel();
	}


	public class Do extends RunnableChoice {
		private String label;
		private Hackable role;

		public Do(String label) throws InvalidChoiceException {
			this.label = label;

			if(label != null) {
				this.role = possibleChoices.get(label);
				if(role == null || label.equals(lastChoice))
					throw new InvalidChoiceException();
			} else {
				this.role = null;
			}
		}

		public String getLabel() {
			return label;
		}

		@Override
		public void run(Game game) {
			if(role == null)
				return;

			role.hack(game, hacker);
		}
	}
}

