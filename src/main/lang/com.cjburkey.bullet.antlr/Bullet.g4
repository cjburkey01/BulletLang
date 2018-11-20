grammar Bullet;

@lexer::members {
    boolean iws = true;
}

// Comments
SL_COMMENT  : '#' ~('\n')* '\n' -> skip ;
ML_COMMENT  : '/#' .*? '#/' -> skip ;

// Ignore whitespace but allow through if necessary
WS          : [ \t\r\n\f]+ { if(iws) skip(); } ;

// Mid
REQUIRE     : 'require' ;
NAMESPACE   : 'namespace' ;
CLASS       : 'class' ;
DEF         : 'def' ;
SEMI        : ';' ;
ELSE        : 'else' ;
IF          : 'if' ;
RETURN      : 'return' ;
COLON       : ':' ;
DEC         : ':=' ;
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
program         : requirements programIn EOF ;

requirements    : requirement requirements
                |
                ;

requirement     : REQUIRE STRING SEMI ;

programIn       : namespace programIn
                | content programIn
                | statement programIn
                |
                ;

namespace       : NAMESPACE IDENTIFIER LB namespaceIn RB ;

namespaceIn     : content namespaceIn
                | 
                ;

content         : variableDec
                | functionDec
                | classDef
                ;

functionDec     : DEF (IDENTIFIER | PLUS | MINUS | TIMES | DIV | POW | ROOT) (LP arguments? RP)? typeDec? LB statements RB ;

arguments       : argument COM arguments
                | argument
                ;

argument        : IDENTIFIER typeDec? ;

typeDec         : OF IDENTIFIER ;

statements      : statement statements
                |
                ;

statement       : variableDec               # StatementVariableDef
                | variableAssign SEMI       # StatementVariableAssign
                | ifStatement               # StatementIf
                | expression SEMI           # StatementExpression
                | RETURN expression SEMI    # StatementReturn
                | expression                # StatementReturn   // Allow raw expression returns (shorthand)
                ;

// Possible variable types:
//      [@[@]]<name> [of <type>] := <value>
//      :[@[@]]variable of Type
variableDec     : VAR_TYPE? IDENTIFIER typeDec? DEC expression SEMI     // Declaration using ':='
                | COLON VAR_TYPE? IDENTIFIER typeDec SEMI               // No-value declaration
                ;

variableAssign  : VAR_TYPE? IDENTIFIER EQ expression ;

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
                
                | LP expression RP                                  # ParenthesisWrap   // Like in math
                
                | expression PER IDENTIFIER LP funcParams? RP       # Reference         // Definitely function
                | expression PER IDENTIFIER funcParams?             # Reference         // Function or variable
                | expression PER VAR_TYPE? IDENTIFIER               # Reference         // Variable
                | IDENTIFIER LP funcParams? RP                      # Reference         // Definitely function
                | IDENTIFIER funcParams?                            # Reference         // Function or variable
                | VAR_TYPE? IDENTIFIER                              # Reference         // Variable
                ;

funcParams      : expression COM funcParams
                | expression
                ;

ifStatement     : IF expression LB statements RB
                | ELSE expression? LB statements RB
                ;

classDef        : CLASS IDENTIFIER (OF types)? LB classMembers RB ;

classMembers    : variableDec classMembers
                | functionDec classMembers
                |
                ;

types           : IDENTIFIER COM types
                | IDENTIFIER
                ;
