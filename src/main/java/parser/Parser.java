package parser;

import java.util.List;
import lexer.tokens.*;
import parser.ParseResult;
import parser.ParserException;

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

    // public ParseResult<Stmt> parseStmt(final int position) throws ParserException{

    // }

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

    public ParseResult<Exp> parseExp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof VariableToken){
            final String name = ((VariableToken)token).name;
            return new ParseResult<Exp>(new VariableExp(name), position + 1);
        } else if(token instanceof IntegerToken){
            final int value = ((NumbersToken)token).value;
            return new ParseResult<Exp>(new IntegerExp(value), position+1);
        } else{
            final ParseResult<Exp> left = parseExp(position);
            final ParseResult<Op> op = parseOp(left.position);
            final ParseResult<Exp> right = parseExp(op.position);
            return new ParseResult<Exp>(new OpExp(left.result, op.result, right.result), right.position);
        }
    }
}
