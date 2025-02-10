grammar PlantUML;

// -- PARSER RULES --
// ENTRY POINT
entryPoint
    : NL? START_UML NL classDiagram END_UML NL?
    | NL? START_UML NL usecaseDiagram END_UML NL?
    ;

// -- USECASE DIAGRAM --
// Usecase diagram document structure
usecaseDiagram
    : usecaseDiagramContent*
    ;

// Usecase diagram contents
usecaseDiagramContent
    : usecaseEntityDef
    | usecaseArrow
    | commonContent
    ;

// Entities definitions
usecaseEntityDef
    : usecaseActorDef
    | usecaseCaseDef
    | usecaseRectangleDef
    ;

usecaseActorDef
    : WS? 'actor' WS name=usecaseEntityID (WS? stereotype)?
        (WS AS WS label=usecaseEntityID (WS? stereotype)?)? (WS? color)? WS? NL
    | WS? name=usecaseEntityID (WS? stereotype)?
        (WS AS WS label=usecaseEntityID (WS? stereotype)?)? (WS? color)? WS? NL
    ;
    
usecaseCaseDef
    : WS? 'usecase' WS name=usecaseEntityID (WS? stereotype)?
        (WS AS WS label=usecaseEntityID (WS? stereotype)?)? (WS? color)? WS? NL
    | WS? name=usecaseEntityID (WS? stereotype)?
        (WS AS WS label=usecaseEntityID (WS? stereotype)?)? (WS? color)? WS? NL
    ;

usecaseRectangleDef
    : WS? 'rectangle' WS name=usecaseEntityID (WS? stereotype)?
        (WS AS WS label=usecaseEntityID (WS? stereotype)?)? (WS? color)? WS?
        ('{' WS? NL usecaseDiagramContent* WS? '}' WS?)? NL
    ;

// Arrows
usecaseArrow
    : WS? headID=usecaseArrowID WS? (headLabel=escapedString WS?)? usecaseArrowType
        WS? (tailLabel=escapedString WS?)? tailID=usecaseArrowID WS? (':' bodyLabel=endLine)? NL
    ;

usecaseArrowType
    : arrowHead? arrowBody arrowTail?
    ;

usecaseArrowID
    : ':' (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>' | '.' |
        '"' | '-' | '*' | escapedKeywords | WS)+ ':'
    | '(' (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | '<' | '>' | '.' | '"' |
        ':' | '-' | '*' | escapedKeywords | WS)+ ')'
    | ID
    ;

// Usecase entities ID
usecaseEntityID
    : usecaseActorID
    | usecaseCaseID
    | escapedString
    | ID
    ;

usecaseActorID
    : ':' (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>' | '.' |
        '"' | '-' | '*' | escapedKeywords | WS)+ ':'
    ;
    
usecaseCaseID
    : '(' (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '<' | '>' | '.' |
              '"' | ':' | '-' | '*' | escapedKeywords | WS)+ ')'
    ;

// -- CLASS DIAGRAM --
// Class diagram document structure
classDiagram
    : classDiagramContent*
    ;

// Class diagram contents
classDiagramContent
    : classPackageEntity
    | classEntity
    | classRelation
    | classExternalField
    | commonContent
    ;

// Packages & Namespaces
classPackageEntity
    : WS? classPackageEntityType WS classPackageName (WS? stereotype)? WS? (color WS?)? '{' WS? NL classDiagramContent* WS? '}' WS? NL
    ;

classPackageEntityType
    : PACKAGE
    | NAMESPACE
    ;
    
classPackageName
    : (classPackageID '.')* classPackageID
    | escapedString (WS AS WS (classPackageID '.')* classPackageID)?
    ;

// Entities (class, enum, interface, abstract)
classEntity
    : WS? classEntityType WS
        (escapedString WS AS WS classEntityID generic?
        | classEntityID generic? (WS AS WS escapedString)?)
        WS? (stereotype WS?)? (classInheritance WS?)? (color WS?)?
        ('{' NL (classField | WS? NL)* WS? '}' WS?)? NL
    ;

