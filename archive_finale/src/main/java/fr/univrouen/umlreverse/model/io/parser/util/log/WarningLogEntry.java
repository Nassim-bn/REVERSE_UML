package fr.univrouen.umlreverse.model.io.parser.util.log;

import javafx.scene.paint.Color;

/**
 * An warning log entry, strating with "WARNING : ". Is orange.
 */
public class WarningLogEntry extends ALogEntry {
    public WarningLogEntry(String text) {
        super("WARNING : " + text);
    }

    public Color getColor() {
        return Color.ORANGE;
    }
}
