package net.pikrass.sporz;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class Text {
	private static Object mutex;

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		Game game = new Game();

		System.out.print("Number of players: ");
		int nbPlayers = scan.nextInt();
		scan.nextLine();

		mutex = new Object();

		try {
			for(int i=1 ; i <= nbPlayers ; ++i) {
				System.out.print("Input fifo for player "+i+": ");
				String inPath = scan.nextLine();
				System.out.print("Output fifo for player "+i+": ");
				String outPath = scan.nextLine();

				FileInputStream in = new FileInputStream(inPath);
				PrintStream out = new PrintStream(new FileOutputStream(outPath));

				TextPlayer player = new TextPlayer(Integer.toString(i), out);
				InputThread thread = new InputThread(in, player);
				game.addPlayer(player);
				thread.start();
			}

			new StandardRules().apply(game);
			game.start();

		} catch(FileNotFoundException e) {
			System.err.println(e);
		} catch(RulesException e) {
			System.err.println("Wrong number of players");
		}
	}

	private static class InputThread extends Thread {
		private BufferedReader in;
		private TextPlayer player;

		public InputThread(InputStream in, TextPlayer player) {
			this.in = new BufferedReader(new InputStreamReader(in));
			this.player = player;
		}

		@Override
		public void run() {
			try {
				while(true) {
					String line = in.readLine();
					synchronized (mutex) {
						player.input(line);
					}
				}
			} catch(IOException e) {
			}
		}
	}
}
