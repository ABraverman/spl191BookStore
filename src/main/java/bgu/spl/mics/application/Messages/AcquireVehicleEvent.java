package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class AcquireVehicleEvent implements Event<Future<DeliveryVehicle>> {

    public AcquireVehicleEvent(){}
}
