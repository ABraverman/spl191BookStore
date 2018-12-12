package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
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
	private LinkedBlockingQueue<DeliveryVehicle> availableVehicles = new LinkedBlockingQueue<DeliveryVehicle>();
	private LinkedBlockingQueue<DeliveryVehicle> unavailableVehicles = new LinkedBlockingQueue<DeliveryVehicle>();
	
	/**
     * Retrieves the single instance of this class.
     */
	public static ResourcesHolder getInstance() {
		return rh;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> f = new Future<DeliveryVehicle>();
		try {
			f.resolve(availableVehicles.take());
		} catch (InterruptedException e) {}
		return f;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		unavailableVehicles.remove(vehicle);
		availableVehicles.add(vehicle);
	}
	
	/** 
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		for (int i=0;i<vehicles.length;i++)
			this.availableVehicles.add(vehicles[i]);
	}

}
