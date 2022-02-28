import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
    // valid program : truefalsetruefalsefalse
    // invalid: true true
    private final String input;
    private int offset;

    public Tokenizer(final String input) {
        this.input = input;
        offset = 0;
    }

    public void skipWhiteSpace() {
        while (offset < input.length() && Character.isWhitespace(input.charAt(offset))) {
            offset++;
        }
    }

    // returns null if there are no more tokens
    public Token tokenizeSingle() throws TokenizerException {
        skipWhiteSpace();
        if (offset < input.length()) {
            if (input.startsWith("true", offset)) {
                // TrueToken
                offset += 4;
                return new TrueToken();
            } else if (input.startsWith("false", offset)) {
                // FalseToken
                offset += 5;
                return new FalseToken();
            } else if (input.startsWith("(", offset)) {
                offset += 1;
                return new LeftParenToken();
            } else if (input.startsWith(")", offset)) {
                offset += 1;
                return new RightParenToken();
            } else if (input.startsWith("{", offset)) {
                offset += 1;
                return new LeftCurlyToken();
            } else if (input.startsWith("}", offset)) {
                offset += 1;
                return new RightCurlyToken();
            } else if (input.startsWith("if")) {
                offset += 2;
                return new IfToken();
            } else {
                throw new TokenizerException();
            }
        } else {
            return null;
        }
    }

    public List<Token> tokenize() throws TokenizerException {
        final List<Token> tokens = new ArrayList<Token>();
        Token token = tokenizeSingle();
        while (token != null) {
            tokens.add(token);
            token = tokenizeSingle();
        }
        return tokens;
    }
}