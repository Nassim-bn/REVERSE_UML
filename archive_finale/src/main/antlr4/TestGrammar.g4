grammar TestGrammar;

entryPoint
    : attr*
    ;

attr
    : ID assign content=value ';'
    | ID assign content=table ';'
    | ID content=group
    ;

value
    : ID
    | STR
    ;

group
    : '{' attr* '}'
    ;

table
    : '[' (value ',')* value ']'
    | '[' ']'
    ;

assign
    : ':'
    | '='
    ;


LPAREN          : '(';
RPAREN          : ')';
LBRACK          : '[';
RBRACK          : ']';
LBRACE          : '{';
RBRACE          : '}';
ASSIGN          : '=';
COLON           : ':';
SEMICOLON       : ';';
COMMA           : ',';

ID              : [A-Za-z0-9_\-]+;
STR             : '"' (~('"' | '\\') | '\\' ('"' | '\\'))* '"';

WS              : [ \t\r\n\u000C]+ -> skip;
COMMENT         :   '/*' .*? '*/' -> skip;
LINE_COMMENT    :   '//' ~[\r\n]* -> skip;