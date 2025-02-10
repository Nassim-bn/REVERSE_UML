package fr.univrouen.umlreverse.model.io.parser;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.io.parser.java.Java8ClassVisitor;
import fr.univrouen.umlreverse.model.io.parser.java.Java8FieldsVisitor;
import fr.univrouen.umlreverse.model.io.parser.java.Java8Lexer;
import fr.univrouen.umlreverse.model.io.parser.java.Java8Parser;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Java loader to parse entities and create a new Diagram.
 */
public class Java8Loader {
    private static final String FILE_LOAD_ERR = "Erreur lors du chargement du fichier : ";
    private static final String LOAD_CLASS_MSG = "Parsing des classes...";
    private static final String LOAD_FIELDS_MSG = "Parsing des champs...";
    private static final String ADD_MODEL_MESSAGE = "Ajout dans le modèle...";
    private final ParseClassDiagram diagram;
    private final Logger logger;

    /**
     * Loads a folder.
     * @param project the model's project.
     * @param diagramName the output diagram name.
     * @param folder the folder in which files need to be parsed (recursive).
     */
    public Java8Loader(IProject project, String diagramName, File folder) throws RefusedAction {
        IClassDiagram model = new ClassDiagram(project, diagramName);
        logger = new Logger();
        diagram = new ParseClassDiagram(model, logger);
        load(folder);
    }

    /**
     * Loads a set of files.
     * @param project the model's project.
     * @param diagramName the output diagram name.
     * @param files the files to parse.
     */
    public Java8Loader(IProject project, String diagramName, List<File> files) throws RefusedAction {
        IClassDiagram model = new ClassDiagram(project, diagramName);
        logger = new Logger();
        diagram = new ParseClassDiagram(model, logger);
        load(files);
    }

    /**
     * @return the log object.
     */
    public Logger getLog() {
        return logger;
    }

    private void load(File folder) {
        getLog().addInfo(LOAD_CLASS_MSG);
        loadClass(folder);
        getLog().addInfo(LOAD_FIELDS_MSG);
        loadFields(folder);
        getLog().addInfo(ADD_MODEL_MESSAGE);
        diagram.addIntoModel();
    }
    
    private void load(List<File> files) {
        getLog().addInfo(LOAD_CLASS_MSG);
        loadClass(files);
        getLog().addInfo(LOAD_FIELDS_MSG);
        loadFields(files);
        getLog().addInfo(ADD_MODEL_MESSAGE);
        diagram.addIntoModel();
    }

    private void loadClass(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File aChildren : children) {
                    loadClass(aChildren);
                }
            }
        } else if (file.isFile()) {
            try {
                Reader reader = new FileReader(file);
                ANTLRInputStream stream = new ANTLRInputStream(reader);
                Java8Lexer lexer = new Java8Lexer(stream);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                Java8Parser parser = new Java8Parser(tokens);
                Java8Parser.EntryPointContext ctx = parser.entryPoint();
                Java8ClassVisitor classLoader = new Java8ClassVisitor(diagram);
                classLoader.visitEntryPoint(ctx);
                System.gc();
            } catch (IOException e) {
                getLog().addError(FILE_LOAD_ERR + file.getName());
            }
        }
    }
    
    private void loadClass(List<File> files) {
        files.forEach(this::loadClass);
    }

    private void loadFields(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File aChildren : children) {
                    loadFields(aChildren);
                }
            }
        } else if (file.isFile()) {
            try {
                Reader reader = new FileReader(file);
                ANTLRInputStream stream = new ANTLRInputStream(reader);
                Java8Lexer lexer = new Java8Lexer(stream);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                Java8Parser parser = new Java8Parser(tokens);
                Java8Parser.EntryPointContext ctx = null;
                ctx = parser.entryPoint();
                Java8FieldsVisitor fieldsLoader = new Java8FieldsVisitor(diagram);
                fieldsLoader.visitEntryPoint(ctx);
                System.gc();
            } catch (IOException | NullPointerException e) {
                getLog().addError(FILE_LOAD_ERR + file.getName());
            }
        }
    }
    
    private void loadFields(List<File> files) {
        files.forEach(this::loadFields);
    }
}
