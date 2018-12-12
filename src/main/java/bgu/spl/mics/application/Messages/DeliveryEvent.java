package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class DeliveryEvent implements Event {

    Customer customer;

    public DeliveryEvent (Customer c) {
        this.customer = c;
    }

    public Customer getCustomer() {
        return customer;
    }
}