classEntityType
    : CLASS
    | ABSTRACT
    | INTERFACE
    | ENUM
    ;

classInheritance
    : WS classInheritanceType WS classEntityID
    ;

classInheritanceType
    : EXTENDS
    | IMPLEMENTS
    ;

// Fields (method & attribute)
// External field
classExternalField
    : WS? classEntityID WS ':' classField
    ;

// Inner field
classField
    : classMethod
    | classAttribute
    | comment
    ;

classMethod
    : WS? classVisibility classFieldContent? (('(' | ')') classFieldContent?)+ WS? NL
    | WS? classFieldNoVisibilityContent? (('(' | ')') classFieldContent?)+ WS? NL
    ;

classAttribute
    : WS? classVisibility classFieldContent WS* NL
    | WS? classFieldNoVisibilityContent WS* NL
    ;

classVisibility
    : '-'
    | '#'
    | '~'
    | '+'
    ;

classFieldNoVisibilityContent
    : (ID | '{' | '<' | '>' | '.' | '"' | ':' | '*' | escapedKeywords)
         (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '<' | '>' | '.' | '"' | ':' | '-' | '*' |
         escapedKeywords | WS)*
    ;

classFieldContent
    : WS? (ID | '~' | '{' | '<' | '>' | '.' | '"' | ':' | '-' | '*' | '#' | '+' | escapedKeywords)
        (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '<' | '>' | '.' | '"' | ':' | '-' | '*' |
        escapedKeywords | WS)*
    ;

// Relations
classRelation
    : WS? headID=classRelationClass WS? (headLabel=escapedString WS?)?
        (classRelationType | arrowBody)
        (WS? tailLabel=escapedString)? WS? (tailType=classEntityType WS)? tailID=classRelationClass
        WS? (':' bodyLabel=endLine)? NL
    | WS? headType=classEntityType WS headID=classRelationClass WS? (headLabel=escapedString WS?)?
        classRelationType
        (WS? tailLabel=escapedString)? WS? (tailType=classEntityType WS)? tailID=classRelationClass
        WS? (':' bodyLabel=endLine)? NL
    | WS? headType=classEntityType WS headID=classRelationClass (WS? headLabel=escapedString)?
        ((WS arrowBody) | (arrowBody WS) | (WS arrowBody WS))
        (tailLabel=escapedString WS?)? (tailType=classEntityType WS)? tailID=classRelationClass WS?
        (':' bodyLabel=endLine)? NL
    | classAssociationClass
    ;

classAssociationClass
    :  WS? '(' WS? firstID=classRelationClass WS? ',' WS? sndID=classRelationClass WS? ')' WS?
        (headLabel=escapedString WS?)?
        arrowHead? arrowBody arrowTail?
        (WS? tailLabel=escapedString)? WS? (tailType=classEntityType WS)? tailID=classRelationClass
        WS? (':' bodyLabel=endLine)? NL
    ;

classRelationClass
    : escapedString
    | classEntityID
    ;

classRelationType
    : arrowHead arrowBody
    | arrowBody arrowTail
    | arrowHead arrowBody arrowTail
    | classLollipopHead
    | classLollipopTail
    ;

classLollipopHead
    : '(' ')' ('.' | '-')+
    ;

classLollipopTail
    : ('.' | '-')+ '(' ')'
    ;

// Class entities ID
classEntityID
    : (classPackageID? '.')* classEntityNameID
    ;

classEntityNameID
    : (ID | '~' | '|' | ',' | '+' | '#' | '\''  | ')' | ':' | '*' | escapedKeywords)
        (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '"' | '(' | ')' | ':' | '-' | '*' | escapedKeywords)*
    ;

classPackageID
    : (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '"' | '(' | ')' | ':' | escapedKeywords)
        (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '"' | '(' | ')' | ':' | '-' | '*' | escapedKeywords)*
    ;

