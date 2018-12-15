package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo implements Serializable {

	private String bookTitle;
	private AtomicInteger amount;
	private AtomicInteger price;

	public BookInventoryInfo(String t ,int a, int p) {

		bookTitle = t;
		amount = new AtomicInteger(a);
		price = new AtomicInteger(p);

	}
	/**
     * Retrieves the bookTitle of this book.
     * <p>
     * @return The bookTitle of this book.   
     */
	public String getBookTitle() {

		return bookTitle;
	}

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	public int getAmountInInventory() {

		return amount.get();
	}

	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	public int getPrice() {

		return price.get();
	}

	public boolean take() {
	    if (amount.get() > 0) {
            amount.decrementAndGet();
            return true;
        }
        return false;
    }
	
	public String toString(){
		return "Title: " + bookTitle + ", Amount: " + amount + ", Price: " + price;
	}
	
}
