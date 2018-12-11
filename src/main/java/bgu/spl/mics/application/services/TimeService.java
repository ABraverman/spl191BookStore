package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.Messages.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private String name;
	private int speed;
	private int duration;
	private Timer timer;
	private int tick;

	public TimeService(String name, int speed, int duration) {
		super(name);
		this.speed = speed;
		this.duration = duration;
		timer = new Timer();
		tick = 1;


	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class,ev -> {
			if (ev.getTick() >= ev.getDuration()) {
				timer.purge();
				timer.cancel();
				this.terminate();
			}
		});
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (tick <= duration) {
					sendBroadcast(new TickBroadcast(tick++, duration));
				}
				else {
					this.cancel();
				}
			}
		};
		timer.scheduleAtFixedRate(task,0, speed);
		
	}

}
