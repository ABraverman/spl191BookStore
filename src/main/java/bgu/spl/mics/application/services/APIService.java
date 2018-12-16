package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.passiveObjects.*;
import javafx.util.Pair;
import bgu.spl.mics.application.BookStoreRunner;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{

	private Customer customer;
	private List<Pair<String,Integer>> orderSchedule;
	private ConcurrentHashMap<Message, Future<OrderReceipt>> futures;

	public APIService(String name, Customer c) {
		super(name);
		this.customer = c;
		this.orderSchedule = this.customer.getOrderSchedule();
		futures = new ConcurrentHashMap<>();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.getTick() >= br.getDuration()) {
				this.terminate();
			}
			else {
				List<Pair<String, Integer>> eToRemove = new LinkedList<>();
				for (Pair<String, Integer> p : orderSchedule) {
					if (p.getValue() == br.getTick()) {
						
						Event e = new BookOrderEvent(customer, p.getKey(), br.getTick());
						Future future = sendEvent(e);
						if (future != null)
							futures.put(e,future);
						eToRemove.add(p);
					}
				}
				for (Pair<String, Integer> p : eToRemove) {
					orderSchedule.remove(p);
				}
				List<Message> fToRemove = new LinkedList<>();
				for (Map.Entry<Message, Future<OrderReceipt>> f : futures.entrySet()) {
					if (f.getValue() != null && f.getValue().get() != null ) {
						customer.addReceipt(f.getValue().get());
					}
					fToRemove.add(f.getKey());
				}
				for (Message m: fToRemove) {
					futures.remove(m);
				}

			}
		});
		BookStoreRunner.initCdl.countDown();
	}

}
