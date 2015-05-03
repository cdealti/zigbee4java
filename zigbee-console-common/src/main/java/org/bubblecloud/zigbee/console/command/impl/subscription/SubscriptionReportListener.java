package org.bubblecloud.zigbee.console.command.impl.subscription;

import org.bubblecloud.zigbee.api.cluster.impl.api.core.Attribute;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ReportListener;
import org.bubblecloud.zigbee.console.ZigBeeConsole;

import java.lang.ref.WeakReference;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.WeakHashMap;


/**
 * Report listener implementation which prints the reports to console.
 */
public class SubscriptionReportListener implements ReportListener {

    private static WeakHashMap<ZigBeeConsole, SubscriptionReportListener> reportListeners = new WeakHashMap<>();

    public static SubscriptionReportListener getListener(ZigBeeConsole console) {
        SubscriptionReportListener listener = reportListeners.get(console);
        if(listener==null){
            listener = new SubscriptionReportListener(console);
            reportListeners.put(console,listener);
        }
        return listener;
    }

    private final WeakReference<ZigBeeConsole> consoleRef;

    private SubscriptionReportListener(ZigBeeConsole console) {
        consoleRef = new WeakReference<>(console);
    }

    @Override
    public void receivedReport(final String endPointId, final short clusterId, final Dictionary<Attribute, Object> reports) {
        final Enumeration<Attribute> attributes = reports.keys();
        while (attributes.hasMoreElements()) {
            final Attribute attribute = attributes.nextElement();
            final Object value = reports.get(attribute);

            ZigBeeConsole console = consoleRef.get();

            if(console!=null) {
                console.print(endPointId + "->" + clusterId + "->" + attribute.getName() + "=" + value);
            }
        }
    }
};