package org.bubblecloud.zigbee.console.command.impl;

import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.api.ZigBeeDeviceException;
import org.bubblecloud.zigbee.api.cluster.general.OnOff;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;


/**
 * Switches a device off.
 */
public class OffCommand extends AbstractCommand {

    public OffCommand() {
        super("off", "Switches device off.", "DEVICEID");
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
        final OnOff onOff = device.getCluster(OnOff.class);
        try {
            onOff.off();
        } catch (ZigBeeDeviceException e) {
            e.printStackTrace();
        }

        return true;
    }
}