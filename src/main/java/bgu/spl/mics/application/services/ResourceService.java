package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.passiveObjects.*;
import java.util.concurrent.CountDownLatch;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{

	private ResourcesHolder resourcesHolder;

	public ResourceService(String name, CountDownLatch cdl) {
		super(name);
		resourcesHolder = ResourcesHolder.getInstance();
		this.cdl = cdl;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.getTick() >= br.getDuration())
				this.terminate();
		});

		subscribeEvent(AcquireVehicleEvent.class, ev -> complete(ev, resourcesHolder.acquireVehicle()));

		subscribeEvent(ReleaseVehicleEvent.class, ev -> {
			resourcesHolder.releaseVehicle(ev.getDeliveryVehicle());
			complete(ev , null);
		});
		
	}

}
