grammar BulletLang;

@header {
    package com.cjburkey.bullet.antlr;
}

// TOKENS
COMMENT         : (('//' .*? '\n')
                | ('/*' .*? '*/'))
                -> skip ;

WHITESPACE      : [ \n\t\r]+
                -> skip;

// LITERALS
STRING          : '"' (~'\n')*? '"';
SMART_STRING    : '@"' .*? '"' ;
fragment DIGIT  : [0-9] ;
INTEGER         : DIGIT+ ;
FLOAT           : INTEGER? '.' INTEGER+ ;

// SYMBOLS
SEMI_COLON      : ';' ;
COMMA           : ',' ;
LEFT_PAR        : '(' ;
RIGHT_PAR       : ')' ;
LEFT_BRACE      : '{' ;
RIGHT_BRACE     : '}' ;
EQUALS          : '=' ;

// KEYWORDS
RETURN          : 'return' ;
OF              : 'of' ;
DEF             : 'def' ;
LET             : 'let' ;

// LITERAL
IDENTIFIER      : [A-Za-z_] [A-Za-z_0-9]* ;

// RULES
returnVal   : RETURN expression SEMI_COLON
            | expression
            ;

expression  : FLOAT                     # FloatExpression
            | INTEGER                   # IntegerExpression
            | STRING                    # StringExpression
            | SMART_STRING              # StringExpression
            ;

typeDec     : OF IDENTIFIER ;

parameter   : IDENTIFIER typeDec ;

parameters  : parameters COMMA parameter
            | parameter
            ;

functionDec : DEF IDENTIFIER (LEFT_PAR parameters RIGHT_PAR)? typeDec? LEFT_BRACE scope? RIGHT_BRACE ;

variableDec : LET IDENTIFIER typeDec? EQUALS expression ;

statement   : returnVal                 # ReturnStatement
            | expression SEMI_COLON     # ExpressionStatement
            | variableDec SEMI_COLON    # VariableDecStatement
            | functionDec               # FunctionDecStatement
            ;

scope       : scope statement
            | statement
            ;

program     : scope? ;
