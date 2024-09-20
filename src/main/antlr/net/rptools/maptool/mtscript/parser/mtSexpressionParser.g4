parser grammar mtSexpressionParser;

options { tokenVocab=mtSexpressionLexer; }

sexpr : item+ EOF ;

item  : atom
      | list ;

atom : INTEGER_LITERAL | STRING_LITERAL | SYMBOL ;
list : LPAREN item* RPAREN ;

