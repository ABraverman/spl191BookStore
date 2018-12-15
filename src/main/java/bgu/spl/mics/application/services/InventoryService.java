package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.BookStoreRunner;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.passiveObjects.*;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{

	private Inventory inventory;

	public InventoryService( String name) {
		super(name);
		inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {

		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.getTick() >= br.getDuration())
				this.terminate();
		});

		subscribeEvent(GetBookPriceEvent.class, ev -> {
			complete(ev,inventory.checkAvailabiltyAndGetPrice(ev.getBook()));
		});

		subscribeEvent(TakeBookEvent.class, ev -> {
			complete(ev, inventory.take(ev.getBook()));
		});
		BookStoreRunner.initCdl.countDown();
	}

}
