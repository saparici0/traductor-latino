grammar Prueba;

commands   : command commands
        | EOF
        |
        ;

command    : conditional
        | repeat
        | printexpr
        | VAR ID 'as' expr SMCOLON
        ;

conditional : 'if' expr ROP expr 'then' commands 'endif';
repeat    : 'repeat' expr 'times' commands 'endrepeat';
printexpr  : 'print' expr SMCOLON ;

expr:  expr MULOP expr
   |  expr SUMOP expr
   |  DOUBLE
   |  PIZQ expr PDER
   |  ID
   ;

COMMENT       : '/*' .*? '*/' -> skip ;
LINE_COMMENT   : '//' ~[\r\n]* -> skip ;
WS    : [ \t\r\n]+ -> skip ;
VAR       : 'var';
PIZQ   : '(' ;
PDER   : ')' ;
ROP       : ( '<' | '<=' | '>=' | '>' | '==' | '!=' ) ;
SMCOLON : ';' ;
MULOP  : ( '*' | '/' );
SUMOP  : ('+' | '-') ;
DOUBLE : [0-9]+( | [.][0-9]+);
ID        : [a-zA-Z][a-zA-Z0-9_]* ;