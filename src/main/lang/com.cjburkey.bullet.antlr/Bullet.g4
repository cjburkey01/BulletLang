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
name            : IDENTIFIER ;

program         : requirements programIn EOF ;

partialExp      : expression EOF ;      // Used with smart strings in the Java source

requirements    : requirement requirements
                |
                ;

requirement     : REQUIRE STRING SEMI ;

programIn       : namespace programIn
                | content programIn
                | statement programIn
                |
                ;

namespace       : NAMESPACE name LB namespaceIn RB ;

namespaceIn     : content namespaceIn
                | 
                ;

content         : variableDec
                | functionDec
                | classDec
                ;

functionDec     : DEF (name | PLUS | MINUS | TIMES | DIV | POW | ROOT) (LP arguments? RP)? typeDec? LB statements RB ;

arguments       : argument COM arguments
                | argument
                ;

argument        : name typeDec? ;

typeDec         : OF IDENTIFIER ;

statements      : statement statements
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
                | COLON VAR_TYPE? name typeDec SEMI                 // No-value declaration
                ;

variableAssign  : variableRef EQ expression ;

expression      : BOOL                                          # Boolean
                | INTEGER                                       # Integer
                | FLOAT                                         # Float
                | STRING                                        # String
                | LIT_STRING                                    # LiteralString
                
                | expression (POW | ROOT)                       # UnaryOp
                | expression (POW | ROOT) expression            # BinaryOp
                | MINUS expression                              # UnaryOp       // This must be out of order for op-precedence
                | expression (TIMES | DIV) expression           # BinaryOp
                | expression (PLUS | MINUS) expression          # BinaryOp
                
                | LP expression RP                              # ParenthesisWrap
                
                | expression PER name LP funcParams? RP         # FunctionReference
                | expression PER variableRef                    # Reference             // Variable or function
                | expression PER name funcParams                # FunctionReference
                | name LP funcParams? RP                        # FunctionReference
                | variableRef                                   # Reference             // Variable or function
                | name funcParams                               # FunctionReference
                ;

funcParams      : expression COM funcParams
                | expression
                ;

ifStatement     : IF expression LB statements RB
                | ELSE expression? LB statements RB
                ;

classDec        : CLASS name (OF types)? LB classMembers RB ;

classMembers    : variableDec classMembers
                | functionDec classMembers
                |
                ;

types           : IDENTIFIER COM types
                | IDENTIFIER
                ;
