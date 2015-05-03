package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.Reporter;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;
import org.bubblecloud.zigbee.console.command.impl.subscription.SubscriptionReportListener;


/**
 * Unlisten from reports of given attribute.
 */
public class UnlistenCommand extends AbstractCommand {

    public UnlistenCommand() {
        super("unlisten", "Unlisten from attribute reports.", "[DEVICE]","[CLUSTER]","[ATTRIBUTE]");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 4) {
            return false;
        }

        final Device device = console.getDeviceByIndexOrEndpointId(api, args[1]);
        final int clusterId;
        try {
            clusterId = Integer.parseInt(args[2]);
        } catch (final NumberFormatException e) {
            return false;
        }
        final int attributeId;
        try {
            attributeId = Integer.parseInt(args[3]);
        } catch (final NumberFormatException e) {
            return false;
        }

        final Reporter reporter = device.getCluster(clusterId).getAttribute(attributeId).getReporter();

        if (reporter == null) {
            console.print("Attribute does not provide reports.");
        } else {
            SubscriptionReportListener listener = SubscriptionReportListener.getListener(console);
            reporter.removeReportListener(listener, false);
        }

        return true;
    }
}