package fr.univrouen.umlreverse.model.io.parser.util.log;

import javafx.scene.paint.Color;

import java.time.LocalDateTime;

/**
 * Superclass for a log entry. Extend this to get new types of entries.
 */
public abstract class ALogEntry {
    private String text;

    protected ALogEntry(String text) {
        this.text = "[" + LocalDateTime.now().toLocalTime().toString() + "] " + text;
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * @return the color of this entry.
     */
    public abstract Color getColor();
}
