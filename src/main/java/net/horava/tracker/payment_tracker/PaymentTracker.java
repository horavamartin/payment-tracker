package net.horava.tracker.payment_tracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import net.horava.tracker.payment_tracker.account.Account;

/**
 * Payment tracker is application that keeps a record of payments.
 * <p>
 * This application needs Internet connection to download exchange rates.
 *
 * @author Martin Horava
 */
public class PaymentTracker {

	/**
	 * Account with all stored currencies and their values.
	 */
	public Account account;

	/**
	 * Thread for printing account balances.
	 */
	private Thread printer;

	public static void main(String[] args) {
		String path = "";

		if (args.length > 0) {
			path = args[0];
		}

		new PaymentTracker(path);
	}

	/**
	 * Constructor that initialize account, printer and then wait for user
	 * input.
	 */
	public PaymentTracker(String path) {
		account = new Account();
		if (!path.isEmpty())
			readFileInput(path);
		initPrinter();
		readInput();
	}

	/**
	 * Initialize thread for printing account balances.
	 */
	private void initPrinter() {
		printer = new Thread(account);
		printer.start();
	}

	/**
	 * It wait for user input from console. User can create payment or quit
	 * application.
	 */
	private void readInput() {
		Scanner scan = new Scanner(System.in);
		String input = "";
		Pattern pattern = Pattern
				.compile("([a-zA-Z]{3} (\\-)?(0|([1-9][0-9]*))|quit)");
		while (!input.equals("quit")) {
			input = scan.nextLine();
			if ((pattern.matcher(input).matches())) {
				try {
					account.createPaymentFromString(input);
					System.out.println("Transaction has been counted in.");
				} catch (IllegalArgumentException ie) {
					System.out
							.println("Ivalid currency. Please enter valid currency.");
				} catch (Exception e) {
					e.getLocalizedMessage();
				}
			} else {
				System.out
						.println("Invalid input. Please enter transaction or \"quit\".");
			}
		}
		printer.interrupt();
		System.out.print("End.");
		scan.close();
	}

	/**
	 * Read file of transactions and store it into the account.
	 * 
	 * @param path Path to file with transactions.
	 */
	private void readFileInput(String path) {
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(path));
		} catch (IOException e) {
			System.out.println("Could not read file: " + path);
			return;
		}

		for (String line : lines) {
			try {
				account.createPaymentFromString(line);
			} catch (Exception e) {
				System.out.println("Invalid command: \"" + line + "\"");
			}
		}

		System.out.println("File " + path + " is loaded.");
	}

}
