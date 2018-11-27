package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    private Inventory invent;

    @Before
    public void setUp() throws Exception {
        invent = createInventory();
    }

    @After
    public void tearDown() throws Exception {
    }

    protected Inventory createInventory() {
        return Inventory.getInstance();
    }

    @Test
    public void getInstance() {
        assertEquals(Inventory.getInstance(),invent);
    }

    @Test
    public void load() {
    }

    @Test
    public void take() {
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
    }

    @Test
    public void printInventoryToFile() {
    }

    @Test
    public void getInstance1() {
    }

    @Test
    public void load1() {
    }

    @Test
    public void take1() {
    }

    @Test
    public void checkAvailabiltyAndGetPrice1() {
    }

    @Test
    public void printInventoryToFile1() {
    }
}