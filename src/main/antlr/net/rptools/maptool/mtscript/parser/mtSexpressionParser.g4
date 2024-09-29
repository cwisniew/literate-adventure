parser grammar mtSexpressionParser;

options { tokenVocab=mtSexpressionLexer; }

sexpr : item+ EOF ;

item  : atom
      | list ;

atom  : FUNCTION_DEF
      | VARIABLE_DEF
      | VARIABLE_ASSIGN
      | VARIABLE_DEF
      | BOOLEAN_LITERAL
      | INTEGER_LITERAL
      | STRING_LITERAL
      | SYMBOL  ;

variable_def : VARIABLE_DEF symbol_def=SYMBOL item ;

list : LPAREN item* RPAREN ;

