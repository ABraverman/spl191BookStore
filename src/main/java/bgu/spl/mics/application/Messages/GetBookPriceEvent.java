package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.*;

public class GetBookPriceEvent implements Event<Integer> {

    private String book;

    public GetBookPriceEvent (String book) {
        this.book = book;
    }

}
