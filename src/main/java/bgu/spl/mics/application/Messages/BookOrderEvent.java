package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.*;


public class BookOrderEvent implements Event<OrderReceipt> {

    private Customer customer;
    private String book;
    private int orderTick;

    public BookOrderEvent(Customer c, String b, int t) {
        this.customer = c;
        this.book = b;
        this.orderTick = t;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getBook() {
        return book;
    }

    public int getOrderTick() {
        return orderTick;
    }
}
