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
	private PrintStream out;
	private ActionHandler handler;

	public TextPlayer(String name, PrintStream out) {
		super(name);
		this.out = out;
		this.handler = null;

		out.println("Welcome, "+name);
	}

	public void input(String msg) {
		if(handler != null)
			handler.onMessage(msg);
	}

	private interface ActionHandler {
		public void start();
		public void onMessage(String msg);
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
	public void notify(Psychoanalysis event) {
		if(!event.hasResult()) {
			out.println("You didn't psychanalyse anybody");
			return;
		}

		switch(event.getResult()) {
			case HUMAN:
				out.println(event.getTarget()+" is human"); break;
			case MUTANT:
				out.println(event.getTarget()+" is mutant"); break;
		}
	}

	@Override
	public void notify(Sequencing event) {
		if(!event.hasResult()) {
			out.println("You didn't sequence someone's genome");
			return;
		}

		switch(event.getResult()) {
			case STANDARD:
				out.println(event.getTarget()+" is standard"); break;
			case HOST:
				out.println(event.getTarget()+" is host"); break;
			case RESISTANT:
				out.println(event.getTarget()+" is resistant"); break;
		}
	}

	@Override
	public void notify(MutantCount event) {
		if(!event.hasResult()) {
			out.println("You didn't count the mutants");
			return;
		}

		if(event.getResult() <= 1)
			out.println("There is "+event.getResult()+" mutant");
		else
			out.println("There are "+event.getResult()+" mutants");
	}

	@Override
	public void notify(Psychoanalysis.Hacked event) {
		if(!event.hasResult()) {
			out.println("The psychologist saw nothing");
			return;
		}

		switch(event.getResult()) {
			case HUMAN:
				out.println("The psychologist saw: \""+event.getTarget()+" is human\""); break;
			case MUTANT:
				out.println("The psychologist saw: \""+event.getTarget()+" is mutant\""); break;
		}
	}

	@Override
	public void notify(Sequencing.Hacked event) {
		if(!event.hasResult()) {
			out.println("The geneticist saw nothing");
			return;
		}

		switch(event.getResult()) {
			case STANDARD:
				out.println("The geneticist saw: \""+event.getTarget()+" is standard\""); break;
			case HOST:
				out.println("The geneticist saw: \""+event.getTarget()+" is host\""); break;
			case RESISTANT:
				out.println("The geneticist saw: \""+event.getTarget()+" is resistant\""); break;
		}
	}

	@Override
	public void notify(MutantCount.Hacked event) {
		if(!event.hasResult()) {
			out.println("The computer engineer saw nothing");
			return;
		}

		if(event.getResult() <= 1)
			out.println("The computer engineer saw: \"there is "+event.getResult()+" mutant\"");
		else
			out.println("The computer engineer saw: \"there are "+event.getResult()+" mutants\"");
	}

	@Override
	public void notify(SpyReport event) {
		StringBuffer actions = new StringBuffer();

		for(SpyReport.Line line : event.getResult()) {
			switch(line.getType()) {
				case MUTATION:       actions.append("mutated, ");        break;
				case PARALYSIS:      actions.append("paralysed, ");      break;
				case HEALING:        actions.append("healed, ");         break;
				case PSYCHOANALYSIS: actions.append("psychoanalysed, "); break;
				case SEQUENCING:     actions.append("sequenced, ");      break;
			}
		}

		if(actions.length() == 0) {
			out.println("Nothing happened to "+event.getTarget()+" tonight");
		} else {
			actions.delete(actions.length()-2, actions.length());
			out.println(event.getTarget().toString()+" has been "+actions+" tonight");
		}
	}

	@Override
	public void notify(Lynching.Anonymous event) {
		if(event.isDraw()) {
			StringBuffer buf = new StringBuffer();
			for(Player p : event.getTargets())
				buf.append(p.toString()+", ");
			out.println("There is a draw between "+buf.substring(0, buf.length()-2));
		} else {
			if(event.getTarget().isNobody())
				out.println("Nobody is to be killed today");
			else
				out.println("We deciced to kill "+event.getTarget());
		}
	}

	@Override
	public void notify(LynchSettling event) {
		out.println("The captain, "+event.getOrigin()+" decided to kill "+event.getTarget());
	}


	@Override
	public void ask(final Game game, final ElectCaptain action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("Election! Vote for a captain!");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Player p = null;

				p = game.getPlayer(msg);
				if(p == null) {
					out.println("This player doesn't exist!");
				} else {
					try {
						action.choose(TextPlayer.this, action.new Vote(p));
						out.println("OK. You can change your vote until the end of the election.");
					} catch(BlankVoteProhibitedException e) {
						throw new Error("BlankVoteProhibitedException");
					}
				}

				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final MutantsActions action) {
		this.handler = (new ActionHandler() {
			Pattern cmdRegexp = Pattern.compile("(mutate|kill|paralyse) (.+)");

			@Override
			public void start() {
				out.println("Mutants! Do what you have to do...");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Matcher m = cmdRegexp.matcher(msg);
				if(!m.matches()) {
					out.println("I expect kill, mutate, or paralyse (with a target)");
					out.print("> ");
					return;
				}

				Player p;
				if(m.group(2).equals("nobody"))
					p = Player.NOBODY;
				else
					p = game.getPlayer(m.group(2));

				if(p == null) {
					out.println("This player doesn't exist!");
					out.print("> ");
					return;
				}

				MutantsActions.MutantChoice choice;
				if(m.group(1).equals("kill"))
					choice = action.new Kill(p);
				else if(m.group(1).equals("mutate"))
					choice = action.new Mutate(p);
				else
					choice = action.new Paralyse(p);

				action.choose(TextPlayer.this, choice);

				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final DoctorsAction action) {
		this.handler = (new ActionHandler() {
			Pattern cmdRegexp = Pattern.compile("(heal|kill) (.+)");

			@Override
			public void start() {
				out.println("Doctors! You can either heal or kill.");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Matcher m = cmdRegexp.matcher(msg);
				if(!m.matches()) {
					out.println("I expect kill or heal (with a target)");
					out.print("> ");
					return;
				}

				Player p;
				if(m.group(2).equals("nobody"))
					p = Player.NOBODY;
				else
					p = game.getPlayer(m.group(2));

				if(p == null) {
					out.println("This player doesn't exist!");
					out.print("> ");
					return;
				}

				DoctorsAction.DoctorChoice choice;
				if(m.group(1).equals("kill"))
					choice = action.new Kill(p);
				else
					choice = action.new Heal(p);

				action.choose(TextPlayer.this, choice);

				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final Psychoanalyse action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("You can choose someone to psychanalyse");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Player p = null;
				if(msg.equals("nobody"))
					p = Player.NOBODY;
				else
					p = game.getPlayer(msg);

				if(p == null) {
					out.println("This player doesn't exist!");
					out.print("> ");
					return;
				}

				action.choose(TextPlayer.this, action.new Do(p));
				out.println("OK. You can change your choice until the end of the phase.");
				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final Sequence action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("You can choose someone whose genome to sequence");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Player p = null;
				if(msg.equals("nobody"))
					p = Player.NOBODY;
				else
					p = game.getPlayer(msg);

				if(p == null) {
					out.println("This player doesn't exist!");
					out.print("> ");
					return;
				}

				action.choose(TextPlayer.this, action.new Do(p));
				out.println("OK. You can change your choice until the end of the phase.");
				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final Count action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("Do you want to count the mutants ?");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				boolean choice;
				if(msg.equals("yes")) {
					choice = true;
				} else if(msg.equals("no")) {
					choice = false;
				} else {
					out.println("Type yes or no");
					out.print("> ");
					return;
				}

				action.choose(TextPlayer.this, action.new Do(choice));
				out.println("OK. You can change your vote until the end of the election.");
				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final Hack action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("What role do you want to hack?");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				String choice = null;
				if(msg.equals("psy")) {
					choice = "p1";
				} else if(msg.equals("genet")) {
					choice = "g1";
				} else if(msg.equals("engineer")) {
					choice = "c1";
				} else if(msg.equals("nothing")) {
					choice = null;
				} else {
					out.println("Type psy, genet, engineer or nothing");
					out.print("> ");
					return;
				}

				try {
					action.choose(TextPlayer.this, action.new Do(choice));
					out.println("OK. You can change your vote until the end of the election.");
				} catch(InvalidChoiceException e) {
					out.println("You hacked this role last night, please choose another one.");
				}
				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final Spy action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("You can choose someone to spy on");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Player p = null;
				if(msg.equals("nobody"))
					p = Player.NOBODY;
				else
					p = game.getPlayer(msg);

				if(p == null) {
					out.println("This player doesn't exist!");
					out.print("> ");
				}

				action.choose(TextPlayer.this, action.new Do(p));
				out.println("OK. You can change your choice until the end of the phase.");
				out.print("> ");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final Lynch action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("Choose who you'd like to kill today");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Player p = null;
				if(msg.equals("nobody"))
					p = Player.NOBODY;
				else
					p = game.getPlayer(msg);

				if(p == null) {
					out.println("This player doesn't exist!");
					out.print("> ");
					return;
				}

				action.choose(TextPlayer.this, action.new Vote(p));
				out.println("OK. You can change your choice until the end of the phase.");
			}
		});

		this.handler.start();
	}

	@Override
	public void ask(final Game game, final SettleLynch action) {
		this.handler = (new ActionHandler() {
			@Override
			public void start() {
				out.println("There is a draw and you're the captain. Choose today's victim.");
				out.print("> ");
			}

			@Override
			public void onMessage(String msg) {
				Player p = null;
				if(msg.equals("nobody"))
					p = Player.NOBODY;
				else
					p = game.getPlayer(msg);

				if(p == null) {
					out.println("This player doesn't exist!");
					out.print("> ");
					return;
				}

				try {
					action.choose(TextPlayer.this, action.new Do(p));
				} catch(InvalidChoiceException e) {
					out.println("Please choose one of the players in a draw");
					out.print("> ");
				}
			}
		});

		this.handler.start();
	}

	@Override
	public void stopAsking(ElectCaptain action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(MutantsActions action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(DoctorsAction action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(Psychoanalyse action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(Sequence action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(Count action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(Hack action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(Spy action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(Lynch action) {
		this.handler = null;
	}

	@Override
	public void stopAsking(SettleLynch action) {
		this.handler = null;
	}
}
