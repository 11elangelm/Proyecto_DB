/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
grammar Gramatica;


//prods
sqlProgram : statement (DOTCOMMA statement)* (DOTCOMMA)? EOF;

statement: dbOperation | tOperation | dOperation;

dbOperation: CREATE DATABASE ID #crearDB
| DROP DATABASE ID #eliminarDB
| USE DATABASE ID #usarDB
| SHOW DATABASES #mostrarDB
| ALTER DATABASE ID RENAME TO ID #alterarDB
;

dOperation: insert 
| update
| delete 
| query;

tOperation: CREATE TABLE ID LPARENT columna (COMMA columna)* (constraints)* RPARENT #crearTB
| ALTER TABLE ID (tableAction (COMMA tableAction)*)+ #alterarTB
| DROP TABLE ID #eliminarTB
| SHOW TABLES #mostrarTablasTB
| SHOW COLUMNS FROM ID #mostrarColumnasTB
;

columna:ID type;
constraints: (CONSTRAINT constraint (COMMA CONSTRAINT constraint)*);

tableAction: ADD COLUMN columna (constraints | ) #addColumnTB
| ADD CONSTRAINT constraint #addConstraintTB
| DROP COLUMN ID #dropColumnTB
| DROP CONSTRAINT ID #dropConstraintTB
| RENAME TO ID #renameTB
;

type: INT #tipoEntero
    | FLOAT #tipoFloat
    | DATE #tipoDAte
    | CHAR LPARENT NUM RPARENT #tipoChar
    ;

constraint: ID PRIMARY KEY LPARENT (ID(COMMA ID)*)* RPARENT #constraintPK
| ID FOREIGN KEY  LPARENT (ID (COMMA ID)*)* RPARENT references #constraintFK
| ID CHECK (exp1) #constraintCheck
;

references: REFERENCES ID (LPARENT (ID(COMMA ID)*)* RPARENT)?;

exp1: exp2;

exp2: exp2 andExp exp3 | exp3;

exp3: exp3 orExp exp4 | exp4;

exp4: exp4 eqExp exp5 | exp5;

exp5: exp5 glExp unarianFactorExp | unarianFactorExp;

unarianFactorExp: NOT factor | factor;

factor: literal
| LPARENT exp1 RPARENT
| ID(DOT ID)?;

literal: value;

exp: andExp | orExp | notExp;

notExp: NOT;
orExp: OR;
andExp: AND;
eqExp: EQ | NOTEQ;
glExp: LTHAN | EQLTHAN | GTHAN | EQGTHAN;

relExp: eqExp | glExp;

insert: INSERT INTO ID (LPARENT(ID(COMMA ID)*)*RPARENT)? VALUES LPARENT (value (COMMA value)*)* RPARENT;

value: entero | decimal | fecha | character | nullo;
nullo: NULO;
entero:(MINUS)? NUM;
decimal: entero DOT NUM;
fecha:  NUM_DATE ;//MINUS nUM_MONTH MINUS nUM_DAY COMILLA;

nUM_YEAR:NUM ;
nUM_MONTH:NUM;
nUM_DAY:NUM;

character: CHARACTER;

update: UPDATE ID SET asignacion (COMMA asignacion)* (WHERE exp1)?;

asignacion: ID EQ literal;
cond: (expression relExp ( value | expression)) (exp cond)?;

delete: DELETE FROM ID (WHERE exp1)?;

query: SELECT (ASTERISK | column (COMMA column ) * ) FROM ID (COMMA ID)* (WHERE exp1)? (ORDER BY orderBy (COMMA orderBy)*)?;

column: ID(DOT ID)?;

orderBy: expression (ASC | DESC)?;

expression: ID(DOT ID)?;


//Keywords
CREATE:	C R E A T E;
DATABASE: D A T A B A S E;
DATABASES: D A T A B A S E S ;
DROP: D R O P;
USE: U S E ;
SHOW: S H O W ;
ALTER: A L T E R;
RENAME: R E N A M E;
TO: T O ;
TABLE: T A B L E ;
TABLES: T A B L E S;
CONSTRAINT: C O N S T R A I N T ;
COLUMN: C O L U M N ;
COLUMNS: C O L U M N S;
FROM: F R O M ;
ADD: A D D ;
SELECT: S E L E C T;
WHERE: W H E R E;
INT: I N T;
DATE: D A T E;
CHAR: C H A R;
FLOAT: F L O A T;
PRIMARY: P R I M A R Y ;
CHECK: C H E C K ;
REFERENCES: R E F E R E N C E S ;
FOREIGN: F O R E I G N;
KEY: K E Y;
INSERT: I N S E R T ;
INTO: I N T O;
VALUES: V A L U E S ;
ORDER: O R D E R ;
SET: S E T ;
UPDATE: U P D A T E ;
BY: B Y ;
ASC: A S C ;
DESC: D E S C;
AND: A N D;
OR: O R;
NOT: N O T ;
NULO: N U L L;
DELETE: D E L E T E ;

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');

//Characters

WH: [ \t\r\n\f]+ -> channel(HIDDEN);
SINGLE_LINE_COMMENT: '//' ~('\r' | '\n' )*  -> channel(HIDDEN);

//dont know if its neccesary
//MULTILINE_COMMENT: '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN);
ID : LETTER (LETTER | DIGIT)* ;
NUM: DIGIT(DIGIT)* ;
fragment LETTER: ( 'a'..'z' | 'A'..'Z') ;
fragment DIGIT: '0'..'9' ;

COMILLA:'\'';
NUM_DATE:  '\'' DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT '\'' ;
CHARACTER: '\''~('\r'|'\n'|'\'')* '\'';
DOTCOMMA: ';';
LPARENT: '(';
RPARENT: ')';
COMMA: ',';
DOT: '.';
EQ: '=';
NOTEQ: '<>';
LTHAN: '<';
EQLTHAN: '<=';
GTHAN: '>';
EQGTHAN: '>=';
MINUS: '-';
ASTERISK: '*';