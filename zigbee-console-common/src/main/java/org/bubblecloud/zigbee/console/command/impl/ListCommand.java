package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;

import java.util.List;


/**
 * Prints list of devices to console.
 */
public class ListCommand extends AbstractCommand {

    public ListCommand() {
        super("list", "Lists devices.");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        final List<Device> devices = api.getDevices();
        for (int i = 0; i < devices.size(); i++) {
            final Device device = devices.get(i);
            console.print(i + ") " + device.getEndpointId() +
                               " [" + device.getNetworkAddress() + "]" +
                               " : " + device.getDeviceType() + "\n");
        }
        return true;
    }
}