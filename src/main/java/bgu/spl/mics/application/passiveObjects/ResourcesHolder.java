package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
	private static final ResourcesHolder rh = new ResourcesHolder();
	private LinkedBlockingQueue<DeliveryVehicle> availableVehicles;
	private ConcurrentLinkedQueue<Future<DeliveryVehicle>> futures;
	
	/**
     * Retrieves the single instance of this class.
     */
	public static ResourcesHolder getInstance() {
		return rh;
	}

	private ResourcesHolder () {
		availableVehicles = new LinkedBlockingQueue<>();
		futures = new ConcurrentLinkedQueue<>();
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> f = new Future<>();
		DeliveryVehicle vehicle = availableVehicles.poll();
			if (vehicle != null)
				f.resolve(vehicle);
			else
				futures.add(f);
		return f;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		Future<DeliveryVehicle> f =futures.poll();
		if (f != null)
			futures.poll().resolve(vehicle);
		else {
			availableVehicles.add(vehicle);
		}





	}

	/** 
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
//	should change from array to a collection and use addAll
	public void load(DeliveryVehicle[] vehicles) {
		for (int i=0;i<vehicles.length;i++)
			this.availableVehicles.add(vehicles[i]);
	}

}
