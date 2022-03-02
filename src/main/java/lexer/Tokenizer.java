package lexer;

import lexer.tokens.*;

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
/*
    public Token tryTokenizeVariable() {
        String name = "";
    }
*/

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
            } else if (input.startsWith("if")) {
                offset += 2;
                return new IfToken();
            } else if (input.startsWith("(", offset)) {
                offset += 1;
                return new LeftParenToken();
            }  else if (input.startsWith(")", offset)) {
                offset += 1;
                return new RightParenToken();
            } else if (input.startsWith("{", offset)) {
                offset += 1;
                return new LeftCurlyToken();
            } else if (input.startsWith("}", offset)) {
                offset += 1;
                return new RightCurlyToken();
            } else if (input.startsWith("strg")){
                offset +=4;
                return new StringToken();
            } else if (input.startsWith("Boolean")){
                offset += 7;
                return new BooleanToken();
            } else if (input.startsWith("Int")){
                offset += 3;
                return new IntegerToken();
            } else if (input.startsWith("this")){
                offset += 4;
                return new ThisToken();
            } else if (input.startsWith("print")){
                offset += 5;
                return new PrintToken();
            } else if (input.startsWith("break")){
                offset += 5;
                return new BreakToken();
            } else if (input.startsWith(";")){
                offset += 1;
                return new SemiColonToken();
            } else if (input.startsWith("return")){
                offset += 6;
                return new ReturnToken();
            } else if (input.startsWith("new")){
                offset += 3;
                return new NewToken();
            } else if (input.startsWith("public")){
                offset += 6;
                return new PublicToken();
            } else if (input.startsWith("protected")){
                offset += 9;
                return new ProtectedToken();
            } else if (input.startsWith("private")){
                offset += 7;
                return new PrivateToken();
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