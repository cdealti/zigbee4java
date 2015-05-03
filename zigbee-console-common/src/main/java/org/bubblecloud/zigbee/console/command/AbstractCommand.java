package org.bubblecloud.zigbee.console.command;

import org.bubblecloud.zigbee.console.ZigBeeConsole;

import java.lang.ref.WeakReference;


/**
 * Interface for console commands.
 */
public abstract class AbstractCommand implements ConsoleCommand {

    private final String name, description;
    private final String[] argNames;

    protected AbstractCommand(String name, String description, String ... argNames) {
        this.name        = name;
        this.description = description;
        this.argNames    = argNames;
    }

    /**
     * {@inheritDoc}
     */
    public final String getName()        { return name; }

    /**
     * {@inheritDoc}
     */
    public final String getDescription() { return description; }

    /**
     * {@inheritDoc}
     */
    public final String getSyntax() {
        StringBuilder syntaxBuilder = new StringBuilder();
        syntaxBuilder.append(name);
        for(String argName : argNames) {
            syntaxBuilder.append(" ");
            syntaxBuilder.append(argName);
        }
        return syntaxBuilder.toString();
    }
}