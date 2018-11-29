package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

    private static final MessageBusImpl MESSAGE_BUS = new MessageBusImpl();

	private ConcurrentHashMap< MicroService, LinkedBlockingQueue<Message>> messagesQueues;
	private ConcurrentHashMap< Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> eventsToSubscribers;
	private ConcurrentHashMap<MicroService,ConcurrentLinkedQueue<Class<? extends Message>>> subscribersToEvents;
	private ConcurrentHashMap< Class<? extends Message>, Future> futures;


    private MessageBusImpl() {
        messagesQueues = new ConcurrentHashMap<>();
        eventsToSubscribers = new ConcurrentHashMap<>();
        futures = new ConcurrentHashMap<>();
        subscribersToEvents = new ConcurrentHashMap<>();
    }

    public static MessageBusImpl getInstance() {
        return MESSAGE_BUS;
    }


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		subscribe(type, m);

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        subscribe(type, m);

	}

	private void subscribe(Class<? extends Message> type, MicroService m) {
        if (!eventsToSubscribers.get(type).contains(m))
            eventsToSubscribers.get(type).add(m);
        if (!subscribersToEvents.get(m).contains(type))
            subscribersToEvents.get(m).add(type);
    }

	@Override
	public <T> void complete(Event<T> e, T result) {
		futures.get(e).resolve(result);
		futures.remove(e);

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (MicroService m : eventsToSubscribers.get(b)) {
            messagesQueues.get(m).add(b);
		}

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = new Future<>();
		MicroService microService;
		synchronized (eventsToSubscribers.get(e)) {
			microService = eventsToSubscribers.get(e).poll();
			eventsToSubscribers.get(e).add(microService);
		}
		if (microService != null)
            messagesQueues.get(microService).add(e);
		return future;
	}

	@Override
	public void register(MicroService m) {
        messagesQueues.put(m, new LinkedBlockingQueue<>());

	}

	@Override
	public void unregister(MicroService m) {
		for (Class<? extends Message> mes : subscribersToEvents.get(m)) {
            eventsToSubscribers.get(mes).remove(m);
		}
        messagesQueues.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
        Message message = null;
	    try {
		    message = messagesQueues.get(m).take();
        } catch (InterruptedException e) {}
		return message;
	}

	

}
