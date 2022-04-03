package parser;

import java.util.ArrayList;
import java.util.List;

import com.sun.jdi.IntegerType;
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
        System.out.println(position + " " + tokens.size());
        if(position>= 0&& position<tokens.size()){
            System.out.println("Token position: " + position);
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
        } else if(token instanceof StringValueToken){
            final String stringVal = ((StringValueToken)token).value;
            return new ParseResult<Exp>(new StringExp(stringVal), position+1);
        }else if(token instanceof TrueToken){
            final boolean value = true;
            return new ParseResult<Exp>(new BooleanLiteralExp(value),position+1);
        }else if(token instanceof FalseToken){
            final boolean value = false;
            return new ParseResult<Exp>(new BooleanLiteralExp(value), position+1);
        }
        else{
            throw new ParserException("expected a variable, string, boolean, expression, integer but received "+token);
        }
    }// parsePrimaryExp

    public ParseResult<Op> parseMultiplicativeOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof TimesToken){
            return new ParseResult<Op>(new MultiplicationOp(), position+1);
        } else if(token instanceof DivisionToken){
            return new ParseResult<Op>(new DivisionOp(), position+1);
        } else{
            throw new ParserException("expected * or /; received "+token);
        }
    }

    public ParseResult<Exp> parseMultiplicativeExp(final int position) throws ParserException{
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
        return current;
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
                final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
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

    public ParseResult<Exp> parseRelationExp(final int position) throws ParserException{
        ParseResult<Exp> current = parseAddidtiveExp(position);
        try {
            final ParseResult<Op> relationOp = parseRelationOp(current.position);
            final ParseResult<Exp> relationExp = parseAddidtiveExp(relationOp.position);
            return new ParseResult<Exp>(new OpExp(current.result, relationOp.result, relationExp.result), relationExp.position);
        }catch (final ParserException e){
            return current;
        }
    }

    public ParseResult<Op> parseEqualityOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof DoubleEqualsToken){
            return new ParseResult<Op>(new DoubleEqualsOp(), position+1);
        } else if(token instanceof NotEqualsToken){
            return new ParseResult<Op>(new NotEqualsOp(), position+1);
        }else{
            throw new ParserException("expected == or !=; received "+token);
        }
    }

    public ParseResult<Exp> parseEqualityExp(final int position) throws ParserException{
        ParseResult<Exp> current = parseRelationExp(position);
        try {
            final ParseResult<Op> equalityOp = parseEqualityOp(current.position);
            final ParseResult<Exp> equalityExp = parseRelationExp(equalityOp.position);
            return new ParseResult<Exp>(new OpExp(current.result, equalityOp.result, equalityExp.result), equalityExp.position);
        }catch (final ParserException e){
            return current;
        }
    }

    public ParseResult<Op> parseAssignmentOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof EqualsToken){
            return new ParseResult<Op>(new EqualsOp(), position+1);
        }else{
            throw new ParserException("expected =; received "+token);
        }
    }

    public ParseResult<Exp> parseAssignmentExp(final int position) throws ParserException{
        ParseResult<Exp> current = parseEqualityExp(position);
        try {
            final ParseResult<Op> assignmentOp = parseAssignmentOp(current.position);
            final ParseResult<Exp> assignmentExp = parseEqualityExp(assignmentOp.position);
            return new ParseResult<Exp>(new OpExp(current.result, assignmentOp.result, assignmentExp.result), assignmentExp.position);
        } catch(final ParserException e){
            return current;
        }
    }

    public ParseResult<Exp> parseExp(final int position) throws ParserException{
        return parseAssignmentExp(position);
    }

    /*
    if (exp) stmt else stmt | while (exp) stmt | break; | return exp;
    intg var; | bool var; | strg var;
    print(exp)
    { stmt* }
     */
    public ParseResult<Stmt> parseStmt(final int position) throws ParserException {
        final Token token = getToken(position);

        // if statement
        if (token instanceof IfToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> exp = parseEqualityExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenToken());
            final ParseResult<Stmt> trueBranch = parseStmt(exp.position + 1);
            assertTokenHereIs(trueBranch.position, new ElseToken());
            final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
            return new ParseResult<Stmt>(new IfStmt(exp.result, trueBranch.result, falseBranch.result),
                falseBranch.position);

            // while statement
        } else if (token instanceof WhileToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> exp = parseEqualityExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenToken());
            final ParseResult<Stmt> whileBranch = parseStmt(exp.position + 1);
            return new ParseResult<Stmt>(new WhileStmt(exp.result, whileBranch.result), whileBranch.position);

            // break statement
        } else if (token instanceof BreakToken) {
            assertTokenHereIs(position + 1, new SemiColonToken());
            return new ParseResult<Stmt>(new BreakStmt(), position + 2);

            // return statement
        } else if (token instanceof ReturnToken) {
            final ParseResult<Exp> exp = parseEqualityExp(position + 1);
            assertTokenHereIs(exp.position, new SemiColonToken());
            return new ParseResult<Stmt>(new ReturnStmt(exp.result), exp.position + 1);

            // var intg declaration
        } else if (token instanceof IntegerToken) {
            final ParseResult<Exp> exp = parsePrimaryExp(position + 1);

            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new IntType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected intg; received " + token);
            }

            // var bool declaration
        } else if (token instanceof BooleanToken) {
            final ParseResult<Exp> exp = parsePrimaryExp(position + 1);

            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new BooleanType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected bool; received " + token);
            }

            // var strg declaration
        } else if (token instanceof StringToken) {
            final ParseResult<Exp> exp = parsePrimaryExp(position + 1);

            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new StringType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected strg; received " + token);
            }

            // print statement
        } else if (token instanceof PrintToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> exp = parseAssignmentExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenToken());
            assertTokenHereIs(exp.position + 1, new SemiColonToken());
            return new ParseResult<Stmt>(new PrintStmt(exp.result), exp.position + 1);

            // 0 or more statements
        } else if (token instanceof LeftCurlyToken) {
            final List<Stmt> stmts = new ArrayList<>();
            int currentPosition = position + 1;
            boolean shouldRun = true;

            while (shouldRun) {
                try {
                    final ParseResult<Stmt> stmt = parseStmt(currentPosition);
                    stmts.add(stmt.result);
                    currentPosition = stmt.position + 1;
                } catch (final ParserException e) {
                    shouldRun = false;
                }
            }

            return new ParseResult<>(new BlockStmt(stmts), currentPosition + 1);

        } else {
            throw new ParserException("expected if or while or break or return or Int or Boolean or String " +
                "or print or {; received " + token);
        }

    }

    private Vardec getVardecStatement(Token token) {

        return null;
    }

/**
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
        } else {
            throw new ParserException("expected statement; received "+ token);
        }
    }*/
}
