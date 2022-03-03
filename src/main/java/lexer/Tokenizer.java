package lexer;

import java.util.List;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

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
    // returns null if it fails to read in any variable  or keyword
    public Token tryTokenizeVariableOrKeyword() {
        skipWhiteSpace();
        // idea: read one character at a time 
        // when we are out of characters, check what the name is 
        // if the name is special (e.g., "true"), emit the special token for it (e.g., TrueToken)
        // if the name isn't special (e.g., "foo"), emit a variable token for it (e.g., VariableToken("foo"))
        // First character of the variable : letter
        // Every subsequent character: letter or a digit

        String name = "";
        if (offset<input.length() && Character.isLetter(input.charAt(offset))){
            name+=input.charAt(offset);
            offset++;
            while(offset<input.length()&& Character.isLetterOrDigit(input.charAt(offset))){
                name += input.charAt(offset);
                offset++;
            }

            // by this point, `name` holds a potential variable
            // `name` could be "true"
            if(name.equals("true")){
                return new TrueToken();
            } else if(name.equals("false")){
                return new FalseToken();
            }else if(name.equals("if")){
                return new IfToken();
            }else if (input.startsWith("strg")){
                return new StringToken();
            } else if (input.startsWith("Boolean")){
                return new BooleanToken();
            } else if (input.startsWith("Int")){
                return new IntegerToken();
            } else if (input.startsWith("this")){
                return new ThisToken();
            } else if (input.startsWith("print")){
                return new PrintToken();
            } else if (input.startsWith("break")){
                return new BreakToken();
            } else if (input.startsWith("return")){
                return new ReturnToken();
            } else if (input.startsWith("new")){
                return new NewToken();
            } else if (input.startsWith("public")){
                return new PublicToken();
            } else if (input.startsWith("protected")){
                return new ProtectedToken();
            } else if (input.startsWith("private")){
                return new PrivateToken();
            } else{
                return new VariableToken(name);
            }
        } else{
            return null;
        }
    }

    public void skipWhiteSpace() {
        while (offset < input.length() && Character.isWhitespace(input.charAt(offset))) {
            offset++;
        }
    }

    // returns null if there are no more tokens
    public Token tokenizeSingle() throws TokenizerException {
        Token retval = null;
        skipWhiteSpace();
        if (offset < input.length()) {
            retval = tryTokenizeVariableOrKeyword();
            if(retval==null){
                if (input.startsWith("(", offset)) {
                    offset += 1;
                    retval = new LeftParenToken();
                }  else if (input.startsWith(")", offset)) {
                    offset += 1;
                    retval =  new RightParenToken();
                } else if (input.startsWith("{", offset)) {
                    offset += 1;
                    retval = new LeftCurlyToken();
                } else if (input.startsWith("}", offset)) {
                    offset += 1;
                    retval = new RightCurlyToken();
                } else if (input.startsWith(";",offset)){
                    offset += 1;
                    retval = new SemiColonToken();
                } else {
                    throw new TokenizerException();
                }
            } 
        }
        return retval;
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