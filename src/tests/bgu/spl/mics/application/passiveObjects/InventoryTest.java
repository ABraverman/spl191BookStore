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
        assertTrue(inv.checkAvailability("a") == 5);

    }

    @Test
    public void checkAvailabiltyNeg() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 0 ,3);
        BookInventoryInfo[ ] books = {b1};
        inv.load(books);
        assertFalse(inv.checkAvailability("a") == 0);

    }

    @Test
    public void removeBookExpected() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 5 ,3);
        BookInventoryInfo[ ] books = {b1};
        inv.load(books);
        inv.removeBook("a");
        assertEquals(inv.checkAvailability("a"), 4);
    }

    @Test
    public void checkAvailabiltyAndGetPriceNoSuchBook() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 5 ,3);
        BookInventoryInfo[ ] books = {b1};
        inv.load(books);
        int p = inv.checkAvailabiltyAndGetPrice("b");
        assertEquals(p,-1);
    }

    @Test
    public void checkAvailabiltyAndGetPriceExpectedValue() {
        BookInventoryInfo b1 = new BookInventoryInfo("a", 5 ,3);
        BookInventoryInfo[ ] books = {b1};
        inv.load(books);
        int p = inv.checkAvailabiltyAndGetPrice("a");
        assertEquals(p,3);

    }


}