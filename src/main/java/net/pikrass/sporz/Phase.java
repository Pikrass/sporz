package net.pikrass.sporz;

import net.pikrass.sporz.actions.Action;

import java.util.List;
import java.util.ArrayList;

public class Phase
{
	private Game game;
	private List<Action> actions;
	private int nbDone;

	private Object mutex;

	public Phase(Game game) {
		this.game = game;
		this.actions = new ArrayList<Action>();
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public void run() {
		if(actions.isEmpty())
			return;

		this.mutex = new Object();

		nbDone = 0;

		for(Action action : actions) {
			Tracker tracker = new Tracker();
			action.start(tracker);
		}

		synchronized (mutex) {
			while(nbDone != actions.size()) {
				try {
					mutex.wait();
				} catch(InterruptedException e) {
				}
			}
		}

		for(Action action : actions) {
			action.execute();
		}
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

			synchronized (mutex) {
				if(state) {
					nbDone++;
					if(nbDone == actions.size())
						mutex.notifyAll();
				} else {
					nbDone--;
				}
			}
		}
	}
}
