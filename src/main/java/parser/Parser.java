package parser;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    public Parser(final List<Token> tokens){
        this.tokens = tokens;
    }

    public Token getToken(final int position) throws ParserException{
        if(position>= 0&& position<tokens.size()){
            return tokens.get(position);
        }else{
            throw new ParserException("Invalid Token position: "+ position);
        }
        
    }

    public ParseResult<Op> parseOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof PlusToken){
            return new ParseResult<Op>(new PlusOp(), position+1);
        } else if (token instanceof MinusToken){
            return new ParseResult<Op>(new MinusOp(), position+1);
        } else if (token instanceof EqualsToken){
            return new ParseResult<Op>(new EqualsOp(), position+1);
        }else{
            throw new ParserException("expected operator; received: "+token);
        }
    }
}
