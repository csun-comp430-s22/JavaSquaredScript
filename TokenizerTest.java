import java.util.List;

public class TokenizerTest {
    public static void testEmptyString() throws TokenizerException {
        // Test # 1
        // check that tokenizing empty string works
        Tokenizer tokenizer = new Tokenizer("");
        List<Token> tokens = tokenizer.tokenize();
        assert (tokens.size() == 0);
    }

    public static void testOnlyWhitespace() throws TokenizerException {
        // Test #2 (checking whitespace)
        Tokenizer tokenizer = new Tokenizer("   ");
        List<Token> tokens = tokenizer.tokenize();
        assert (tokens.size() == 0);
    }

    public static void testTrueByItself() throws TokenizerException {
        // Test #3 (checking true token)
        Tokenizer tokenizer = new Tokenizer("true");
        List<Token> tokens = tokenizer.tokenize();
        assert (tokens.size() == 1);
        Token trueToken = tokens.get(0);
        assert (trueToken instanceof TrueToken);
    }

    // Test-driven development : write tests first
    // 1. TokenizerTest. Compile and run.
    // 2. Tokens/Tokenizer

    public static void main(String[] args) throws TokenizerException {
        testOnlyWhitespace();
        testEmptyString();
        testTrueByItself();
    }
}