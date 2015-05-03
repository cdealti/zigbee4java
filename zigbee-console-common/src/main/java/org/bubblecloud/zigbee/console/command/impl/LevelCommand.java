package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.api.ZigBeeDeviceException;
import org.bubblecloud.zigbee.api.cluster.general.ColorControl;
import org.bubblecloud.zigbee.api.cluster.general.LevelControl;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;


/**
 * Changes a device level for example lamp brightness.
 */
public class LevelCommand extends AbstractCommand
{
    public LevelCommand() {
        super("level", "Changes device level for example lamp brightness.", "DEVICEID", "LEVEL");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 3) {
            return false;
        }

        final Device device = console.getDeviceByIndexOrEndpointId(api, args[1]);
        if (device == null) {
            return false;
        }
        final ColorControl colorControl = device.getCluster(ColorControl.class);
        if (colorControl == null) {
            console.print("Device does not support color control.");
            return false;
        }

        float level;
        try {
            level = Float.parseFloat(args[2]);
        } catch (final NumberFormatException e) {
            return false;
        }

        try {
            int l = (int) (level * 254);
            if (l > 254) {
                l = 254;
            }
            if (l < 0) {
                l = 0;
            }

            final LevelControl levelControl = device.getCluster(LevelControl.class);
            levelControl.moveToLevel((short) l, 10);
        } catch (ZigBeeDeviceException e) {
            e.printStackTrace();
        }

        return true;
    }
}
