package net.pikrass.sporz;

import net.pikrass.sporz.actions.Action;

import java.util.List;
import java.util.ArrayList;

public class Phase
{
	private Game game;
	private List<Action> actions;
	private int nbDone;
	private boolean started;

	public Phase(Game game) {
		this.game = game;
		this.actions = new ArrayList<Action>();
		this.started = false;
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public void start() {
		if(started)
			throw new PhaseError("phase already started");

		if(actions.isEmpty())
			game.phaseEnded(this);

		started = true;
		nbDone = 0;

		for(Action action : actions) {
			Tracker tracker = new Tracker();
			action.start(tracker);
		}
	}

	private void end() {
		if(!started)
			throw new PhaseError("phase not yet started");

		for(Action action : actions) {
			action.execute();
		}

		started = false;
		game.phaseEnded(this);
	}

	public class Tracker {
		private boolean state;

		private Tracker() {
			this.state = false;
		}

		public void done(boolean isDone) {
			if(isDone == state)
				return;

			state = isDone;

			if(state) {
				nbDone++;
				if(nbDone == actions.size())
					end();
			} else {
				nbDone--;
			}
		}
	}
}
