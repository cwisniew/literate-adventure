lexer grammar mtSexpressionLexer;


INTEGER_LITERAL                  : Digit+;
STRING_LITERAL                   : ( '"'  (~["] | EscapeSequence)* '"'   );
SYMBOL                           : [A-Za-z0-9_\-+*=!<>/]+;

LPAREN                           : '(';
RPAREN                           : ')';



WS                               : [ \t\r\n\u000C]+ -> channel(HIDDEN);
SINGLE_LINE_COMMENT              : '//' ~[\r\n]* -> channel(HIDDEN);
MULTI_LINE_COMMENT               : '/*' .*? '*/' -> channel(HIDDEN);

fragment Digit                   : '0'..'9';
fragment EscapeSequence          : '\\' [btnfr"'\\] ;
