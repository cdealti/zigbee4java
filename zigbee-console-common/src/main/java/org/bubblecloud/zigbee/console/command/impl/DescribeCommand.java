package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.api.ZigBeeApiConstants;
import org.bubblecloud.zigbee.api.cluster.Cluster;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.Attribute;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;


/**
 * Prints device information to console.
 */
public class DescribeCommand extends AbstractCommand {

    public DescribeCommand() {
        super("desc", "Describes a device.", "DEVICEID");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 2) {
            return false;
        }

        final Device device = console.getDeviceByIndexOrEndpointId(api, args[1]);

        if (device == null) {
            return false;
        }

        console.print("Network Address  : " + device.getNetworkAddress());
        console.print("Extended Address : " + device.getIeeeAddress());
        console.print("Endpoint Address : " + device.getEndPointAddress());
        console.print("Device Type      : " + device.getDeviceType());
        console.print("Device Category  : " + ZigBeeApiConstants.getCategoryDeviceName(device.getDeviceTypeId()));
        console.print("Device Version   : " + device.getDeviceVersion());
        console.print("Input Clusters   : ");
        for (int c : device.getInputClusters()) {
            final Cluster cluster = device.getCluster(c);
            console.print("                 : " + c + " " + ZigBeeApiConstants.getClusterName(c));
            if (cluster != null) {
                for (int a = 0; a < cluster.getAttributes().length; a++) {
                    final Attribute attribute = cluster.getAttributes()[a];
                    console.print("                 :    " + attribute.getId()
                                  + " "
                                  + "r"
                                  + (attribute.isWritable() ? "w" : "-")
                                  + (attribute.isReportable() ? "s" : "-")
                                  + " "
                                  + attribute.getName()
                                  + " "
                                  + (attribute.getReporter() != null ? "(" +
                                                                       Integer.toString(attribute.getReporter().getReportListenersCount()) + ")" : "")
                                  + "  [" + attribute.getZigBeeType() + "]");
                }
            }
        }
        console.print("Output Clusters  : ");
        for (int c : device.getOutputClusters()) {
            final Cluster cluster = device.getCluster(c);
            console.print("                 : " + c + " " + ZigBeeApiConstants.getClusterName(c));
        }

        return true;
    }
}