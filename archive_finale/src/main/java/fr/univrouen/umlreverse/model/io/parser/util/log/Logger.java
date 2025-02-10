package fr.univrouen.umlreverse.model.io.parser.util.log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A log entry list.
 */
public class Logger {
    private final LinkedList<ALogEntry> entries;

    public Logger() {
        this.entries = new LinkedList<>();
    }

    public void add(ALogEntry entry) {
        entries.add(entry);
    }

    /**
     * Create and adds an info entry.
     * @param message the info message.
     */
    public void addInfo(String message) {
        add(new InfoLogEntry(message));
    }

    /**
     * Create and adds a warning entry.
     * @param message the warning message.
     */
    public void addWarning(String message) {
        add(new WarningLogEntry(message));
    }

    /**
     * Create and adds an error entry.
     * @param message the error message.
     */
    public void addError(String message) {
        add(new ErrorLogEntry(message));
    }

    /**
     * Return an unmodifiablelist containing all the entries.
     */
    public List<ALogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ALogEntry entry : entries) {
            sb.append(entry.toString()).append('\n');
        }
        return sb.toString();
    }
}
