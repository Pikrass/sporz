package net.pikrass.sporz;

import net.pikrass.sporz.actions.*;
import net.pikrass.sporz.events.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class TextPlayer extends Player
{
	private BufferedReader in;
	private PrintStream out;

	private StringBuffer line;
	private Thread source, sink;

	public TextPlayer(String name, InputStream in, PrintStream out) {
		super(name);
		this.in = new BufferedReader(new InputStreamReader(in));
		this.out = out;

		out.println("Welcome, "+name);

		this.line = new StringBuffer();
		this.source = new SourceThread();
		this.source.start();
	}

	public class SourceThread extends Thread {
		@Override
		public void run() {
			String read;
			try {
				while(true) {
					read = in.readLine();
					synchronized (line) {
						line.delete(0, line.length());
						line.append(read);
						line.notify();
					}
				}
			} catch(IOException e) {
				System.err.println("Input closed ("+getName()+")");
			}
		}
	}

	@Override
	public void notifyRound(int num, RoundPeriod period) {
		String periodName = period == RoundPeriod.DAY ? "DAY" : "NIGHT";
		out.println(String.format("===== %s %d =====", periodName, num));
	}

	@Override
	public void notify(Attribution event) {
		if(event.getState() == State.MUTANT) {
			out.println("You're a mutant");
		} else if(event.getRole() == Role.DOCTOR) {
			out.print("You're a doctor with ");

			boolean first = true;
			for(Player other : event.getGroup()) {
				if(other.equals(this))
					continue;

				if(first)
					out.print(other);
				else
					out.print(", "+other);
				first = false;
			}
			out.println();
		} else {
			switch(event.getRole()) {
				case PSYCHOLOGIST:
					out.println("You're a psychologist");
					break;
				case GENETICIST:
					out.println("You're a geneticist");
					break;
				case COMPUTER_ENGINEER:
					out.println("You're a computer engineer");
					break;
				case HACKER:
					out.println("You're a hacker");
					break;
				case SPY:
					out.println("You're a spy");
					break;
				case TRAITOR:
					out.println("You're a traitor");
					break;
				case ASTRONAUT:
					out.println("You're an astronaut");
					break;
				default:
			}
		}
	}

	@Override
	public void notify(NewCaptain event) {
		out.printf("\n%s was elected captain!\n", event.getWinner());
	}

	@Override
	public void notifyOrigin(Paralysis event) {
		out.println("We paralysed "+event.getTarget());
	}

	@Override
	public void notifyTarget(Paralysis event) {
		out.println("You've been paralysed");
	}

	@Override
	public void notifyOrigin(Mutation.NoResult event) {
		out.println("We tried to mutate "+event.getTarget());
	}

	@Override
	public void notifyTarget(Mutation event) {
		switch(event.getResult()) {
			case SUCCESS:
				out.println("You've been mutated (it suceeded)"); break;
			case FAIL:
				out.println("They tried to mutate you but it failed"); break;
			case USELESS:
				out.println("You've been mutated (it was useless)"); break;
		}
	}

	@Override
	public void notify(Murder event) {
		if(event.getOrigin() == Murder.Origin.MUTANTS)
				out.print("Mutants");
		else
				out.print("Doctors");
		out.println(" killed "+event.getTarget());
		//Note: we should tell the role, genome and state of the victim too
	}

	@Override
	public void notifyOrigin(Healing.NoResult event) {
		out.println("We tried to heal "+event.getTarget());
	}

	@Override
	public void notifyTarget(Healing event) {
		switch(event.getResult()) {
			case SUCCESS:
				out.println("You've been healed (it suceeded)"); break;
			case FAIL:
				out.println("They tried to heal you but it failed"); break;
			case USELESS:
				out.println("You've been healed (it was useless)"); break;
		}
	}


	@Override
	public void ask(final Game game, final ElectCaptain action) {
		this.sink = (new Thread() {
			@Override
			public void run() {
				Player p = null;

				out.println("Election! Vote for a captain!");

				while(true) {
					out.print("> ");

					String nick = null;
					synchronized (line) {
						try {
							line.wait();
						} catch(InterruptedException e) {
							return;
						}
						nick = line.toString();
					}

					p = game.getPlayer(nick);
					if(p == null) {
						out.println("This player doesn't exist!");
						continue;
					}

					try {
						action.choose(TextPlayer.this, action.new Vote(p));
						out.println("OK. You can change your vote until the end of the election.");
					} catch(BlankVoteProhibitedException e) {
						throw new Error("BlankVoteProhibitedException");
					}
				}
			}
		});

		this.sink.start();
	}

	@Override
	public void ask(final Game game, final MutantsActions action) {
		this.sink = (new Thread() {
			Pattern cmdRegexp = Pattern.compile("(mutate|kill|paralyse) (.+)");

			@Override
			public void run() {
				out.println("Mutants! Do what you have to do...");

				while(true) {
					out.print("> ");

					String cmd = null;
					synchronized (line) {
						try {
							line.wait();
						} catch(InterruptedException e) {
							return;
						}
						cmd = line.toString();
					}

					Matcher m = cmdRegexp.matcher(cmd);
					if(!m.matches()) {
						out.println("I expect kill, mutate, or paralyse (with a target)");
						continue;
					}

					Player p;
					if(m.group(2).equals("nobody"))
						p = Player.NOBODY;
					else
						p = game.getPlayer(m.group(2));

					if(p == null) {
						out.println("This player doesn't exist!");
						continue;
					}

					MutantsActions.MutantChoice choice;
					if(m.group(1).equals("kill"))
						choice = action.new Kill(p);
					else if(m.group(1).equals("mutate"))
						choice = action.new Mutate(p);
					else
						choice = action.new Paralyse(p);

					action.choose(TextPlayer.this, choice);
				}
			}
		});

		this.sink.start();
	}

	@Override
	public void ask(final Game game, final DoctorsAction action) {
		this.sink = (new Thread() {
			Pattern cmdRegexp = Pattern.compile("(heal|kill) (.+)");

			@Override
			public void run() {
				out.println("Doctors! You can either heal or kill.");

				while(true) {
					out.print("> ");

					String cmd = null;
					synchronized (line) {
						try {
							line.wait();
						} catch(InterruptedException e) {
							return;
						}
						cmd = line.toString();
					}

					Matcher m = cmdRegexp.matcher(cmd);
					if(!m.matches()) {
						out.println("I expect kill or heal (with a target)");
						continue;
					}

					Player p;
					if(m.group(2).equals("nobody"))
						p = Player.NOBODY;
					else
						p = game.getPlayer(m.group(2));

					if(p == null) {
						out.println("This player doesn't exist!");
						continue;
					}

					DoctorsAction.DoctorChoice choice;
					if(m.group(1).equals("kill"))
						choice = action.new Kill(p);
					else
						choice = action.new Heal(p);

					action.choose(TextPlayer.this, choice);
				}
			}
		});

		this.sink.start();
	}

	@Override
	public void stopAsking(ElectCaptain action) {
		this.sink.interrupt();
	}

	@Override
	public void stopAsking(MutantsActions action) {
		this.sink.interrupt();
	}

	@Override
	public void stopAsking(DoctorsAction action) {
		this.sink.interrupt();
	}
}
