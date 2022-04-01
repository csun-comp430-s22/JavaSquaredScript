package parser;

import java.util.ArrayList;
import java.util.List;
import lexer.tokens.*;
import parser.BooleanLiteralExp;
import parser.DivisionOp;
import parser.GreaterThanOp;
import parser.LessThanOp;
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

    public void assertTokenHereIs(final int position, final Token expected) throws ParserException{
        final Token received = getToken(position);
        if(!expected.equals(received)){
            throw new ParserException("expected: "+ expected + "; received" + received);
        }
    }

    public ParseResult<Exp> parsePrimaryExp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof VariableToken){
            final String name = ((VariableToken)token).name;
            return new ParseResult<Exp>(new VariableExp(name), position + 1);
        } else if(token instanceof NumbersToken){
            final int value = ((NumbersToken)token).value;
            return new ParseResult<Exp>(new IntegerExp(value), position+1);
        } else if(token instanceof LeftParenToken){
            final ParseResult<Exp> inParens = parseExp(position+1);
            assertTokenHereIs(inParens.position, new RightParenToken());
            return new ParseResult<Exp>(inParens.result, inParens.position+1);
        } else if(token instanceof TrueToken|| token instanceof FalseToken){
            final boolean value = ((BooleanLiteralExp)token).value;
            return new ParseResult<Exp>(new BooleanLiteralExp(value), position+1);
        } else if(token instanceof StringValueToken){
            final String stringVal = ((StringExp)token).value;
            return new ParseResult<Exp>(new StringExp(stringVal), position+1);
        }else{
            throw new ParserException("expected a variable, string, boolean, expression, integer but received "+token);
        }
    }// parsePrimaryExp

    public ParseResult<Op> parseMultiplicativeOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof MultiplicationToken){
            return new ParseResult<Op>(new MultiplicationOp(), position+1);
        } else if(token instanceof DivisionToken){
            return new ParseResult<Op>(new DivisionOp(), position+1);
        } else{
            throw new ParserException("expected * or /; received "+token);
        }
    }

    public ParseResult<Exp> parseMultiplicativeExp(final int position){
        ParseResult<Exp> current = parsePrimaryExp(position); 
        boolean shouldRun = true;
        while(shouldRun){
            try {
                final ParseResult<Op> multiplicativeOp = parseMultiplicativeOp(current.position);
                final ParseResult<Exp> multiplicativeExp = parsePrimaryExp(multiplicativeOp.position);
                current = new ParseResult<Exp>(new OpExp(current.result,multiplicativeOp.result,multiplicativeExp.result), multiplicativeExp.position);
            } catch (final ParserException e) {
                shouldRun = false;
            }
        }
    }

    public ParseResult<Op> parseAdditiveOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof PlusToken){
            return new ParseResult<Op>(new PlusOp(), position+1);
        } else if (token instanceof MinusToken){
            return new ParseResult<Op>(new MinusOp(), position+1);
        } else{
            throw new ParserException("expected + or -; received "+token);
        }
    }

    public ParseResult<Exp> parseAddidtiveExp(final int position) throws ParserException{
        ParseResult<Exp> current = parseMultiplicativeExp(position);
        boolean shouldRun = true;
        while(shouldRun){
            try{
                final ParseResult<Op> additiveOp = parseMultiplicativeOp(current.position);
                final ParseResult<Exp> anotherPrimary =  parseMultiplicativeExp(additiveOp.position);
                current = new ParseResult<Exp>(new OpExp(current.result, additiveOp.result,anotherPrimary.result), anotherPrimary.position);
            }catch(final ParserException e){
                shouldRun = false;
            }
        }
        return current;
    } // parseAdditiveExp

    public ParseResult<Op> parseRelationOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof LessThanToken){
            return new ParseResult<Op>(new LessThanOp(), position+1);
        } else if(token instanceof GreaterThanToken){
            return new ParseResult<Op>(new GreaterThanOp(), position+1);
        }else{
            throw new ParserException("expected < or >; received "+token);
        }
    }



    public ParseResult<Stmt> parseStmt(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof IfToken){
            assertTokenHereIs(position+1, new LeftParenToken());
            final ParseResult<Exp> guard = parseExp(position+2);
            assertTokenHereIs(guard.position, new RightParenToken());
            final ParseResult<Stmt> trueBranch = parseStmt(guard.position+1);
            assertTokenHereIs(trueBranch.position, new ElseToken());
            final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position+1);
            return new ParseResult<Stmt>(new IfStmt(guard.result, trueBranch.result, falseBranch.result),
                falseBranch.position);
        } else if(token instanceof LeftCurlyToken){
            final List<Stmt> stmts = new ArrayList<Stmt>();
            int curPosition = position +1;
            boolean shouldRun = true;
            while(shouldRun){
                try {
                    final ParseResult<Stmt> stmt = parseStmt(curPosition);
                    stmts.add(stmt.result);
                    curPosition = stmt.position;
                } catch (final ParserException e) {
                    shouldRun = false;
                }
            }
            return new ParseResult<Stmt>(new BlockStmt(stmts), curPosition);
        /* } else if (token instanceof PrintToken){
            assertTokenHereIs(position+1, new LeftParenToken());
            final ParseResult<Exp> exp = parseExp(position+2);
            assertTokenHereIs(exp.position, new RightParenToken());
            assertTokenHereIs(exp.position+1, new SemiColonToken());
            return new ParseResult<Stmt>(new PrintStmt(exp.result), exp.position+2);
        } else { */
            throw new ParserException("expected statement; received "+ token);
        }
    }

    /* public ParseResult<Op> parseOp(final int position) throws ParserException{
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
    } */
}