// -- COMMON RULES --
// Common content
commonContent
    : comment
    | title
    | note
    | legend
    | ignoredCommand
    | WS? NL
    ;

// Title
title
    : TITLE NL?
    ;

// Note
note
    : WS? NOTE WS escapedString WS AS WS ID (WS color)? WS? NL
    | WS? NOTE WS AS WS ID (WS color)? WS? (NL noteContent? endNote | ':' endLine NL)
    | WS? NOTE WS noteDirection (WS color)? WS? (NL noteContent? endNote | ':' endLine NL)
    | WS? NOTE WS noteDirection WS OF WS noteEntityID (WS color)? (NL noteContent? endNote | ':' endLineNoDoubleDots NL)
    | WS? NOTE WS noteDirection WS OF WS noteEntityID (WS color)? WS (NL noteContent? endNote | ':' endLine NL)
    | WS? NOTE WS noteDirection WS ON WS LINK (WS color)? WS? (NL noteContent? endNote | ':' endLine NL)
    ;

noteEntityID
    :  (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '(' | ')' | '.' | '"' | ':' | '-' | '*' |  escapedKeywords)+
    ;

noteDirection
    : LEFT
    | RIGHT
    | TOP
    | BOT
    ;

noteContent
    :   ( WS? (END WS?)? NL
        | WS? (noteWord | NOTE) WS? ((noteWord | NOTE | END | END_NOTE) WS?)* NL
        | WS? END WS? (noteWord | END) WS? ((noteWord | NOTE | END | END_NOTE) WS?)* NL
        | WS? ((END WS? NOTE) | END_NOTE) WS? ((noteWord | NOTE | END | END_NOTE) WS?)+ NL)+
    ;

noteWord
    : (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>' | '.' | '"' | ':' | '-' | '*' |
        escapedNoteKeyword)
    ;

endNote
    : WS? ((END WS? NOTE) | END_NOTE) WS? NL
    ;

// Legend
legend
    : WS? LEGEND (WS noteDirection (WS noteDirection)?)? WS? NL legendContent? endLegend
    ;

legendContent
    :   ( WS? (END WS?)? NL
        | WS? (legendWord | LEGEND) WS? ((legendWord | LEGEND | END | END_LEGEND) WS?)* NL
        | WS? END WS? (legendWord | END) WS? ((legendWord | LEGEND | END | END_LEGEND) WS?)* NL
        | WS? ((END WS? NOTE) | END_LEGEND) WS? ((legendWord | LEGEND | END | END_LEGEND) WS?)+ NL)+
    ;

legendWord
    : (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>' | '.' | '"' | ':' | '-' | '*' |
        escapedLegendKeyword)
    ;

endLegend
    : WS? ((END WS? LEGEND) | END_LEGEND) WS? NL
    ;

// Comment
comment
    : WS? '\'' (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>' | '.' | '"' | ':' | '-' | '*' |
        escapedKeywords | WS)* NL
    | '/\'' (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>' | '.' | '"' | ':' | '-' | '*' |
        escapedKeywords | WS | NL)* '\'/' NL
    ;

// -- TOOL RULES --
// Generics
generic
    : '<' genericContent '>'
    ;

genericContent
    : genericPart (generic genericPart?)*
    ;

genericPart
    : (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '.' | '"' | ':' | '-' | '*' | escapedKeywords | WS)+
    ;

stereotype
    : '<' '<' stereotypeContent '>' '>'
    ;

stereotypeContent
    : (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '.' | '"' | ':' | '-' |
        escapedKeywords | WS | '>')+
    ;

// Arrows
arrowHead
    : '<' '|'
    #arrowFullHead
    | '<'
    #arrowBasicHead
    | 'o'
    #arrowCircleHead
    | '*'
    #arrowStarHead
    | '+'
    #arrowPlusHead
    ;

arrowBody
    : '.' ('.' | '-')* (arrowDirection ('.' | '-')+)?
    #arrowDotBody
    | '-'('.' | '-')* (arrowDirection ('.' | '-')+)?
    #arrowDashBody
    ;

