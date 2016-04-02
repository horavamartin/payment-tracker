package net.horava.tracker.payment_tracker;

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
		for (String arg : args) {
			System.out.println(arg);
		}

		new PaymentTracker();
	}

	/**
	 * Constructor that initialize account, printer and then wait for user
	 * input.
	 */
	public PaymentTracker() {
		account = new Account();
		example(account);
		initPrinter();
		readInput();
	}

	/**
	 * Initialize thread for printing account balances.
	 */
	public void initPrinter() {
		printer = new Thread(account);
		printer.start();
	}

	/**
	 * It wait for user input from console. User can create payment or quit application.
	 */
	public void readInput() {
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
		System.out.println("End.");
		scan.close();
	}

	/**
	 * TODO: odstranit
	 * 
	 * Example values. 
	 * 
	 * @param account
	 */
	public void example(Account account) {
		account.createPayment("USD", 1000);
		account.createPayment("HKD", 100);
		account.createPayment("USD", -100);
		account.createPayment("RMB", 2000);
		account.createPayment("HKD", 200);
	}

}
