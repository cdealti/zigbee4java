package org.bubblecloud.zigbee.util;

import java.util.Observable;


/**
 * Created by Chris on 02/05/15.
 */
public class ObservableStatus<TS extends Enum<TS>> extends Observable
{
    private TS status;

    public TS get() {
        return status;
    }

    public void set(TS status) {
        this.status = status;
        notifyObservers();
    }
}
