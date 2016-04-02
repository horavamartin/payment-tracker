package net.horava.tracker.payment_tracker.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represents currency and stored value in this currency.
 * <p>
 * Currency identifier is represented as three letter code.
 * 
 * @author Martin Horava
 *
 */
public class Currency {

	/**
	 * Identifier of default currency. Every other currency is converted to this
	 * value.
	 */
	public static String DEFAULT_CURRENCY = "USD";

	/**
	 * Exchange rate of currency to the default currency
	 */
	private double rate = 0;

	/**
	 * Identifier of currency
	 */
	private String id;

	/**
	 * Stored amount of money
	 */
	private int value = 0;

	/**
	 * It creates Currency class and store current exchange rate from given
	 * currency to default currency.
	 * 
	 * @param id
	 *            Identifier of given currency. It should be 3 character length
	 *            ISO code.
	 */
	public Currency(String id) {
		this.id = id.toUpperCase();
		downloadRate();
	}

	/**
	 * It returns rate to default currency
	 * 
	 * @return Rate to default currency.
	 */
	public double getRate() {
		return this.rate;
	}

	/**
	 * It stores value to currency
	 * 
	 * @param value
	 *            amount of currency
	 */
	public void addValue(int value) {
		this.value += value;
	}

	/**
	 * It returns value of this currency
	 * 
	 * @return value of this currency
	 */
	public int getValue() {
		return value;
	}

	/**
	 * It download current exchange rate from this currency to the default
	 * currency. Exchange rate is downloaded from yahoo.com web pages.
	 */
	private void downloadRate() {
		if (id != DEFAULT_CURRENCY) {
			URL url;
			try {
				url = new URL("http://quote.yahoo.com/d/quotes.csv?f=l1&s="
						+ DEFAULT_CURRENCY + this.id + "=X");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(url.openStream()));
				String line = reader.readLine();
				if (line.length() > 0 && !line.equals("N/A")) {
					rate = Double.parseDouble(line);
				}
				reader.close();
			} catch (MalformedURLException e1) {
				return;
			} catch (IOException e) {
				return;
			}
		} else {
			rate = 1;
		}
	}

	/**
	 * It prints amount of money stored in this currency.
	 */
	public void print() {
		if (value != 0) {
			if (id != DEFAULT_CURRENCY && rate != 0) {
				System.out.printf("%s %d (%s %.2f)\n", id, value,
						DEFAULT_CURRENCY, value / rate);
			} else if (id != DEFAULT_CURRENCY) {
				System.out.printf("%s %d (%s N/A)\n", id, value,
						DEFAULT_CURRENCY);
			} else {
				System.out.printf("%s %d\n", id, value);
			}
		}
	}

}
