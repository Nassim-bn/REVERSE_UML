package fr.univrouen.umlreverse.model.io.parser;

import fr.univrouen.umlreverse.model.io.parser.plantuml.*;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * A PlantUML loader to extract a diagram from a source file.
 */
public class PlantUMLLoader {
    private static final String FILE_LOAD_ERR = "Erreur lors du chargement du fichier : ";
    private static final String LOAD_DIAG_MSG = "Lecture du fichier : ";
    private final IProject project;
    private final String diagramName;
    private PlantUMLDiagramVisitor visitor;

    /**
     * @param project the model's project.
     * @param diagramName the output diagram name.
     * @param file the file containing the diagram.
     */
    public PlantUMLLoader(IProject project, String diagramName, File file) throws RefusedAction {
        this.project = project;
        this.diagramName = diagramName;
        load(file);
    }

    /**
     * @return the log object.
     */
    public Logger getLog() {
        return visitor.getLogger();
    }

    private void load(File file) {
        Logger logger = new Logger();
        logger.addInfo(LOAD_DIAG_MSG + file.getName());
        try {
            Reader reader = new FileReader(file);
            ANTLRInputStream stream = new ANTLRInputStream(reader);
            PlantUMLLexer lexer = new PlantUMLLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            PlantUMLParser parser = new PlantUMLParser(tokens);
            PlantUMLParser.EntryPointContext ctx = parser.entryPoint();
            visitor = new PlantUMLDiagramVisitor(project, diagramName, logger);
            visitor.visitEntryPoint(ctx);
        } catch (IOException e) {
            logger.addError(FILE_LOAD_ERR + file.getName());
        }
    }
}
