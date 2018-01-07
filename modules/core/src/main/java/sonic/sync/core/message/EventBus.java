package sonic.sync.core.message;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import sonic.sync.core.event.AbstractEvent;

public class EventBus extends MBassador<AbstractEvent> {

	public EventBus() {
		super(createBusConfiguration());
	}

	private static BusConfiguration createBusConfiguration() {
		BusConfiguration config = new BusConfiguration();
		// synchronous dispatching of events
		config.addFeature(Feature.SyncPubSub.Default());
		// asynchronous dispatching of events
		config.addFeature(Feature.AsynchronousHandlerInvocation.Default());
		config.addFeature(Feature.AsynchronousMessageDispatch.Default());
		return config;
	}

}
