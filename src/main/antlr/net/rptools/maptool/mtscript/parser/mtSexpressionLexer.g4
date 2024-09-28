lexer grammar mtSexpressionLexer;

FUNCTION_DEF                     : 'def';
VARIABLE_DEF                     : 'var';
VARIABLE_ASSIGN                  : 'set';

BOOLEAN_LITERAL                  : TRUE | FALSE;
INTEGER_LITERAL                  : Digit+;
STRING_LITERAL                   : ( '"'  (~["] | EscapeSequence)* '"'   );

LPAREN                           : '(';
RPAREN                           : ')';


SYMBOL                           : [A-Za-z0-9_\-+*=!<>/]+;

WS                               : [ \t\r\n\u000C]+ -> channel(HIDDEN);
SINGLE_LINE_COMMENT              : '//' ~[\r\n]* -> channel(HIDDEN);
MULTI_LINE_COMMENT               : '/*' .*? '*/' -> channel(HIDDEN);

fragment Digit                   : '0'..'9';
fragment EscapeSequence          : '\\' [btnfr"'\\] ;
fragment TRUE                    : 'true';
fragment FALSE                   : 'false';