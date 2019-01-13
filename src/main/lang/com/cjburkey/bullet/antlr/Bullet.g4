grammar Bullet;

/* TOKENIZER RULES */

// Comments
SL_COMMENT      : '//' ~('\n')* '\n' -> skip ;
ML_COMMENT      : '/*' .*? '*/' -> skip ;

// Bullet is whitespace-ignorant
WS              : [ \t\r\n\f]+ -> skip ;

// Literals (integers, floats, strings, booleans)
INTEGER         : [0-9]+ ;
FLOAT           : INTEGER*? '.' INTEGER+ ;
BOOLEAN			: ('true' | 'false') ;
STRING          : ('"' ~('\n')* '"')	// Normal string
				| ('`' .*? '`')			// String interpolation: ${<EXPRESSION>} will execute <EXPRESSION> and put the result in the string
				;						// These smart strings allow all characters within them, including new lines, tabs, etc

// Keywords


// Identifiers cannot start with numerals, but may contain ANY CHARACTERS!
// New lines, comments, keywords, and special characters will already be tokenized as they have a higher tokenizer priority
// This allows any UTF-8 valid character to be a name for a function/variable, though it is REALLY suggested not to stray far from
// 	English characters to allow easier implementation of the libraries.
IDENTIFIER      : ~[0-9] .*? ;

/* PARSER RULES */

// Example program. This is just to get the parser to generate before I write the grammar.
program:		INTEGER EOF ;
