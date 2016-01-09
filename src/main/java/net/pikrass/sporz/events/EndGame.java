package net.pikrass.sporz.events;

public class EndGame extends Event {
	public enum Winner {
		HUMANS,
		MUTANTS,
		DRAW
	}

	public enum Reason {
		ANNIHILATION,
		ASSURED_VICTORY,
		CUSTOM,
		NO_REASON
	}

	private Winner winner;
	private Reason reason;
	private String customReason;

	public EndGame(Winner winner) {
		this.winner = winner;
		this.reason = Reason.NO_REASON;
	}

	public EndGame(Winner winner, Reason reason) {
		this.winner = winner;
		this.reason = reason;
	}

	public EndGame(Winner winner, String reason) {
		this.winner = winner;
		this.reason = Reason.CUSTOM;
		this.customReason = reason;
	}

	public Winner getWinner() {
		return winner;
	}

	public Reason getReason() {
		return reason;
	}

	public String getCustomReason() {
		return customReason;
	}
}
