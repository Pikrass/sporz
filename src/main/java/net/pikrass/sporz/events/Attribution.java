package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

import java.util.List;
import java.util.LinkedList;

public class Attribution extends Event {
	private Player player;
	private Role role;
	private State state;
	private List<Player> group;

	public Attribution(Player player, Role role) {
		this.player = player;
		this.role = role;
		this.state = State.HUMAN;
		this.group = null;
	}

	public Attribution(Player player, Role role, State state) {
		this.player = player;
		this.role = role;
		this.state = state;
		this.group = null;
	}

	public Attribution(Player player, Role role, List<Player> group) {
		this.player = player;
		this.role = role;
		this.state = State.HUMAN;
		this.group = new LinkedList<Player>(group);
	}

	public Attribution(Player player, Role role, State state, List<Player> group) {
		this.player = player;
		this.role = role;
		this.state = state;
		this.group = new LinkedList<Player>(group);
	}

	public Player getPlayer() {
		return player;
	}

	public Role getRole() {
		return role;
	}

	public State getState() {
		return state;
	}

	public List<Player> getGroup() {
		return group;
	}
}
