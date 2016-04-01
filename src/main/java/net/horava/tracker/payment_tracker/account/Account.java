package net.horava.tracker.payment_tracker.account;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account implements Runnable {

    private static long SLEEP_TIME = 10000;

    protected HashMap<String, Currency> balances = new HashMap<String, Currency>();

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

    public void printPayments() {
	// TODO: dat pryc
	System.out.println("Account balances:");
	for (String currency : balances.keySet()) {
	    balances.get(currency).print();
	}
    }

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
