package fr.univrouen.umlreverse.model.io.parser.util.log;

import javafx.scene.paint.Color;

/**
 * An info log entry, strating with "INFO : ". Is green.
 */
public class InfoLogEntry extends ALogEntry {
    public InfoLogEntry(String text) {
        super("INFO : " + text);
    }

    public Color getColor() {
        return Color.GREEN;
    }
}
