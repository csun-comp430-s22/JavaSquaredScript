package parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

import com.sun.jdi.IntegerType;

import org.omg.IOP.TAG_ORB_TYPE;

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
        List<Exp> exp = new ArrayList<>();
        if(token instanceof VariableToken){
            int counter =0;
            try{
                counter++;
                assertTokenHereIs(position+1, new LeftParenToken());
                counter++;
                final String name = ((VariableToken)token).name;
                boolean shouldRun = true;
                while(shouldRun){
                    shouldRun = false;
                    try{
                        assertTokenHereIs(position+counter, new RightParenToken());
                    }catch(final ParserException e){
                        shouldRun=true;
                        exp.add(parseExp(position+counter).result);
                        counter++;
                        try{
                            assertTokenHereIs(position+counter, new CommaToken());
                            counter++;
                        }catch(final ParserException exception){
                        }  
                    }
                }
                counter++;
                return new ParseResult<Exp>(new FunctionCallExp(new FunctionName(name),exp), position+counter);
                //return new ParseResult<Exp>(new FunctionCallExp(new FunctionName(name),)), position)
            }catch(final ParserException e){
                final String name = ((VariableToken)token).name;
                return new ParseResult<Exp>(new VariableExp(name), position + 1);
            }
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
        } else if(token instanceof NewToken){
            if(getToken(position+1) instanceof VariableToken && getToken(position+2) instanceof LeftParenToken){
                final String name = ((VariableToken)getToken(position+1)).name;
                int counter = 3;
                boolean shouldRun = true;
                while(shouldRun){
                    shouldRun = false;
                    try{
                        assertTokenHereIs(position+counter, new RightParenToken());
                    }catch(final ParserException e){
                        shouldRun=true;
                        exp.add(parseExp(position+counter).result);
                        counter++;
                        try{
                            assertTokenHereIs(position+counter, new CommaToken());
                            counter++;
                        }catch(final ParserException exception){
                        }  
                    }
                }
                counter++;
                return new ParseResult<Exp>(new ClassCallExp(new ClassName(name),exp), position+counter);
            }else{
                throw new ParserException("expected new token; received: "+token);
            }
        }else{
            throw new ParserException("expected a variable, string, boolean, expression, integer but received "+token);
        }
    }// parsePrimaryExp

    public ParseResult<Op> parsePeriodOp(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof PeriodToken){
            return new ParseResult<Op>(new PeriodOp(), position+1);
        }
        else{
            throw new ParserException("expected .; received "+token);
        }
    }

    public ParseResult<Exp> parsePeriodExp(final int position) throws ParserException{
        ParseResult<Exp> current = parsePrimaryExp(position);
        boolean shouldRun = true;
        while(shouldRun){
            try{
                final ParseResult<Op> periodOp = parsePeriodOp(current.position);
                final ParseResult<Exp> periodExp = parsePrimaryExp(periodOp.position);
                current = new ParseResult<Exp>(new OpExp(current.result, periodOp.result, periodExp.result), periodExp.position);
            }catch(final ParserException e){
                shouldRun = false;
            }
        }
        return current;
    }

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
        ParseResult<Exp> current = parsePeriodExp(position); 
        boolean shouldRun = true;
        while(shouldRun){
            try {
                final ParseResult<Op> multiplicativeOp = parseMultiplicativeOp(current.position);
                final ParseResult<Exp> multiplicativeExp = parsePeriodExp(multiplicativeOp.position);
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
            final ParseResult<Exp> exp = parseExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenToken());
            final ParseResult<Stmt> trueBranch = parseStmt(exp.position + 1);
            assertTokenHereIs(trueBranch.position, new ElseToken());
            final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
            return new ParseResult<Stmt>(new IfStmt(exp.result, trueBranch.result, falseBranch.result),
                falseBranch.position);

            // while statement
        } else if (token instanceof WhileToken) {
            assertTokenHereIs(position + 1, new LeftParenToken());
            final ParseResult<Exp> exp = parseExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenToken());
            final ParseResult<Stmt> whileBranch = parseStmt(exp.position + 1);
            return new ParseResult<Stmt>(new WhileStmt(exp.result, whileBranch.result), whileBranch.position);

            // break statement
        } else if (token instanceof BreakToken) {
            assertTokenHereIs(position + 1, new SemiColonToken());
            return new ParseResult<Stmt>(new BreakStmt(), position + 2);

            // return statement
        } else if (token instanceof ReturnToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);
            assertTokenHereIs(exp.position, new SemiColonToken());
            return new ParseResult<Stmt>(new ReturnStmt(exp.result), exp.position + 1);

            // var intg declaration
        } else if (token instanceof IntegerToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);

            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new IntType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected intg; received " + token);
            }

            // var bool declaration
        } else if (token instanceof BooleanToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);

            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new BooleanType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected bool; received " + token);
            }

            // var strg declaration
        } else if (token instanceof StringToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);

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
            final ParseResult<Exp> exp = parseExp(position + 2);
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
    //instanceDec ::= accessMod varDec`;`
    public ParseResult<Stmt> parseInstanceDec(final int position){
        final Token token = getToken(position);
        if(token instanceof PublicToken || token instanceof PrivateToken || token instanceof ProtectedToken){
            ParseResult<Stmt> stmt = parseStmt(position+1);
            assertTokenHereIs(stmt.position, new SemiColonToken());
            // needs to return the instanceDecStmt
        }
    }

    // methodDef::= accessMod type methodName(exp*) stmt
    public ParseResult<Stmt> parseMethodDef(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof PublicToken || token instanceof PrivateToken || token instanceof ProtectedToken){
            //TODO: need to continue
            if(getToken(position+1) instanceof IntegerToken|| getToken(position+1) instanceof BooleanToken||getToken(position+1) instanceof StringToken){
                ParseResult<Exp> exp = parseExp(position+2);
                ParseResult<Stmt> stmt = parseStmt(exp.position);
                //needs a return the methodDef
            }
        }
    }

    publicnParseResult<Stmt> parseConstructor(final int position) throws ParserException{
        final Token token= getToken(position);
        //ConstructorStmt(List<VarDec>)
    }

    // classDef::= class classname extends classname{ instancedec* methodDef* constructor(varDec*) stmt}
    // new ParserResult<Stmt>(new ClassDef(new ClassName(classname),new ClassName(extendsclassname), List<InstanceDec>, List<MethodDef>, new ConstructorStmt(List<varDec>), stmt.result),stmt.position);
    public ParseResult<Stmt> parseClassDef(final int position) throws ParserException{
        final Token token = getToken(position);
        if(token instanceof ClassToken){
            //TODO: need to continue
            if(getToken(position+1) instanceof VariableToken){
                final String className = ((VariableToken)getToken(position+1)).name;
                if(getToken(position+2) instanceof ExtendsToken){
                    final String extendsClassName = ((VariableToken)getToken(position+3)).name;
                    assertTokenHereIs(position+4, new LeftCurlyToken());
                    position = 4;
                    boolean shouldRun = true;
                    while(true){
                        try{
                            ParseResult<Stmt> constructor = parseConstructor(position+1);
                        }catch(final ParserException e){
                            
                        }
                    }
                }
            } else{
                throw new ParserException("expected classname; received "+getToken(position+1));
            }
        }else{
            throw new ParserException("expected class token; received "+token);
        }

    }

    // program::= classDef*
    public ParseResult<Program> parseProgram(final int position) throws ParserException{
        final ParseResult<Stmt> stmt = parseClassDef(position);
        return new ParseResult<Program>(new Program(stmt.result),stmt.position);
    }

    public Program parseProgram() throws ParserException{
        final ParseResult<Program> program = parseProgram(0);
        if(program.position == tokens.size()){
            return program.result;
        }else{
            throw new ParserException("Remaining tokens at end");
        }
    }
}
