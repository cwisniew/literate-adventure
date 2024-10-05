parser grammar mtSexpressionParser;

options { tokenVocab=mtSexpressionLexer; }

sexpr          : item+ EOF
               ;

item           : atom
               | list
               | defineFunction
               ;

atom           : VARIABLE_DEF
               | VARIABLE_ASSIGN
               | VARIABLE_DEF
               | BOOLEAN_LITERAL
               | INTEGER_LITERAL
               | STRING_LITERAL
               | SYMBOL
               ;

defineFunction : FUNCTION_DEF functionName=SYMBOL LPAREN paramList RPAREN body=list
               ;

paramList      : SYMBOL*
               ;

list           : LPAREN item* RPAREN
               ;

