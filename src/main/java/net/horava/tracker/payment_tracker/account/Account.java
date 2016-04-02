package net.horava.tracker.payment_tracker.account;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * This is class that represents account. Account can have more currencies and
 * every currency has own value.
 * 
 * @author Martin Horava
 *
 */
public class Account implements Runnable {

	/**
	 * Default wait time between printing accaunt balances.
	 */
	private static long SLEEP_TIME = 10000;

	/**
	 * Account balances for every currency
	 */
	protected HashMap<String, Currency> balances = new HashMap<String, Currency>();

	/**
	 * It stores value in given currency.
	 * 
	 * @param currency
	 *            three character identifier for currency
	 * @param value
	 *            amount of money in payment
	 */
	public void createPayment(String currency, int value) {
		Currency balance;
		if (balances.containsKey(currency)) {
			balance = balances.get(currency);
		} else {
			balance = new Currency(currency);
			balances.put(currency, balance);
		}
		balance.addValue(value);
	}

	/**
	 * It parse and store value of currency represented by string.
	 * 
	 * @param input
	 *            String input of payment
	 * @throws Exception
	 */
	public void createPaymentFromString(String input) throws Exception {
		Pattern pattern = Pattern
				.compile("([a-zA-Z]{3}) ((\\-)?(0|([1-9][0-9]*)))");
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			String currency = matcher.group(1).toUpperCase();
			int value = Integer.parseInt(matcher.group(2));
			this.createPayment(currency, value);
		} else {
			throw new Exception("Invalid form of transaction.");
		}
	}

	/**
	 * It prints stored values in all currencies.
	 */
	public void printPayments() {
		for (String currency : balances.keySet()) {
			balances.get(currency).print();
		}
	}

	/**
	 * It prints account balances until it is terminated. Balances are printed
	 * every SLEEP_TIME ms.
	 */
	public void run() {
		while (true) {
			this.printPayments();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// Just exit
				return;
			}
		}
	}

}
