grammar GRAM;

// Root rule
sec
    : asig sec
    | llamfunc sec
    | incr sec
    | si sec
    | func sec
    | RETORNAR exp sec
    | elegir sec
    | 'romper' sec
    | desde sec
    | mientras sec
    | para sec
    | repetir sec
    | funcReserv2 '(' argExp sec
    |
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
    : '(' exp (',' exp)* ')'
    ;

incr
    : ID '++'
    | ID '--'
    ;

asig
    : ID (',' ID)* OP_ASIG exp (',' exp)*
    ;

OP_ASIG
    : ('=' | '+=' | '-=' | '*=' | '/=' | '%=')
    ;

// Expressions

exp
    : '!' exp
    | '+' exp
    | '-' exp
    | '(' exp ')' (OP exp)*
    | val (OP exp)*
    | valesp (OP exp)*
    | funcalt
    ;

valesp
    : list
    | dict
    ;

val
    : ID dictargs
    | llamfunc
    | funcreservret
    | ( REAL | 'cierto' | 'verdadero' | 'falso' | CAD_TKN | 'nulo' )
    ;

// Operators
 OP
    : ( '+' | '-' | '*' | '/' | '%' | '^' ) // ARITMETICA
    | ( '==' | '!=' | '>' | '<' | '>=' | '<=' | '~=' ) // RELACIONALES
    | ( '&&' | '||' ) // LOGICOS
    | '..' // DE CADENA
    ;

// Functions
func:  FUNC ID args secNoVac 'fin'
    ;

funcalt:  FUNC '(' argExp secNoVac 'fin'
       ;

 FUNC: 'fun' | 'funcion'
      ;

argExp: exp argExp1 ')'
      | ')'
      ;

argExp1: ',' exp argExp1
       |
       ;

secNoVac: asigIncFuncLlam sec
         | si sec
         | func sec
         | RETORNAR exp sec
         | elegir sec
         | 'romper' sec
         | desde sec
         | mientras sec
         | para sec
         | repetir sec
         | funcReserv2 '(' argExp sec
         ;

// Return statement
RETORNAR: 'retornar' | 'retorno' | 'ret'
           ;

// Lists
list: '[' list1
     ;

list1: exp list2 ']'
     | ']'
     ;

list2: ',' exp list2
      |
      ;

// Dictionaries
dic: '{' dic1
    ;

dic1: expv ':' dic2 '}'
    | '}'
    ;

dic2: exp dic3
     ;

dic3: ',' expv ':' exp dic3
     |
     ;

// Conditionals
si: 'si' exp sec osi sino 'fin'
   ;

osi: 'osi' exp sec osi
    |
    ;

sino: 'sino' sec
     |
     ;

elegir: 'elegir' exp 'caso' exp ':' sec caso otro 'fin'
      ;

caso: 'caso' exp ':' sec caso
     |
     ;

otro: otroPc ':' sec
     |
     ;

otroPc: 'otro' | 'defecto'
       ;

// Loops
desde: 'desde' '(' asig ';' exp ';' asigIncFuncLlam1 ')' sec 'fin'
     ;

mientras: 'mientras' exp sec 'fin'
         ;

para: 'para' ID 'en' 'rango' '(' paraArg sec 'fin'
     ;

paraArg: REAL paraArg1
        ;

paraArg1: ',' REAL paraArg2
         | ')'
         ;

paraArg2: ',' REAL ')'
         | ')'
         ;

repetir: 'repetir' sec 'hasta' exp
        ;

// Reserved Functions
funcReserv: funcReserv1 '(' argExp
          | funcReserv2 '(' argExp
          ;


funcreservret
    : FUNCRESERVRET args
    ;

funcreserv
    : FUNCRESERV args
    ;

FUNCRESERVRET
    : ( 'tipo' | 'acadena' | 'alogico' | 'anumero' | 'leer' )
    ;

FUNCRESERV
    : ( 'escribir' | 'imprimir' | 'poner' | 'imprimirf' | 'limpiar' | 'error' )
    ;


funcReserv1: 'tipo' | 'acadena' | 'alogico' | 'anumero' | 'leer'
           ;

funcReserv2: 'escribir' | 'imprimir' | 'poner' | 'imprimirf' | 'limpiar' | 'error'
           ;

expv: '(' expv1
     | val (OP exp)*
     | valesp  OP exp
     ;

expv1: val (OP exp)* ')' (OP exp)*
     | valesp expv2
     | '(' expv ')' (OP exp)*
     ;

expv2:  OP exp ')' (OP exp)*
      | ')'  OP exp
      ;

ID
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

REAL: [0-9]+('.'[0-9]+)?
    ;

CAD_TKN: '"' .*? '"'
       ;

COMMENT: '/*' .*? '*/' -> skip
       ;

LINE_COMMENT: '//' ~[\r\n]* -> skip
            ;

WS: [ \t\r\n]+ -> skip
   ;
