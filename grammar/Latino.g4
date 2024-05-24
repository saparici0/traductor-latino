grammar Latino;

gram
    : sec EOF
    | EOF
    |
    ;

sec
    : secnovac
    |
    ;

// Root rule
secnovac
    : asig sec
    | llamfunc sec
    | incr sec
    | si sec
    | func sec
    | retornar sec
    | elegir sec
    | romper sec
    | desde sec
    | mientras sec
    | para sec
    | repetir sec
    | funcreserv sec
    ;

romper
    : 'romper'
    ;

llamfunc
    : ID dictargs args
    ;

dictargs
    : '[' expv ']' dictargs
    | '.' ID dictargs
    |
    ;

args
    : '(' (exp)? (',' exp)* ')'
    ;

incr
    : ID INCR
    ;

asig
    : ID (',' ID)* OP_ASIG ( exp | funcalt ) (',' ( exp | funcalt ) )*
    ;

// Expressions

exp
    : OPI exp
    | '(' exp ')' (OP exp)*
    | val (OP exp)*
    ;

val
    : ID dictargs
    | llamfunc
    | funcreservret
    | list
    | dic
    | ( REAL | 'cierto' | 'verdadero' | 'falso' | CADENA | 'nulo' )
    ;

// Functions
func
    : FUNC ID args secnovac 'fin'
    ;

funcalt
    : FUNC args secnovac 'fin'
    ;

// RETORNAR

retornar
    : RETORNAR exp
    ;

// Lists
list
    : '[' ']'
    | '[' exp (',' exp)* ']'
    ;

dic // DICCIONARIOS
    : '{' '}'
    | '{' expv ':' ( exp | funcalt )  (',' expv ':' ( exp | funcalt ) )* '}'
    ;

si // CONDICIONAL SI
    : 'si' exp secnovac (osi)* (sino)? 'fin'
    ;

osi
    : 'osi' exp secnovac
    ;

sino
    : 'sino' secnovac
    ;

elegir
    : 'elegir' exp caso (caso)* otro 'fin'
    ;

caso
    : 'caso' exp ':' sec
    ;

otro:
    OTRO ':' sec
    ;

// Loops
desde
    : 'desde' '(' asig ';' exp ';' ( asig | incr | llamfunc ) ')' sec 'fin'
    ;

mientras
    : 'mientras' exp sec 'fin'
    ;

para
    : 'para' ID 'en' 'rango' paraargs sec 'fin'
    ;

paraargs
    : '(' ('-')? REAL (',' ('-')? REAL)? (',' ('-')? REAL)? ')' // AMBIGUEDAD
    ;

repetir
    : 'repetir' sec 'hasta' exp // PUEDE SER CUALQUIER EXP? P.EJ VERDADERO?
    ;

funcreservret
    : FUNCRESERVRET args
    ;

funcreserv
    : FUNCRESERV args
    ;

expv
    : '(' + expv + ')'
    | CADENA
    | exp OP exp
    ;

// NO TERMINALES

COMENTARIO
    : '/*' .*? '*/' -> skip
    ;

COMENTARIOLINEA
    : '//' ~[\r\n]* -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

// ---

OP_ASIG
    : ('=' | '+=' | '-=' | '*=' | '/=' | '%=')
    ;

OPI
    : ( '!' | '+' | '-' )
    ;

INCR
    : ( '++' | '--' )
    ;

FUNCRESERVRET
    : ( 'tipo' | 'acadena' | 'alogico' | 'anumero' | 'leer' )
    ;

FUNCRESERV
    : ( 'escribir' | 'imprimir' | 'poner' | 'imprimirf' | 'limpiar' | 'error' )
    ;

OTRO
    : ( 'otro' | 'defecto' )
    ;

RETORNAR
    : ('retornar' | 'retorno' | 'ret')
    ;

FUNC
    : ( 'fun' | 'funcion' )
    ;

OP // OPERADORES
    : ( '+' | '-' | '*' | '/' | '%' | '^' ) // ARITMETICA
    | ( '==' | '!=' | '>' | '<' | '>=' | '<=' | '~=' ) // RELACIONALES
    | ( '&&' | '||' ) // LOGICOS
    | '..' // DE CADENA
    ;

// ---

ID
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

REAL
    : [0-9]+('.'[0-9]+)?
    ;

CADENA
    : '"' .*? '"'
    | '\'' .*? '\''
    ;
