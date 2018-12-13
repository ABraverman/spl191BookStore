package bgu.spl.mics.application.passiveObjects;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister {
	private static final MoneyRegister mr = new MoneyRegister();
	private ConcurrentLinkedQueue<OrderReceipt> receipts;
	
	private MoneyRegister(){
		receipts = new ConcurrentLinkedQueue<>();
	}
	
	/**
     * Retrieves the single instance of this class.
     */
	public static MoneyRegister getInstance() {
		return mr;
	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void file (OrderReceipt r) {
		receipts.add(r);
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		int totalEarnings = 0;
		for (OrderReceipt or : receipts)
			totalEarnings += or.getPrice();
		return totalEarnings;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {
		if (c.getAvailableCreditAmount() < amount)
			throw new IllegalArgumentException("Cannot charge customer, balance lower than charge");
		c.decrementCreditBalance(amount);
	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.. 
     */
	public void printOrderReceipts(String filename) {
		List<OrderReceipt> orl = new LinkedList<OrderReceipt>();
		for (OrderReceipt or : receipts)
			orl.add(or);
		try{
			FileOutputStream outFile = new FileOutputStream(filename);
			ObjectOutputStream mapWriter = new ObjectOutputStream(outFile);
			mapWriter.writeObject(orl);
			mapWriter.close();
			outFile.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return receipts.toString();
	}
}
