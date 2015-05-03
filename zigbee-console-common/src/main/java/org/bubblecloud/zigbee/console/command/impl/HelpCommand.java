package org.bubblecloud.zigbee.console.command.impl;


import org.bubblecloud.zigbee.console.ZigBeeConsole;
import org.bubblecloud.zigbee.console.command.AbstractCommand;
import org.bubblecloud.zigbee.console.command.ConsoleCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Prints help on console.
 */
public class HelpCommand extends AbstractCommand
{
    public HelpCommand() {
        super("help", "View command help.", "[command]");
    }

    /**
     * {@inheritDoc}
     */
    public boolean process(final ZigBeeConsole console, final String[] args) {

        String commandName = args[1];
        ConsoleCommand command = console.getCommandByName(commandName);

        if (args.length == 2) {
            if (command!=null) {
                console.print(command.getDescription()+"\n");
                console.print("\n");
                console.print("Syntax: " + command.getSyntax() + "\n");
            } else {
                return false;
            }
        } else if (args.length == 1) {
            final List<ConsoleCommand> commandList = new ArrayList<>(console.getCommands());

            Comparator<ConsoleCommand> consoleNameComparator = new Comparator<ConsoleCommand>()
            {
                @Override
                public int compare(final ConsoleCommand o1, final ConsoleCommand o2)
                {
                    return o1.getName().compareTo(o2.getName());
                }
            };

            Collections.sort(commandList, consoleNameComparator);
            console.print("Commands:");
            for (final ConsoleCommand listCommand : commandList) {
                console.print(listCommand.getName() + " - " + command.getDescription());
            }
        } else {
            return false;
        }

        return true;
    }
}