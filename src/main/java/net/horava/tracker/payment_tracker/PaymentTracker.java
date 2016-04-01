package net.horava.tracker.payment_tracker;

import java.util.Scanner;
import java.util.regex.Pattern;

import net.horava.tracker.payment_tracker.account.Account;

/**
 * 
 *
 */
public class PaymentTracker {

    public static void main(String[] args) {
	for (String arg : args) {
	    System.out.println(arg);
	}

	Account account = new Account();
	example(account);

	Thread printer = new Thread(account);
	printer.start();

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

    public static void example(Account account) {
	account.createPayment("USD", 1000);
	account.createPayment("HKD", 100);
	account.createPayment("USD", -100);
	account.createPayment("RMB", 2000);
	account.createPayment("HKD", 200);
    }

}
