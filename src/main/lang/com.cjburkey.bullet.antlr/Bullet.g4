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
                | statement programIn
                | content programIn
                |
                ;

namespace       : NAMESPACE IDENTIFIER LB namespaceIn RB ;

namespaceIn     : content namespaceIn
                | 
                ;

content         : function
                | classDef
                ;

function        : attrib? DEF (IDENTIFIER | PLUS | MINUS | TIMES | DIV | POW | ROOT) (LP arguments? RP)? typeDef? LB statements RB ;

arguments       : argument COM arguments
                | argument
                ;

argument        : IDENTIFIER typeDef?
                ;

typeName        : IDENTIFIER ;

typeDef         : OF typeName ;

statements      : statement statements
                |
                ;

statement       : variableDef SEMI          # StatementVariableDef
                | ifStatement               # StatementIf
                | expression SEMI           # StatementExpression
                | RETURN expression SEMI    # StatementReturn
                | expression                # StatementReturn   // Allow raw expression returns (shorthand)
                ;

//  Variable format:
//      [':']['@']['@']<NAME> ['of' <TYPE>] [<':=' / '='> <EXPRESSION>]
variableDef     : VAR_TYPE? IDENTIFIER typeDef DEC expression   // Declaration using ':='
                | VAR_TYPE? IDENTIFIER DEC expression           // Value declaration
                | COLON? VAR_TYPE? IDENTIFIER EQ expression     // Value assignment or declaration
                | COLON VAR_TYPE? IDENTIFIER typeDef            // Value declaration
                | VAR_TYPE? IDENTIFIER EQ expression            // Value assignment
                ;

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

classMembers    : variableDef SEMI classMembers
                | function classMembers
                |
                ;

types           : IDENTIFIER COM types
                | IDENTIFIER
                ;

attrib          : LBR attribIn RBR ;

attribIn        : IDENTIFIER COM attribIn
                | IDENTIFIER
                ;
