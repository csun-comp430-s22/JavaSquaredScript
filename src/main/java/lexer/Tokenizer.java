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
            }else if (name.equals("strg")){
                return new StringToken();
            } else if (name.equals("Boolean")){
                return new BooleanToken();
            } else if (name.equals("Int")){
                return new IntegerToken();
            } else if (name.equals("this")){
                return new ThisToken();
            } else if (name.equals("print")){
                return new PrintToken();
            } else if (name.equals("break")){
                return new BreakToken();
            } else if (name.equals("return")){
                return new ReturnToken();
            } else if (name.equals("new")){
                return new NewToken();
            } else if (name.equals("public")){
                return new PublicToken();
            } else if (name.equals("protected")){
                return new ProtectedToken();
            } else if (name.equals("private")){
                return new PrivateToken();
            } else if (name.equals("while")){
                return new WhileToken();
            }else{
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

    public Token tryTokenizeSymbol(){
        skipWhiteSpace();
        Token retval = null;
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
        } else if (input.startsWith("+",offset)){
            offset += 1;
            retval = new PlusToken();
        } else if (input.startsWith("-",offset)){
            offset += 1;
            retval = new MinusToken();
        } else if (input.startsWith("*",offset)){
            offset += 1;
            retval = new TimesToken();
        } else if (input.startsWith("/",offset)){
            offset += 1;
            retval = new DivisionToken();
        } else if (input.startsWith(">",offset)){
            offset += 1;
            retval = new GreaterThanToken();
        } else if (input.startsWith("<",offset)){
            offset += 1;
            retval = new LessThanToken();
        }  else if (input.startsWith("==",offset)){
            offset += 2;
            retval = new DoubleEqualsToken();
        } else if (input.startsWith("=",offset)){
            offset += 1;
            retval = new EqualsToken();
        } else if (input.startsWith("!=",offset)){
            offset += 2;
            retval = new NotEqualsToken();
        } 
        return retval;
    }

    public StringValueToken tryTokenizeStringValue(){
        skipWhiteSpace();
        String stringVal = "";
        if(offset<input.length() && Character.compare(input.charAt(offset), '\"')==0){
            stringVal+=input.charAt(offset);
            offset++;
            while(offset<input.length() && Character.compare(input.charAt(offset), '\"')!=0){
                stringVal+=input.charAt(offset);
                offset++;
            }
            if(Character.compare(input.charAt(offset),'\"')==0){
                stringVal+=input.charAt(offset);
                offset++;
                return new StringValueToken(stringVal);
            }else{
                return null;
            }
        }else{
            return null;
        } 
    }

    public NumbersToken tryTokenizeNumbers(){
        skipWhiteSpace();
        String number = "";

        while(offset<input.length() && Character.isDigit(input.charAt(offset))){
            number+=input.charAt(offset);
            offset++;
        }
        if(number.length()>0){
            return  new NumbersToken(Integer.parseInt(number));
        }else{
            return null;
        }
    }

    // returns null if there are no more tokens
    public Token tokenizeSingle() throws TokenizerException {
        Token retval = null;
        skipWhiteSpace();
        if(offset<input.length() && 
        (retval=tryTokenizeStringValue())==null&&
        (retval=tryTokenizeVariableOrKeyword())==null &&
        (retval=tryTokenizeNumbers())==null && 
        (retval=tryTokenizeSymbol())==null){
            throw new TokenizerException();
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