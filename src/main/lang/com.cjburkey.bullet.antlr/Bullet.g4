grammar Bullet;

@lexer::members {
    boolean iws = true;
}

// Comments
SL_COMMENT  : '#' ~('\n')* '\n' -> skip ;
ML_COMMENT  : ('/#' | '/*') .*? ('#/' | '*/') -> skip ;

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
BIT_AND     : '&' ;
BIT_OR      : '|' ;
BIT_XOR     : '^' ;
BIT_NOT     : '~' ;
BIT_RIGHT   : '>>' ;
BIT_LEFT    : '<<' ;
NOT         : '!' ;
NOT_EQ      : '!=' ;
EQUAL       : '==' ;
GREAT       : '>' ;
LESS        : '<' ;
LESS_EQ     : '<=' ;
GREAT_EQ    : '>=' ;

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
name            : IDENTIFIER ;

program         : requirements programIn EOF ;

partialExp      : expression EOF ;      // Used with smart strings in the parser

requirements    : requirement requirements
                |
                ;

requirement     : REQUIRE STRING SEMI ;

programIn       : namespace programIn
                | functionDec programIn
                | classDec programIn
                | statement programIn
                |
                ;

namespace       : NAMESPACE name LB namespaceIn RB ;

namespaceIn     : variableDec namespaceIn
                | functionDec namespaceIn
                | classDec namespaceIn
                |
                ;

functionDec     : DEF (name | op) (LP arguments? RP)? typeDec? LB scope RB ;

op              : POW
                | ROOT
                | TIMES
                | DIV
                | PLUS
                | MINUS
                | BIT_AND
                | BIT_OR
                | BIT_XOR
                | BIT_NOT
                | BIT_RIGHT
                | BIT_LEFT
                | NOT
                | NOT_EQ
                | GREAT
                | GREAT_EQ
                | LESS
                | LESS_EQ
                | EQUAL ;

arguments       : argument COM arguments
                | argument
                ;

argument        : name typeDec? ;

arrayType       : LBR expression? RBR ;

typeDec         : OF IDENTIFIER arrayType? ;

scope           : statement scope
                |
                ;

statement       : variableDec               # StatementVariableDec
                | variableAssign SEMI       # StatementVariableAssign
                | ifStatement               # StatementIf
                | expression SEMI           # StatementExpression
                | RETURN expression SEMI    # StatementReturn
                | expression                # StatementReturn       // Allow raw expression returns (shorthand)
                ;

variableRef     : VAR_TYPE? name ;

// Possible variable types:
//      [@[@]]<name> [of <type>] := <value>
//      :[@[@]]variable of Type
variableDec     : variableRef typeDec? DEC expression SEMI          // Declaration using ':='
                | COLON variableRef typeDec SEMI                    // No-value declaration
                ;

variableAssign  : variableRef EQ expression ;

exprList        : expression COM exprList
                | expression
                ;

reference       : name LP exprList? RP                          # FunctionReference
                | variableRef                                   # AmbigReference        // Variable or function
                | name exprList                                 # FunctionReference
                | op exprList?                                  # FunctionReference
                ;

expression      // Literals
                : BOOL                                          # Boolean
                | INTEGER                                       # Integer
                | FLOAT                                         # Float
                | STRING                                        # String
                | LIT_STRING                                    # LiteralString
                
                // Bitwise operators
                | BIT_NOT expression                                    # UnaryOp
                | expression (BIT_AND | BIT_OR | BIT_XOR) expression    # BinaryOp
                | expression (BIT_RIGHT | BIT_LEFT) expression          # BinaryOp
                
                // Value operators
                | NOT expression                                                                # UnaryOp
                | expression (LESS | GREAT | LESS_EQ | GREAT_EQ | EQUAL | NOT_EQ) expression    # BinaryOp
                
                // Mathematical operators
                | expression (POW | ROOT)                       # UnaryOp
                | expression (POW | ROOT) expression            # BinaryOp
                | MINUS expression                              # UnaryOp               // This must be out of order for op-precedence
                | expression (TIMES | DIV) expression           # BinaryOp
                | expression (PLUS | MINUS) expression          # BinaryOp
                | expression (GREAT | LESS) expression          # BinaryOp
                | expression (GREAT | LESS) expression          # BinaryOp
                
                | LP expression RP                              # ParenthesisWrap
                
                // References
                | reference                                     # Ref
                | expression PER reference                      # ParentChild
                
                | LB exprList COM? RB                           # ArrayValue
                ;

ifStatement     : IF expression LB scope RB
                | IF expression statement 
                | ELSE expression? LB scope RB
                | ELSE expression? statement
                ;

classDec        : CLASS name (OF types)? LB classMembers RB ;

classMembers    : variableDec classMembers
                | functionDec classMembers
                |
                ;

types           : IDENTIFIER COM types
                | IDENTIFIER
                ;
