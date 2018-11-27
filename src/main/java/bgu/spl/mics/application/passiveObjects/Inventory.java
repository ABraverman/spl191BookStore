package bgu.spl.mics.application.passiveObjects;


/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {

	enum OrderResult  {NOT_IN_STOCK, SUCCESSFULLY_TAKEN}

	private static final Inventory inv = new Inventory();
	private BookInventoryInfo[ ] books;

	private Inventory() {
		books = null;
	}

	/**
     * Retrieves the single instance of this class.
	 * @POST this.getInstance() == this.getInstance()
     */
	public static Inventory getInstance() {
		return inv;
	}
	
	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
	 * @POST this.books == @param inventory;
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		inv.books = inventory;
	}

//	ONLY FOR TESTING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public BookInventoryInfo[ ] getBooks(){
	    return inv.books;
    }
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
		BookInventoryInfo bookInfo = inv.getBook(book);
		synchronized (bookInfo) {
			if (bookInfo != null && inv.checkAvailabile(book) > 0) {
				inv.removeBook(bookInfo);
				return OrderResult.SUCCESSFULLY_TAKEN;
			}

			return OrderResult.NOT_IN_STOCK;
		}
	}

	/**
	 * Checks book availability and returns the book info if available null otherwise.
	 * @param book
	 * @return book's bookInfo
	 * @POST this.checkAvailabilty(book).getBookTitle() == @Param book, positive test
     * @Post return null when there are no copies available
	 */
	protected int checkAvailabile (String book) {
        BookInventoryInfo info = inv.getBook(book);
        return info.getAmountInInventory();

	}

	/**
	 * takes a specified book from the inventory (removes its amount by 1)
	 * @param book
	 * @pre book.getAmountInInventory > 0
	 * @Post @pre @Param book.getAmountInInventory - 1 == @Post @Param book.getAmountInInventory()
	 */
    protected void removeBook (BookInventoryInfo book) {
		book.take();
	}

	private BookInventoryInfo getBook (String book) {
        for (BookInventoryInfo info : inv.books) {
            if (book == info.getBookTitle())
                return info;
        }
        return null;
    }

	
	
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		//TODO: Implement this
		return -1;
	}
	
	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		//TODO: Implement this
	}
}
