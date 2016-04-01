package net.horava.tracker.payment_tracker.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author Martin Horava
 *
 */
public class Currency {

    public static String DEFAULT_CURRENCY = "USD";

    /**
     * Exchange rate of currency to USD
     */
    private double rateToUSD = 0;

    private String name;

    private int value = 0;

    /**
     * 
     * @param rateToUSD
     */
    public Currency(String name) {
	this.name = name.toUpperCase();
	downloadRateToUSD();
    }

    /**
     * 
     * @return
     */
    public double getRateToUSD() {
	return this.rateToUSD;
    }

    public void addValue(int value) {
	this.value += value;
    }

    public int getValue() {
	return value;
    }

    private void downloadRateToUSD() {
	if (name != DEFAULT_CURRENCY) {
	    URL url;
	    try {
		url = new URL("http://quote.yahoo.com/d/quotes.csv?f=l1&s="
			+ DEFAULT_CURRENCY + this.name + "=X");
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(url.openStream()));
		String line = reader.readLine();
		if (line.length() > 0 && !line.equals("N/A")) {
		    rateToUSD = Double.parseDouble(line);
		}
		reader.close();
	    } catch (MalformedURLException e1) {
		return;
	    } catch (IOException e) {
		return;
	    }
	} else {
	    rateToUSD = 1;
	}
    }

    public void print() {
	if (value != 0) {
	    if (name != DEFAULT_CURRENCY && rateToUSD != 0) {
		System.out.printf("%s %d (%s %.2f)\n", name, value,
			DEFAULT_CURRENCY, value / rateToUSD);
	    } else if (name != DEFAULT_CURRENCY) {
		System.out.printf("%s %d (%s N/A)\n", name, value,
			DEFAULT_CURRENCY);
	    } else {
		System.out.printf("%s %d\n", name, value);
	    }
	}
    }

}
