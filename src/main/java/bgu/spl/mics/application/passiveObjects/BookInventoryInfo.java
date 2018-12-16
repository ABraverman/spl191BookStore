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
	private int price;

	public BookInventoryInfo(String t ,int a, int p) {

		bookTitle = t;
		amount = new AtomicInteger(a);
		price = p;

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

		return price;
	}

	public OrderResult take() {

	    if (amount.get() > 0) {
            amount.decrementAndGet();
            return OrderResult.SUCCESSFULLY_TAKEN;
        }
        return OrderResult.NOT_IN_STOCK;
    }
	
	public String toString(){
		return "Title: " + bookTitle + ", Amount: " + amount + ", Price: " + price;
	}
	
}
