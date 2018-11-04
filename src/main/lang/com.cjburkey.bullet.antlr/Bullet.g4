grammar Bullet;

// Ignored
SL_COMMENT  : '#' ~('\n')* '\n' -> skip ;
ML_COMMENT  : '/*' .*? '*/' -> skip ;
WS          : [ \t\r\n\f]+ -> skip ;

// Key
MODULE      : 'module' ;
DEF         : 'def' ;
SEMI        : ';' ;
ELSE        : 'else' ;
IF          : 'if' ;
EQ          : '=' ;
LP          : '(' ;
RP          : ')' ;
LB          : '{' ;
RB          : '}' ;
COL         : ':' ;
COM         : ',' ;

// Literals
IDENTIFIER  : [A-Za-z_][A-Za-z0-9_]* ;
FLOAT       : INTEGER? '.' INTEGER ;
INTEGER     : DIGIT+ ;
DIGIT       : [0-9] ;
STRING      : '"' ~('\n'|'"')*? '"' ;
LIT_STRING  : '"""' .*? '"""' ;

// Rules
program         : module programIn EOF ;

module          : MODULE IDENTIFIER SEMI ;

programIn       : function programIn
                | statement programIn
                |
                ;

function        : DEF IDENTIFIER LP arguments? RP functionType LB statements RB ;

arguments       : argument COM arguments
                | argument
                ;

argument        : IDENTIFIER (COL IDENTIFIER)?
                ;

functionType    : COL IDENTIFIER
                |
                ;

statements      : statement statements
                |
                ;

statement       : variableDef SEMI  # StatementVariableDef
                | ifStatement       # StatementIf
                | expression SEMI   # StatementExpression
                ;

variableDef     : IDENTIFIER variableVal
                | IDENTIFIER COL IDENTIFIER
                | IDENTIFIER COL IDENTIFIER variableVal
                ;

variableVal     : EQ expression ;

expression      : INTEGER                       # Integer
                | FLOAT                         # Float
                | STRING                        # String
                | LIT_STRING                    # LiteralString
                | IDENTIFIER LP funcParams? RP  # Reference
                | IDENTIFIER funcParams?        # Reference
                ;

funcParams      : expression COM funcParams
                | expression
                ;

ifStatement     : IF expression LB statements RB
                | ELSE expression? LB statements RB
                ;
