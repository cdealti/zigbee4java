package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.api.ZigBeeDeviceException;
import org.bubblecloud.zigbee.api.cluster.general.ColorControl;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;
import org.bubblecloud.zigbee.util.Cie;


/**
 * Changes a light color on device.
 */
public class ColorCommand extends AbstractCommand
{
    public ColorCommand() {
        super("color", "Changes light color.", "DEVICEID", "RED", "GREEN", "BLUE");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 5) {
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
        // @param colorX x * 65536 where colorX can be in rance 0 to 65279
        // @param colorY y * 65536 where colorY can be in rance 0 to 65279

        float red;
        try {
            red = Float.parseFloat(args[2]);
        } catch (final NumberFormatException e) {
            return false;
        }
        float green;
        try {
            green = Float.parseFloat(args[3]);
        } catch (final NumberFormatException e) {
            return false;
        }
        float blue;
        try {
            blue = Float.parseFloat(args[4]);
        } catch (final NumberFormatException e) {
            return false;
        }

        try {
                /*
                // RED
                int x = (int) (0.648427f * 65536);
                int y = (int) (0.330856f * 65536);
                colorControl.moveToColor(x, y, 10);*/
            Cie cie = Cie.rgb2cie(red, green ,blue);
            int x = (int) (cie.x * 65536);
            int y = (int) (cie.y * 65536);
            if (x > 65279) {
                x = 65279;
            }
            if (y > 65279) {
                y = 65279;
            }
            colorControl.moveToColor(x, y, 10);
        } catch (ZigBeeDeviceException e) {
            e.printStackTrace();
        }

        return true;
    }
}