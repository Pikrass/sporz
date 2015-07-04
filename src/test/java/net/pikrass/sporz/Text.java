package net.pikrass.sporz;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Text {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		Game game = new Game();

		System.out.print("Number of players: ");
		int nbPlayers = scan.nextInt();
		scan.nextLine();

		try {
			for(int i=1 ; i <= nbPlayers ; ++i) {
				System.out.print("Input fifo for player "+i+": ");
				String inPath = scan.nextLine();
				System.out.print("Output fifo for player "+i+": ");
				String outPath = scan.nextLine();

				FileInputStream in = new FileInputStream(inPath);
				PrintStream out = new PrintStream(new FileOutputStream(outPath));

				game.addPlayer(new TextPlayer(Integer.toString(i), in, out));
			}
		} catch(FileNotFoundException e) {
			System.err.println(e);
		}

		try {
			new StandardRules().apply(game);
			game.start();
		} catch(RulesException e) {
			System.err.println("Wrong number of players");
		}
	}
}
