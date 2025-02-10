package fr.univrouen.umlreverse.model.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of the error code that the model can generate.
 */
public enum ErrorAbstraction {
    UndefinedProblem("0x0000", "Erreur indéfinie."),
    ConflictNameEntitiesInPackage("0x0001", "Le package contient déjà une entité portant ce nom."),
    ConflictAttributeInEntity("0x0002", "L’entité possède déjà un attribut portant ce nom."),
    ConflictSignatureMethodInEntity("0x0003", "L’entité contient déjà une méthode avec cette signature."),
    ConflictOfNameInProject("0x0004", "Le projet contient déjà un diagramme portant ce nom."),
    ConflictNamePackageInPackage("0x0005", "Le projet contient déjà un package portant ce nom."),
    ConflictArgumentInMethod("0x0006", "Deux arguments de la méthode possèdent le même type et le même nom."),
    ConflictNoteIdInDiagram("0x0007", "Deux notes possèdent le même ID."),
    ErrorImpossibleToCreateFile("0x0008", "Erreur durant la création du fichier."),
    ErrorImpossibleToWriteInFile("0x0009", "Erreur durant l’écriture du fichier."),
    ErrorImpossibleToCloseTheFile("0x0010", "Erreur durant la fermeture du fichier."),
    ErrorEntityAlreadyInDiagram("0x0011", "L’entité est déjà dans le diagramme."),
	ErrorObjectAlreadyInDiagram("0x0011", "Ce nom d'objet est déjà utilisé.");
    /**
     * The code error.
     */
    private String code;
    /**
     * The explain.
     */
    private String explain;

    /**
     * The link between the code and the error.
     */
    private static Map<String, ErrorAbstraction> errorFromCode = new HashMap<>();
    //initialization
    static {
        for (ErrorAbstraction error : ErrorAbstraction.values()) {
            errorFromCode.put(error.getCode(), error);
        }
    }

    /**
     * An ErrorAbstraction
     */
    ErrorAbstraction(String code, String explain) {
        this.code = code;
        this.explain = explain;
    }

    /**
     * The code.
     */
    public String getCode() {
        return code;
    }

    /**
     * The explain.
     */
    public String getExplain() {
        return explain;
    }

    /**
     * The error representing by code.
     */
    public static ErrorAbstraction getErrorFromCode(String code) {
        ErrorAbstraction error = errorFromCode.get(code);
        if (error == null) {
            error = UndefinedProblem;
        }
        return error;
    }
}
