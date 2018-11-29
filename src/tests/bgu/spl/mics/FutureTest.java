package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest {
    Future<Integer> f;
    @Before
    public void setUp() throws Exception {
        f = new Future<>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void get() {
        Integer i;
        f.resolve(5);
        i = f.get();
        assertEquals((int) i,5);

    }

    @Test
    public void resolve() {
        Integer i;
        boolean b = false;
        f.resolve(5);
        i = f.get();
        b = f.isDone();
        assertEquals((int) i,5);
        assertTrue(b);
    }


    @Test
    public void isDonePos() {
        boolean b = false;
        f.resolve(5);
        b = f.isDone();
        assertTrue(b);
    }

    @Test
    public void isDoneNeg() {
        boolean b = false;
        f.resolve(5);
        b = f.isDone();
        assertFalse(b);
    }

    @Test
    public void getTimedPos() {
        Integer i;
        f.resolve(5);
        i = f.get();
        assertEquals((int) i,5);
    }

    @Test
    public void getTimedNeg() {
        Integer i;
        i = f.get(2, TimeUnit.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {}
        assertNull(i);
    }
}