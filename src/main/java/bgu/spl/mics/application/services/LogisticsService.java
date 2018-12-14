package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.passiveObjects.*;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

	public LogisticsService( String name) {
		super(name);
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.getTick() >= br.getDuration())
				this.terminate();
		});

		subscribeEvent(DeliveryEvent.class, ev -> {
			Future<Future<DeliveryVehicle>> future = sendEvent(new AcquireVehicleEvent());
			if (future != null && future.get() != null && future.get().get() != null) {
				DeliveryVehicle deliveryVehicle = future.get().get();
				deliveryVehicle.deliver(ev.getCustomer().getAddress(), ev.getCustomer().getDistance());
				sendEvent(new ReleaseVehicleEvent(deliveryVehicle));
			}
			complete(ev, null);

		});
		
	}

}
