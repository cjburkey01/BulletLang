grammar Bullet;

@lexer::members {
    boolean iws = true;
}

// Comments
SL_COMMENT  : '#' ~('\n')* '\n' -> skip ;
ML_COMMENT  : '/*' .*? '*/' -> skip ;

// Ignore whitespace but allow through if necessary
WS          : [ \t\r\n\f]+ { if(iws) skip(); } ;

// Mid
MODULE      : 'module' ;
REQUIRE     : 'require' ;
CLASS       : 'class' ;
DEF         : 'def' ;
SEMI        : ';' ;
ELSE        : 'else' ;
IF          : 'if' ;
EQ          : '=' ;
LP          : '(' ;
RP          : ')' ;
LB          : '{' ;
RB          : '}' ;
LBR         : '[' ;
RBR         : ']' ;
OF          : 'of' ;
COM         : ',' ;
PER         : '.' ;

// Operators
POW         : '**' ;
ROOT        : '//' ;
TIMES       : '*' ;
DIV         : '/' ;
PLUS        : '+' ;
MINUS       : '-' ;

// Late
BOOL        : ('true' | 'false') ;
FLOAT       : INTEGER? '.' INTEGER ;
INTEGER     : DIGIT+ ;
DIGIT       : [0-9] ;
fragment IDA: [A-Za-z_] ;
fragment IDB: [A-Za-z0-9_] ;
fragment ID : IDA IDB* ;
IDENTIFIER  : ID ;
VAR_TYPE    : ('@' | '@@') ;

// Strings
STRING_IN   : ~('\n' | '"')+? ;
STR_INTER   : '@"' ;
STRING      : ('"' | STR_INTER) { iws = false; } STRING_IN? '"' { iws = true; } ;
LIT_STRING  : '"""' { iws = false; } .*? '"""' { iws = true; } ;

// Rules
program         : module requirements programIn EOF ;

module          : MODULE IDENTIFIER SEMI ;

requirements    : requirement requirements
                |
                ;

requirement     : REQUIRE STRING SEMI ;

programIn       : function programIn
                | statement programIn
                | classDef programIn
                |
                ;

function        : attrib? DEF (IDENTIFIER | PLUS | MINUS | TIMES | DIV | POW | ROOT) LP arguments? RP type? LB statements RB ;

arguments       : argument COM arguments
                | argument
                ;

argument        : IDENTIFIER type?
                ;

type            : OF IDENTIFIER ;

statements      : statement statements
                |
                ;

statement       : variableDef SEMI  # StatementVariableDef
                | ifStatement       # StatementIf
                | expression SEMI   # StatementExpression
                ;

variableDef     : VAR_TYPE? IDENTIFIER type variableVal?
                | VAR_TYPE? IDENTIFIER variableVal
                ;

variableVal     : EQ expression ;

expression      : BOOL                                              # Boolean
                | INTEGER                                           # Integer
                | FLOAT                                             # Float
                | STRING                                            # String
                | LIT_STRING                                        # LiteralString
                
                | expression (POW | ROOT)                           # UnaryOp
                | expression (POW | ROOT) expression                # BinaryOp
                | MINUS expression                                  # UnaryOp
                | expression (TIMES | DIV) expression               # BinaryOp
                | expression (PLUS | MINUS) expression              # BinaryOp
                
                | LP expression RP                                  # ParenthesisWrap
                
                | expression PER IDENTIFIER LP funcParams? RP       # Reference
                | expression PER IDENTIFIER funcParams?             # Reference
                | expression PER VAR_TYPE? IDENTIFIER               # Reference
                | IDENTIFIER LP funcParams? RP                      # Reference
                | IDENTIFIER funcParams?                            # Reference
                | VAR_TYPE? IDENTIFIER                              # Reference
                ;

funcParams      : expression COM funcParams
                | expression
                ;

ifStatement     : IF expression LB statements RB
                | ELSE expression? LB statements RB
                ;

classDef        : CLASS IDENTIFIER (OF types)? LB classMembers RB ;

classMembers    : function classMembers
                |
                ;

types           : IDENTIFIER COM types
                | IDENTIFIER
                ;

attrib          : LBR attribIn RBR ;

attribIn        : IDENTIFIER COM attribIn
                | IDENTIFIER
                ;
