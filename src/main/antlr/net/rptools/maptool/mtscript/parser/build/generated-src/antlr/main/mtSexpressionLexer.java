/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({
  "all",
  "warnings",
  "unchecked",
  "unused",
  "cast",
  "CheckReturnValue",
  "this-escape"
})
public class mtSexpressionLexer extends Lexer {
  static {
    RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION);
  }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  public static final int FUNCTION_DEF = 1,
      VARIABLE_DEF = 2,
      VARIABLE_ASSIGN = 3,
      BOOLEAN_LITERAL = 4,
      INTEGER_LITERAL = 5,
      STRING_LITERAL = 6,
      LPAREN = 7,
      RPAREN = 8,
      SYMBOL = 9,
      WS = 10,
      SINGLE_LINE_COMMENT = 11,
      MULTI_LINE_COMMENT = 12;
  public static String[] channelNames = {"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};

  public static String[] modeNames = {"DEFAULT_MODE"};

  private static String[] makeRuleNames() {
    return new String[] {
      "FUNCTION_DEF",
      "VARIABLE_DEF",
      "VARIABLE_ASSIGN",
      "BOOLEAN_LITERAL",
      "INTEGER_LITERAL",
      "STRING_LITERAL",
      "LPAREN",
      "RPAREN",
      "SYMBOL",
      "WS",
      "SINGLE_LINE_COMMENT",
      "MULTI_LINE_COMMENT",
      "Digit",
      "EscapeSequence",
      "TRUE",
      "FALSE"
    };
  }

  public static final String[] ruleNames = makeRuleNames();

  private static String[] makeLiteralNames() {
    return new String[] {null, "'def'", "'var'", "'set'", null, null, null, "'('", "')'"};
  }

  private static final String[] _LITERAL_NAMES = makeLiteralNames();

  private static String[] makeSymbolicNames() {
    return new String[] {
      null,
      "FUNCTION_DEF",
      "VARIABLE_DEF",
      "VARIABLE_ASSIGN",
      "BOOLEAN_LITERAL",
      "INTEGER_LITERAL",
      "STRING_LITERAL",
      "LPAREN",
      "RPAREN",
      "SYMBOL",
      "WS",
      "SINGLE_LINE_COMMENT",
      "MULTI_LINE_COMMENT"
    };
  }

  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override
  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  public mtSexpressionLexer(CharStream input) {
    super(input);
    _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  @Override
  public String getGrammarFileName() {
    return "mtSexpressionLexer.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @Override
  public String[] getChannelNames() {
    return channelNames;
  }

  @Override
  public String[] getModeNames() {
    return modeNames;
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public static final String _serializedATN =
      "\u0004\u0000\fy\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"
          + "\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"
          + "\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"
          + "\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"
          + "\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"
          + "\u000f\u0007\u000f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"
          + "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"
          + "\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0003\u00030\b\u0003\u0001"
          + "\u0004\u0004\u00043\b\u0004\u000b\u0004\f\u00044\u0001\u0005\u0001\u0005"
          + "\u0001\u0005\u0005\u0005:\b\u0005\n\u0005\f\u0005=\t\u0005\u0001\u0005"
          + "\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0004"
          + "\bF\b\b\u000b\b\f\bG\u0001\t\u0004\tK\b\t\u000b\t\f\tL\u0001\t\u0001\t"
          + "\u0001\n\u0001\n\u0001\n\u0001\n\u0005\nU\b\n\n\n\f\nX\t\n\u0001\n\u0001"
          + "\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b`\b\u000b"
          + "\n\u000b\f\u000bc\t\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"
          + "\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001"
          + "\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"
          + "\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001a\u0000\u0010\u0001\u0001"
          + "\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f"
          + "\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\u0000\u001b\u0000\u001d\u0000"
          + "\u001f\u0000\u0001\u0000\u0005\u0001\u0000\"\"\b\u0000!!*+--/9<>AZ__a"
          + "z\u0003\u0000\t\n\f\r  \u0002\u0000\n\n\r\r\b\u0000\"\"\'\'\\\\bbffnn"
          + "rrtt|\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000"
          + "\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000"
          + "\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000"
          + "\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000"
          + "\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000"
          + "\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0001"
          + "!\u0001\u0000\u0000\u0000\u0003%\u0001\u0000\u0000\u0000\u0005)\u0001"
          + "\u0000\u0000\u0000\u0007/\u0001\u0000\u0000\u0000\t2\u0001\u0000\u0000"
          + "\u0000\u000b6\u0001\u0000\u0000\u0000\r@\u0001\u0000\u0000\u0000\u000f"
          + "B\u0001\u0000\u0000\u0000\u0011E\u0001\u0000\u0000\u0000\u0013J\u0001"
          + "\u0000\u0000\u0000\u0015P\u0001\u0000\u0000\u0000\u0017[\u0001\u0000\u0000"
          + "\u0000\u0019i\u0001\u0000\u0000\u0000\u001bk\u0001\u0000\u0000\u0000\u001d"
          + "n\u0001\u0000\u0000\u0000\u001fs\u0001\u0000\u0000\u0000!\"\u0005d\u0000"
          + "\u0000\"#\u0005e\u0000\u0000#$\u0005f\u0000\u0000$\u0002\u0001\u0000\u0000"
          + "\u0000%&\u0005v\u0000\u0000&\'\u0005a\u0000\u0000\'(\u0005r\u0000\u0000"
          + "(\u0004\u0001\u0000\u0000\u0000)*\u0005s\u0000\u0000*+\u0005e\u0000\u0000"
          + "+,\u0005t\u0000\u0000,\u0006\u0001\u0000\u0000\u0000-0\u0003\u001d\u000e"
          + "\u0000.0\u0003\u001f\u000f\u0000/-\u0001\u0000\u0000\u0000/.\u0001\u0000"
          + "\u0000\u00000\b\u0001\u0000\u0000\u000013\u0003\u0019\f\u000021\u0001"
          + "\u0000\u0000\u000034\u0001\u0000\u0000\u000042\u0001\u0000\u0000\u0000"
          + "45\u0001\u0000\u0000\u00005\n\u0001\u0000\u0000\u00006;\u0005\"\u0000"
          + "\u00007:\b\u0000\u0000\u00008:\u0003\u001b\r\u000097\u0001\u0000\u0000"
          + "\u000098\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000\u0000;9\u0001\u0000"
          + "\u0000\u0000;<\u0001\u0000\u0000\u0000<>\u0001\u0000\u0000\u0000=;\u0001"
          + "\u0000\u0000\u0000>?\u0005\"\u0000\u0000?\f\u0001\u0000\u0000\u0000@A"
          + "\u0005(\u0000\u0000A\u000e\u0001\u0000\u0000\u0000BC\u0005)\u0000\u0000"
          + "C\u0010\u0001\u0000\u0000\u0000DF\u0007\u0001\u0000\u0000ED\u0001\u0000"
          + "\u0000\u0000FG\u0001\u0000\u0000\u0000GE\u0001\u0000\u0000\u0000GH\u0001"
          + "\u0000\u0000\u0000H\u0012\u0001\u0000\u0000\u0000IK\u0007\u0002\u0000"
          + "\u0000JI\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000LJ\u0001\u0000"
          + "\u0000\u0000LM\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000\u0000NO\u0006"
          + "\t\u0000\u0000O\u0014\u0001\u0000\u0000\u0000PQ\u0005/\u0000\u0000QR\u0005"
          + "/\u0000\u0000RV\u0001\u0000\u0000\u0000SU\b\u0003\u0000\u0000TS\u0001"
          + "\u0000\u0000\u0000UX\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000"
          + "VW\u0001\u0000\u0000\u0000WY\u0001\u0000\u0000\u0000XV\u0001\u0000\u0000"
          + "\u0000YZ\u0006\n\u0000\u0000Z\u0016\u0001\u0000\u0000\u0000[\\\u0005/"
          + "\u0000\u0000\\]\u0005*\u0000\u0000]a\u0001\u0000\u0000\u0000^`\t\u0000"
          + "\u0000\u0000_^\u0001\u0000\u0000\u0000`c\u0001\u0000\u0000\u0000ab\u0001"
          + "\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000bd\u0001\u0000\u0000\u0000"
          + "ca\u0001\u0000\u0000\u0000de\u0005*\u0000\u0000ef\u0005/\u0000\u0000f"
          + "g\u0001\u0000\u0000\u0000gh\u0006\u000b\u0000\u0000h\u0018\u0001\u0000"
          + "\u0000\u0000ij\u000209\u0000j\u001a\u0001\u0000\u0000\u0000kl\u0005\\"
          + "\u0000\u0000lm\u0007\u0004\u0000\u0000m\u001c\u0001\u0000\u0000\u0000"
          + "no\u0005t\u0000\u0000op\u0005r\u0000\u0000pq\u0005u\u0000\u0000qr\u0005"
          + "e\u0000\u0000r\u001e\u0001\u0000\u0000\u0000st\u0005f\u0000\u0000tu\u0005"
          + "a\u0000\u0000uv\u0005l\u0000\u0000vw\u0005s\u0000\u0000wx\u0005e\u0000"
          + "\u0000x \u0001\u0000\u0000\u0000\t\u0000/49;GLVa\u0001\u0000\u0001\u0000";
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
