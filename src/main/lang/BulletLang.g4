grammar BulletLang;

// IGNORED
COMMENT         : (('//' .*? '\n')
                | ('/*' .*? '*/'))
                -> skip
                ;

// STRINGS
SMART_STRING    : '@"' .*? '"' ;
STRING          : '"' (~'\n')*? '"';

// IGNORED
WHITESPACE      : [ \n\t\r]+
                -> skip
                ;

// NUMBERS
fragment DIGIT  : [0-9] ;
INTEGER         : DIGIT+ ;
FLOAT           : INTEGER? '.' INTEGER+ ;

// SYMBOLS
SEMI_COLON      : ';' ;
COMMA           : ',' ;
LEFT_PAR        : '(' ;
RIGHT_PAR       : ')' ;
LEFT_D_BRACE    : '${' ;
LEFT_BRACE      : '{' ;
RIGHT_BRACE     : '}' ;
EQUALS          : '=' ;

// Level 1 operators
TIMES           : '*' ;
DIV             : '/' ;

// Level 2 operators
ADD             : '+' ;
SUB             : '-' ;

// Level 3 operators
GT              : '>' ;
LT              : '<' ;
GTE             : '>=' ;
LTE             : '<=' ;
EQ              : '==' ;
NEQ             : '!=' ;

// Level 4 operators
AND             : '&&' ;
OR              : '||' ;

// KEYWORDS
RETURN          : 'return' ;
OF              : 'of' ;
DEF             : 'def' ;
LET             : 'let' ;
TRUE            : 'true' ;
FALSE           : 'false' ;
CLASS           : 'class' ;

// NAMES
IDENTIFIER      : [A-Za-z_] [A-Za-z_0-9]* ;

// RULES
classInner  : variableDec       # VariableDecClass
            | functionDec       # FunctionDecClass
            | classDec          # ClassDecClass
            ;

classScope  : classScope classInner
            | classInner
            ;

classDec    : CLASS IDENTIFIER typeDec? LEFT_BRACE classScope? RIGHT_BRACE ;

arguments   : arguments COMMA expression
            | expression
            ;

reference   : IDENTIFIER LEFT_PAR arguments? RIGHT_PAR  // Definitely a Function Reference
            | IDENTIFIER arguments                      // Definitely a Function Reference
            | IDENTIFIER                                // Variable/Function Reference
            ;

expression  : reference                                                     # ReferenceExpression
            | LEFT_PAR expression RIGHT_PAR                                 # ParExpression
            | op=SUB expression                                             # UnOpExpression
            | expression op=(TIMES | DIV) expression                        # BinOpExpression
            | expression op=(ADD | SUB) expression                          # BinOpExpression
            | expression op=(GT | LT | GTE | LTE | EQ | NEQ) expression     # BinOpExpression
            | expression op=(AND | OR) expression                           # BinOpExpression
            | INTEGER                                                       # IntegerExpression
            | (TRUE | FALSE)                                                # BooleanExpression
            | FLOAT                                                         # FloatExpression
            | SMART_STRING                                                  # StringExpression
            | STRING                                                        # StringExpression
            ;

returnVal   : RETURN expression SEMI_COLON
            | expression
            ;

typeDec     : OF IDENTIFIER ;

parameter   : IDENTIFIER typeDec ;

parameters  : parameters COMMA parameter
            | parameter
            ;

functionDec : DEF IDENTIFIER? (LEFT_PAR parameters RIGHT_PAR)? typeDec? LEFT_BRACE scope? RIGHT_BRACE ;

variableDec : LET IDENTIFIER typeDec? EQUALS expression ;

statement   : expression SEMI_COLON     # ExpressionStatement
            | variableDec SEMI_COLON    # VariableDecStatement
            | functionDec               # FunctionDecStatement
            | classDec                  # ClassDecStatement
            | returnVal                 # ReturnStatement
            ;

scope       : scope statement
            | statement
            ;

program     : scope? ;

rawExpr     : expression EOF ;
