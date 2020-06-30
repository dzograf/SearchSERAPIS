// Generated from ArgQLBNF.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ArgQLBNFLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, MATCH=15, RETURN=16, 
		ATTACK=17, REBUT=18, UNDERCUT=19, SUPPORT=20, ENDORSE=21, BACK=22, VARIABLE=23, 
		EMPTYSET=24, TIMESTAMP=25, AUTHOR=26, SCHEME=27, DOTS=28, TEXT=29, INT=30, 
		WS=31;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "MATCH", "RETURN", "ATTACK", 
			"REBUT", "UNDERCUT", "SUPPORT", "ENDORSE", "BACK", "VARIABLE", "EMPTYSET", 
			"TIMESTAMP", "AUTHOR", "SCHEME", "DOTS", "TEXT", "INT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','", "'<'", "'>'", "'['", "']'", "'/'", "'.'", "'!'", "'{'", 
			"'}'", "'('", "')'", "'*'", "'+'", null, null, null, null, null, null, 
			null, null, null, "'_'", null, null, null, "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "MATCH", "RETURN", "ATTACK", "REBUT", "UNDERCUT", "SUPPORT", 
			"ENDORSE", "BACK", "VARIABLE", "EMPTYSET", "TIMESTAMP", "AUTHOR", "SCHEME", 
			"DOTS", "TEXT", "INT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
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


	public ArgQLBNFLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ArgQLBNF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 30:
			WS_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			skip();
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2!\u00c2\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \3\2"+
		"\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3"+
		"\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\7\30\u0099"+
		"\n\30\f\30\16\30\u009c\13\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3"+
		"\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\36\3\36\7\36\u00b0\n\36\f\36"+
		"\16\36\u00b3\13\36\3\36\3\36\3\37\6\37\u00b8\n\37\r\37\16\37\u00b9\3 "+
		"\6 \u00bd\n \r \16 \u00be\3 \3 \3\u00b1\2!\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+"+
		"\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!\3\2\24\4\2OOoo\4\2CCc"+
		"c\4\2VVvv\4\2EEee\4\2JJjj\4\2TTtt\4\2GGgg\4\2WWww\4\2PPpp\4\2MMmm\4\2"+
		"DDdd\4\2FFff\4\2UUuu\4\2RRrr\4\2QQqq\4\2C\\c|\6\2\62;C\\aac|\5\2\13\f"+
		"\17\17\"\"\2\u00c5\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"+
		"\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\3A\3\2\2\2\5C\3\2\2\2\7E"+
		"\3\2\2\2\tG\3\2\2\2\13I\3\2\2\2\rK\3\2\2\2\17M\3\2\2\2\21O\3\2\2\2\23"+
		"Q\3\2\2\2\25S\3\2\2\2\27U\3\2\2\2\31W\3\2\2\2\33Y\3\2\2\2\35[\3\2\2\2"+
		"\37]\3\2\2\2!c\3\2\2\2#j\3\2\2\2%q\3\2\2\2\'w\3\2\2\2)\u0080\3\2\2\2+"+
		"\u0088\3\2\2\2-\u0090\3\2\2\2/\u0095\3\2\2\2\61\u009d\3\2\2\2\63\u009f"+
		"\3\2\2\2\65\u00a2\3\2\2\2\67\u00a7\3\2\2\29\u00ab\3\2\2\2;\u00ad\3\2\2"+
		"\2=\u00b7\3\2\2\2?\u00bc\3\2\2\2AB\7.\2\2B\4\3\2\2\2CD\7>\2\2D\6\3\2\2"+
		"\2EF\7@\2\2F\b\3\2\2\2GH\7]\2\2H\n\3\2\2\2IJ\7_\2\2J\f\3\2\2\2KL\7\61"+
		"\2\2L\16\3\2\2\2MN\7\60\2\2N\20\3\2\2\2OP\7#\2\2P\22\3\2\2\2QR\7}\2\2"+
		"R\24\3\2\2\2ST\7\177\2\2T\26\3\2\2\2UV\7*\2\2V\30\3\2\2\2WX\7+\2\2X\32"+
		"\3\2\2\2YZ\7,\2\2Z\34\3\2\2\2[\\\7-\2\2\\\36\3\2\2\2]^\t\2\2\2^_\t\3\2"+
		"\2_`\t\4\2\2`a\t\5\2\2ab\t\6\2\2b \3\2\2\2cd\t\7\2\2de\t\b\2\2ef\t\4\2"+
		"\2fg\t\t\2\2gh\t\7\2\2hi\t\n\2\2i\"\3\2\2\2jk\t\3\2\2kl\t\4\2\2lm\t\4"+
		"\2\2mn\t\3\2\2no\t\5\2\2op\t\13\2\2p$\3\2\2\2qr\t\7\2\2rs\t\b\2\2st\t"+
		"\f\2\2tu\t\t\2\2uv\t\4\2\2v&\3\2\2\2wx\t\t\2\2xy\t\n\2\2yz\t\r\2\2z{\t"+
		"\b\2\2{|\t\7\2\2|}\t\5\2\2}~\t\t\2\2~\177\t\4\2\2\177(\3\2\2\2\u0080\u0081"+
		"\t\16\2\2\u0081\u0082\t\t\2\2\u0082\u0083\t\17\2\2\u0083\u0084\t\17\2"+
		"\2\u0084\u0085\t\20\2\2\u0085\u0086\t\7\2\2\u0086\u0087\t\4\2\2\u0087"+
		"*\3\2\2\2\u0088\u0089\t\b\2\2\u0089\u008a\t\n\2\2\u008a\u008b\t\r\2\2"+
		"\u008b\u008c\t\20\2\2\u008c\u008d\t\7\2\2\u008d\u008e\t\16\2\2\u008e\u008f"+
		"\t\b\2\2\u008f,\3\2\2\2\u0090\u0091\t\f\2\2\u0091\u0092\t\3\2\2\u0092"+
		"\u0093\t\5\2\2\u0093\u0094\t\13\2\2\u0094.\3\2\2\2\u0095\u0096\7A\2\2"+
		"\u0096\u009a\t\21\2\2\u0097\u0099\t\22\2\2\u0098\u0097\3\2\2\2\u0099\u009c"+
		"\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\60\3\2\2\2\u009c"+
		"\u009a\3\2\2\2\u009d\u009e\7a\2\2\u009e\62\3\2\2\2\u009f\u00a0\t\4\2\2"+
		"\u00a0\u00a1\t\2\2\2\u00a1\64\3\2\2\2\u00a2\u00a3\t\3\2\2\u00a3\u00a4"+
		"\t\t\2\2\u00a4\u00a5\t\4\2\2\u00a5\u00a6\t\6\2\2\u00a6\66\3\2\2\2\u00a7"+
		"\u00a8\t\16\2\2\u00a8\u00a9\t\5\2\2\u00a9\u00aa\t\6\2\2\u00aa8\3\2\2\2"+
		"\u00ab\u00ac\7<\2\2\u00ac:\3\2\2\2\u00ad\u00b1\7$\2\2\u00ae\u00b0\13\2"+
		"\2\2\u00af\u00ae\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b1"+
		"\u00af\3\2\2\2\u00b2\u00b4\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4\u00b5\7$"+
		"\2\2\u00b5<\3\2\2\2\u00b6\u00b8\4\62;\2\u00b7\u00b6\3\2\2\2\u00b8\u00b9"+
		"\3\2\2\2\u00b9\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba>\3\2\2\2\u00bb"+
		"\u00bd\t\23\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00bc\3"+
		"\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c1\b \2\2\u00c1"+
		"@\3\2\2\2\7\2\u009a\u00b1\u00b9\u00be\3\3 \2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}