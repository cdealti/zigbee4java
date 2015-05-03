package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;
import org.bubblecloud.zigbee.network.impl.ZigBeeNetworkManagerException;


/**
 * Unbinds device from another device with given cluster ID.
 */
public class UnbindCommand extends AbstractCommand {

    public UnbindCommand() {
        super("unbind", "Unbinds a device from another device.", "CLIENT", "SERVER", "CLUSTERID");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 3 && args.length != 4) {
            return false;
        }

        if (args.length == 3) {
            Device server = console.getDeviceByIndexOrEndpointId(api, args[1]);
            final int clusterId;
            try {
                clusterId = Integer.parseInt(args[2]);
            } catch (final NumberFormatException e) {
                return false;
            }
            try {
                server.unbindFromLocal(clusterId);
            } catch (final ZigBeeNetworkManagerException e) {
                e.printStackTrace();
            }
        } else {
            Device client = console.getDeviceByIndexOrEndpointId(api, args[1]);
            Device server = console.getDeviceByIndexOrEndpointId(api, args[2]);
            final int clusterId;
            try {
                clusterId = Integer.parseInt(args[3]);
            } catch (final NumberFormatException e) {
                return false;
            }
            try {
                client.unbindFrom(server, clusterId);
            } catch (final ZigBeeNetworkManagerException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}