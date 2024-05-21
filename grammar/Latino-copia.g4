grammar Latino;

sections
    : section sections
    | EOF
    |
    ;

section : assign ;

assign: ID + OP + exp ;

exp : INIT_OP + exp ;
exp : '(' exp ')'

INIT_OP : ( '!' | '-' | '+' ) ;



