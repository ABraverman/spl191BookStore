package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    private Inventory inv;

    @Before
    public void setUp() throws Exception {
        inv = createInventory();
    }

    @After
    public void tearDown() throws Exception {
    }

    protected Inventory createInventory() {
        return Inventory.getInstance();
    }

    @Test
    public void getInstance() {
        assertEquals(Inventory.getInstance(),inv);
    }

    @Test
    public void load() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 5 ,3);
        BookInventoryInfo b2 = new BookInventoryInfo("b", 3 ,2);
        BookInventoryInfo b3 = new BookInventoryInfo("c", 8 ,50);
        BookInventoryInfo[ ] books = {b1,b2,b3};
        inv.load(books);
        assertArrayEquals(books,inv.getBooks());
    }

    @Test
    public void take() {
    }

    @Test
    public void checkAvailabiltyPos() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 5 ,3);
        BookInventoryInfo[ ] books = {b1};
        inv.load(books);
        assertTrue(inv.checkAvailabile("a") == 5);

    }

    @Test
    public void checkAvailabiltyPNeg() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 0 ,3);
        BookInventoryInfo[ ] books = {b1};
        inv.load(books);
        assertFalse(inv.checkAvailabile("a") == 0);

    }

    @Test
    public void removeBook() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 0 ,3);
        BookInventoryInfo[ ] books = {b1};
        inv.load(books);
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {

    }

    @Test
    public void printInventoryToFile() {
    }


}