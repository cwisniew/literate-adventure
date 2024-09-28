parser grammar mtSexpressionParser;

options { tokenVocab=mtSexpressionLexer; }

sexpr : item+ EOF ;

item  : atom
      | list ;

atom  : FUNCTION_DEF
      | VARIABLE_DEF
      | VARIABLE_ASSIGN
      | BOOLEAN_LITERAL
      | INTEGER_LITERAL
      | STRING_LITERAL
      | SYMBOL  ;

list : LPAREN item* RPAREN ;

