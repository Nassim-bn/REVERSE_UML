package fr.univrouen.umlreverse.model.io.parser.util.log;

import javafx.scene.paint.Color;

/**
 * An error log entry, strating with "ERROR : ". Is red.
 */
public class ErrorLogEntry extends ALogEntry {
    public ErrorLogEntry(String text) {
        super("ERROR : " + text);
    }

    public Color getColor() {
        return Color.RED;
    }
}
