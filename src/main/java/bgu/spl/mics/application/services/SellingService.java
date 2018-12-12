package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.Messages.*;


/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{

	private MoneyRegister moneyRegister;
	private int tick;


	public SellingService(String name) {
		super(name);
		moneyRegister = MoneyRegister.getInstance();
		tick = 0;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			this.tick = br.getTick();
			if (tick >= br.getDuration()) {
				this.terminate();
			}
		});

		subscribeEvent(BookOrderEvent.class, ev -> {
			Integer bookPrice = sendEvent(new GetBookPriceEvent(ev.getBook())).get();
			if (bookPrice != null) {
                OrderReceipt receipt = new OrderReceipt(0, this.getName(), ev.getCustomer().getId(), ev.getBook(), bookPrice, ev.getOrderTick(), tick);
                synchronized (ev.getCustomer()) {
                    if ((bookPrice != -1) && (ev.getCustomer().getAvailableCreditAmount() >= bookPrice)) {
                        if (sendEvent(new TakeBookEvent(ev.getBook())).get() == OrderResult.SUCCESSFULLY_TAKEN) {
                            moneyRegister.chargeCreditCard(ev.getCustomer(), bookPrice);
                        } else {
                            complete(ev, null);
                            return;
                        }
                    } else {
						complete(ev, null);
						return;
					}
                }
                receipt.setIssueTick(tick);
				complete(ev, receipt);
                sendEvent(new DeliveryEvent(ev.getCustomer()));
                moneyRegister.file(receipt);
            }
            complete(ev, null);

		});
		
	}

}
