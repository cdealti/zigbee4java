package org.bubblecloud.zigbee.util;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Wraps an Enumerated State, allowing it to be Observed for changes.
 * Provides methods which block a calling thread until a change to specified state(s) has occurred.
 * Created by Chris on 02/05/15.
 */
public final class ObservableState<TS extends Enum<TS>> extends Observable {

    public interface StateChangeHandler<TS> {
        void handleStatusChange(TS newStatus);
    }

    private final Object monitor = new Object();

    private TS status;

    public ObservableState() {}

    public ObservableState(TS status) {
        this.status = status;
    }

    public void addObserver(final StateChangeHandler<TS> observer) {
        final Observer observerWrapper = new Observer() {
            @Override
            @SuppressWarnings("unchecked")
            public void update(final Observable o, final Object statusObj) {
                TS status = (TS)statusObj;
                observer.handleStatusChange(status);
            }
        };
        addObserver(observerWrapper);
    }

    public TS get() {
        return status;
    }

    public boolean is(TS status) {
        return this.status == status;
    }

    public boolean isAnyOf(TS... statuses) {
        boolean isAny = false;
        for(TS status : statuses) {
            if( this.status == status ) {
                isAny = true;
                break;
            }
        }
        return isAny;
    }

    public void set(TS status) {
        if(this.status!=status) {
            this.status = status;
            this.setChanged();
        }
        notifyObservers(status);
    }

    public int ordinal() {
        return status.ordinal();
    }

    @SuppressWarnings("unchecked")
    public TS waitFor(TS status) {
        return waitForAnyOf(status);
    }

    public TS waitForAnyOf(TS ... statuses) {

        Observer statusObserver = null;

        int foundStatusIndex;
        while( ( foundStatusIndex = Arrays.binarySearch(statuses,status) ) < 0) {

            if( statusObserver == null ) {
                statusObserver = new Observer() {
                    @Override
                    public void update(final Observable o, final Object driverStatusObj) {
                        synchronized(monitor) {
                            monitor.notifyAll();
                        }
                    }
                };
            }

            synchronized(monitor) {
                try                           { monitor.wait(); }
                catch(InterruptedException e) { throw new RuntimeException(e); }
            }
        }

        return statuses[ foundStatusIndex ];
    }
}
