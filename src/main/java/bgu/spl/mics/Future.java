package bgu.spl.mics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	
	private AtomicBoolean isDone;
	private T result;
	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		isDone = new AtomicBoolean(false);
		result = null;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * @post when resolved, returned == @param result
     *
     */
	public T get() {
		while (!isDone()) {
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) { }
		}
		return result;
	}
	
	/**
     * Resolves the result of this Future object.
     * @post isdone = true, result = @param result
     */
	public void resolve (T result) {

		this.result = result;
		isDone.set(true);
		synchronized (this) {
			this.notifyAll();
		}
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		return isDone.get();
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     * @post returns null after time out
     * @post if resolved, returned == @param result
     */
	public T get(long timeout, TimeUnit unit) {
		synchronized (this) {
			if (!isDone()) {
				try {
					unit.timedWait(this,timeout);
				} catch (InterruptedException e) { }
			}
			if (isDone())
				return result;
			else
				return null;
		}
	}

}
