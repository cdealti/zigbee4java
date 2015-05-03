package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.api.cluster.Cluster;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.Attribute;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZigBeeClusterException;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;


/**
 * Reads an attribute from a device.
 */
public class ReadCommand extends AbstractCommand {

    public ReadCommand() {
        super("read", "Read an attribute.", "[DEVICE]", "[CLUSTER]", "[ATTRIBUTE]");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 4) {
            return false;
        }

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

        final Device device = console.getDeviceByIndexOrEndpointId(api, args[1]);
        if (device == null) {
            console.print("Device not found.");
            return false;
        }

        final Cluster cluster = device.getCluster(clusterId);
        if (cluster == null) {
            console.print("Cluster not found.");
            return false;
        }

        final Attribute attribute = cluster.getAttribute(attributeId);
        if (attribute == null) {
            console.print("Attribute not found.");
            return false;
        }

        try {
            console.print(attribute.getName() + "=" + attribute.getValue());
        } catch (ZigBeeClusterException e) {
            console.print("Failed to read attribute.");
            e.printStackTrace();
        }

        return true;
    }
}