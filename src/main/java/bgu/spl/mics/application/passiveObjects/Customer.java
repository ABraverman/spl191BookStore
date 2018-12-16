package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;
import javafx.util.Pair;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer implements Serializable {
	private int id;
	private String name;
	private String address;
	private int distance;
	private LinkedBlockingQueue<OrderReceipt> receipts = new LinkedBlockingQueue<>();
	private CreditCard creditCard;
	private OrderSchedule[] orderSchedule;
		
	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		return new LinkedList<OrderReceipt>(receipts);
	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		return creditCard.getAvailableAmountInCreditCard().get();
	}
	
	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() {
		return creditCard.getCreditCardNumber	();
	}
	
	public CreditCard getCreditCard(){
		return creditCard;
	}
	
	public List<Pair<String,Integer>> getOrderSchedule(){
		List<Pair<String,Integer>> os = new LinkedList<Pair<String,Integer>>(); 
		for (int i=0;i<orderSchedule.length;i++)
			os.add(orderSchedule[i].getOrderSchedule());
		return os;
	}
	/**
	 * reduces the amount given from the credit balance
	 * @param amount: amount of money to reduce from credit balance
	 */
	synchronized public void decrementCreditBalance(int amount){
		int tmp = creditCard.getAvailableAmountInCreditCard().get();
		while (!creditCard.getAvailableAmountInCreditCard().compareAndSet(tmp,tmp-amount))
			tmp = creditCard.getAvailableAmountInCreditCard().get();
	}

	public void addReceipt( OrderReceipt r) {
		this.receipts.add(r);
	}


	public String toString() {
		String rec;
		if (receipts == null)
			rec = "#### NO RECEIPTS ####";
		else{
			rec = "#### RECEIPTS ####\n";
			for (OrderReceipt or : receipts)
				rec += or.toString() + "\n";
			rec += "#### RECEIPTS ####\n";
		}
		String out = "Id: " + id + " creditcard: " + creditCard.toString() + " receipts: " + rec + "\n";
		out += "#### ORDERS SCHEDULE ####\n";
		for (int i=0;i<orderSchedule.length;i++)
			out += orderSchedule[i].toString() + "\n";
		out += "#### ORDERS SCHEDULE ####\n";
		return out;
	}
}

class CreditCard implements Serializable{
	private int number;
	private AtomicInteger amount;
	
	public CreditCard(int number, int amount){
		this.number = number;
		this.amount = new AtomicInteger(number);
	}
	
	public int getCreditCardNumber(){
		return this.number;
	}
	
	public AtomicInteger getAvailableAmountInCreditCard(){
		return amount;
	}
	
	public String toString(){
		return "Credit Number: " + number + ", Amount: " + amount;
	}
}

class OrderSchedule implements Serializable{
	private String bookTitle;
	private int tick;
	
	public Pair<String,Integer> getOrderSchedule(){
		return new Pair<String,Integer>(bookTitle,tick);
	}
	
	public String toString(){
		return "Title: " + bookTitle + ", Tick: " + tick;
	}
}