arrowDirection
    : LEFT_A | RIGHT_A | LEFT | RIGHT | UP | DOWN
    ;

arrowTail
    : '|' '>'
    #arrowFullTail
    | '>'
    #arrowBasicTail
    | 'o'
    #arrowCircleTail
    | '*'
    #arrowStarTail
    | '+'
    #arrowPlusTail
    ;

// Escaped string
escapedString
    :  '"' escapedStringContent '"'
    ;

escapedStringContent
    : (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>'
        | '.' | ':' | '-' | '*' | WS | escapedKeywords | NL)+
    ;

// Escaped keywords
escapedKeywords
    : escapedCommonKeyword | END | NOTE | LEGEND | END_NOTE | END_LEGEND
    ;

escapedNoteKeyword
    : escapedCommonKeyword | LEGEND
    ;

escapedLegendKeyword
    : escapedCommonKeyword | NOTE
    ;

escapedCommonKeyword
    : CLASS | ABSTRACT | INTERFACE | ENUM | PACKAGE | IMPLEMENTS | EXTENDS | AS
    | TITLE | LEFT | RIGHT | LEFT_A | RIGHT_A | TOP | BOT | UP | DOWN | OF | ON | LINK | NAMESPACE | IGNORED
    | ACTOR | USECASE | RECTANGLE
    ;

// Colors
color
    : '#' (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '(' | ')' | '<' | '>' | '.' | '"' | ':' | '-' | '*' | escapedKeywords)+
    ;

// Line end (everything but NL)
endLine
    : (endLineNoDoubleDots | ':')+
    ;

endLineNoDoubleDots
    : (ID | '~' | '|' | ',' | '+' | '#' | '\'' | '{' | '}' | '(' | ')' | '<' | '>' | '.' | '"' | '-' | '*' |
        escapedKeywords | WS)+
    ;

// Ignored commands
ignoredCommand
    : START_UML
    | IGNORED
    ;

// TOKENS
// Common
START_UML : '@startuml';
END_UML : ('@enduml' | '@end');
TITLE : 'title ' ~[\r\n]* '\n';
AS : 'as';

// Directions
LEFT : 'left';
LEFT_A : 'le' | 'l';
RIGHT : 'right';
RIGHT_A : 'ri' | 'r';
UP : 'up' | 'u';
DOWN : 'down' | 'do' | 'd';
TOP : 'top';
BOT : 'bottom';

// Notes & legends
NOTE : 'note';
LEGEND : 'legend';
OF : 'of';
ON : 'on';
LINK : 'link';
END : 'end';
END_NOTE : 'endnote';
END_LEGEND : 'endlegend';

// Class diagram
CLASS : 'class';
ABSTRACT : 'abstract' | 'abstract class';
INTERFACE : 'interface';
ENUM : 'enum';
PACKAGE : 'package';
NAMESPACE : 'namespace';
IMPLEMENTS : 'implements';
EXTENDS : 'extends';

// Usecase
ACTOR : 'actor';
USECASE : 'usecase';
RECTANGLE : 'rectangle';

// Ignored commands
IGNORED
    : WS? ('hide' | 'show ' | 'newpage' | 'page' | 'skinparam' | '!'
        | 'set' | 'caption') ~[\r\n{]* ('{' ~[}]* '}')? '\n'
    | WS? 'left to right direction' WS? '\n'
    | WS? 'top to bottom direction' WS? '\n'
    ;

// Separators
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
GT : '>';
LT : '<';
DOT : '.';
MIN : '-';
PLUS : '+';
STAR : '*';
HASH : '#';
QUOTE : '\'';
DQUOTE : '"';
DOTS : ':';
COMMA : ',';
BAR : '|';
TILDE : '~';

// Identifier
ID : Letter+;
fragment
Letter
    : ~[\'"{}()<>.,\-:\r\n \t#+]
    | [0-9]
    ;

// Spaces
WS : (' ' | '\t')+;
NL : ('\r'? '\n')+;