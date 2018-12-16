package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.BookStoreRunner;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.concurrent.ConcurrentLinkedQueue;

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
	private ConcurrentLinkedQueue<Future<DeliveryVehicle>> waitingFutures;

	public ResourceService(String name) {
		super(name);
		resourcesHolder = ResourcesHolder.getInstance();
		waitingFutures = new ConcurrentLinkedQueue<>();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.getTick() >= br.getDuration()) {
				for (Future<DeliveryVehicle> f : waitingFutures)
					f.resolve(null);
				this.terminate();
			}
		});

		subscribeEvent(AcquireVehicleEvent.class, ev -> {
			Future<DeliveryVehicle> f = resourcesHolder.acquireVehicle();
			complete(ev, f);
			if (!f.isDone())
				waitingFutures.add(f);
		});

		subscribeEvent(ReleaseVehicleEvent.class, ev -> {
			resourcesHolder.releaseVehicle(ev.getDeliveryVehicle());
			complete(ev , null);
		});
		BookStoreRunner.initCdl.countDown();
	}

}
