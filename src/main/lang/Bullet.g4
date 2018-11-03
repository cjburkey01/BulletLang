grammar Bullet;

// Tokens
ID  : [a-z]+ ;
WS : [ \t\r\n]+ -> skip ;

// Rules
r   : 'hello' ID;
