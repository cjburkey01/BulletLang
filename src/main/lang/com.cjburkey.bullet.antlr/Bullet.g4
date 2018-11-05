grammar Bullet;

@lexer::members {
    boolean iws = true;
}

// Comments
SL_COMMENT  : ('##' | '//') ~('\n')* '\n' -> skip ;
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
OF          : 'of' ;
COM         : ',' ;

// Late
BOOL        : ('true' | 'false') ;
FLOAT       : INTEGER? '.' INTEGER ;
INTEGER     : DIGIT+ ;
DIGIT       : [0-9] ;
IDENTIFIER  : [A-Za-z_][A-Za-z0-9_]* ;

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

function        : DEF IDENTIFIER LP arguments? RP type? LB statements RB ;

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

variableDef     : IDENTIFIER type variableVal?
                | IDENTIFIER variableVal
                ;

variableVal     : EQ expression ;

expression      : LP expression RP                      # ParenthesisWrap
                | BOOL                                  # Boolean
                | INTEGER                               # Integer
                | FLOAT                                 # Float
                | STRING                                # String
                | LIT_STRING                            # LiteralString
                | IDENTIFIER LP funcParams? RP          # Reference
                | IDENTIFIER funcParams?                # Reference
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
