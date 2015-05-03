package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.ZigBeeApi;
import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;


public class JoinCommand extends AbstractCommand
{
    public JoinCommand() {
        super("join", "Enable or diable network join.", "[enable|disable]");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        ZigBeeApi api = console.getZigBeeApi();

        if (args.length != 2) {
            return false;
        }

        boolean join = false;
        if(args[1].toLowerCase().startsWith("e")) {
            join = true;
        }

        if (!api.permitJoin(join)) {
            if (join) {
                console.print("ZigBee API permit join enable ... [FAIL]");
            } else {
                console.print("ZigBee API permit join disable ... [FAIL]");
            }
        } else {
            if (join) {
                console.print("ZigBee API permit join enable ... [OK]");
            } else {
                console.print("ZigBee API permit join disable ... [OK]");
            }
        }
        return true;
    }
}