package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.api.Device;
import org.bubblecloud.zigbee.api.cluster.Cluster;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.Attribute;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZigBeeClusterException;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;


/**
 * Writes an attribute to a device.
 */
public class WriteCommand extends AbstractCommand {

    public WriteCommand() {
        super("write", "Write an attribute.", "[DEVICE]", "[CLUSTER]", "[ATTRIBUTE]", "[VALUE]");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 5) {
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

        if(attribute.isWritable() == false) {
            console.print(attribute.getName() + " is not writable");
            return true;
        }

        try {
            Object val = null;
            //TODO Handle other value types.
            switch(attribute.getZigBeeType()) {
                case Bitmap16bit:
                    break;
                case Bitmap24bit:
                    break;
                case Bitmap32bit:
                    break;
                case Bitmap8bit:
                    break;
                case Boolean:
                    break;
                case CharacterString:
                    val = new String(args[4]);
                    break;
                case Data16bit:
                    break;
                case Data24bit:
                    break;
                case Data32bit:
                    break;
                case Data8bit:
                    break;
                case DoublePrecision:
                    break;
                case Enumeration16bit:
                    break;
                case Enumeration8bit:
                    break;
                case IEEEAddress:
                    break;
                case LongCharacterString:
                    break;
                case LongOctectString:
                    break;
                case OctectString:
                    break;
                case SemiPrecision:
                    break;
                case SignedInteger16bit:
                    break;
                case SignedInteger24bit:
                    break;
                case SignedInteger32bit:
                    break;
                case SignedInteger8bit:
                    break;
                case SinglePrecision:
                    break;
                case UnsignedInteger16bit:
                    break;
                case UnsignedInteger24bit:
                    break;
                case UnsignedInteger32bit:
                    break;
                case UnsignedInteger8bit:
                    break;
                default:
                    break;
            }
            attribute.setValue(val);
        } catch (ZigBeeClusterException e) {
            console.print("Failed to write attribute.");
            e.printStackTrace();
        }

        return true;
    }
}
