package net.pikrass.sporz;

import net.pikrass.sporz.actions.Action;

import java.util.List;
import java.util.ArrayList;

public class Phase
{
	private Game game;
	private String name;
	private List<Action> actions;
	private int nbDone;
	private boolean stopped;

	public Phase(Game game, String name) {
		this.game = game;
		this.name = name;
		this.actions = new ArrayList<Action>();
		this.stopped = false;
	}

	public String getName() {
		return name;
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public void run() {
		if(actions.isEmpty()) {
			game.step();
			return;
		}

		nbDone = 0;

		for(Action action : actions) {
			Tracker tracker = new Tracker();
			action.start(tracker);
		}
	}

	public void stop() {
		for(Action action : actions) {
			action.stop();
		}
		this.stopped = true;
	}

	private void end() {
		if(stopped)
			return;

		for(Action action : actions) {
			action.execute();
		}

		if(!stopped)
			game.step();
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
