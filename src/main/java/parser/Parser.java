package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lexer.tokens.*;
import parser.BooleanLiteralExp;
import parser.BooleanType;
import parser.DivisionOp;
import parser.GreaterThanOp;
import parser.IntType;
import parser.LessThanOp;
import parser.ParseResult;
import parser.ParserException;
import parser.PrivateModifier;
import parser.ProtectedModifier;
import parser.PublicModifier;
import parser.StringType;

public class Parser {
    private final List<Token> tokens;
    public Parser(final List<Token> tokens){
        this.tokens = tokens;
    }

    public Token getToken(final int position) throws ParserException{
        if(position>= 0&& position<tokens.size()){
            //System.out.println("Position: " + position + " | " + "Token: " + tokens.get(position));
            return tokens.get(position);
        }else{
            throw new ParserException("Invalid Token position: "+ position);
        }
    }

    public void assertTokenHereIs(final int position, final Token expected) throws ParserException{
        final Token received = getToken(position);
        if(!expected.equals(received)){
            throw new ParserException("expected: "+ expected + "; received " + received);
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
                        System.out.print("TOKEN IS: "+getToken(position+counter));
                        assertTokenHereIs(position+counter, new RightParenToken());
                    }catch(final ParserException e){
                        shouldRun=true;
                        exp.add(parseExp(position+counter).result);
                        counter++;
                        try{
                            System.out.println("TOKEN: "+getToken(position+counter));
                            assertTokenHereIs(position+counter, new CommaToken());
                            counter++;
                        }catch(final ParserException exception){
                        }
                    }
                }
                counter++;
                return new ParseResult<Exp>(new FunctionCallExp(new FunctionName(name),exp), position+counter);
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

            // break statement::= break;
        } else if (token instanceof BreakToken) {
            assertTokenHereIs(position + 1, new SemiColonToken());
            return new ParseResult<Stmt>(new BreakStmt(), position + 2);
            // return statement
        } else if (token instanceof ReturnToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);
            assertTokenHereIs(exp.position, new SemiColonToken());
            return new ParseResult<Stmt>(new ReturnStmt(exp.result), exp.position + 1);

            // var intg declaration::= int var;
        } else if (token instanceof IntegerToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);
            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new IntType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected intg; received " + token);
            }

            // var bool declaration: bool var;
        } else if (token instanceof BooleanToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);

            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new BooleanType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected bool; received " + token);
            }

            // var strg declaration::= strg var;
        } else if (token instanceof StringToken) {
            final ParseResult<Exp> exp = parseExp(position + 1);

            if (getToken(exp.position - 1) instanceof VariableToken) {
                assertTokenHereIs(exp.position, new SemiColonToken());
                return new ParseResult<Stmt>(new Vardec(new StringType(), (VariableExp) exp.result),
                    exp.position + 1);
            } else {
                throw new ParserException("expected strg; received " + token);
            }

            // print statement::= print(exp);
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

    public ParseResult<AccessType> parseAccessType(final int position) throws ParserException {
        Token token = getToken(position);

        if (token instanceof PrivateToken) {
            return new ParseResult<>(new PrivateType(), position + 1);
        } else if (token instanceof PublicToken) {
            return new ParseResult<>(new PublicType(), position + 1);
        } else if (token instanceof ProtectedToken) {
            return new ParseResult<>(new ProtectedType(), position + 1);
        } else {
            throw new ParserException("expected private or public or protected; received " + token);
        }
    }

    public ParseResult<Type> parseReturnType(final int position) throws ParserException {
        Token token = getToken(position);

        if (token instanceof IntegerToken) {
            return new ParseResult<>(new IntType(), position + 1);
        } else if (token instanceof BooleanToken) {
            return new ParseResult<>(new BooleanType(), position + 1);
        } else if (token instanceof StringToken) {
            return new ParseResult<>(new StringType(), position + 1);
        } else {
            throw new ParserException("expected Int or Boolean or String; received " + token);
        }
    }
    public ParseResult<List<Vardec>> parseMethodParameters(final int position) throws ParserException {
        List<Vardec> vardecs = new ArrayList<>();
        int counter = position;
        boolean shouldRun = true;

        while (shouldRun) {
            try{
                ParseResult<Type> type = parseReturnType(counter);

                if (getToken(type.position) instanceof VariableToken) {
                    vardecs.add(new Vardec(type.result,
                        new VariableExp(((VariableToken) getToken(type.position)).name)));
                    counter++;
                    try {
                        System.out.println("Entered Here.");
                        assertTokenHereIs(type.position + 1, new CommaToken());
                        counter+=2;
                    } catch (final ParserException e) {
                        shouldRun = false;
                    }
                } else {
                    throw new ParserException("expected variable; received " + getToken(type.position));
                }
            }catch(final ParserException e){
                shouldRun = false;
                return new ParseResult<>(vardecs, counter);
            }
        }

        return new ParseResult<>(vardecs, counter+1);
    }

    public ParseResult<List<ClassDef>> parseClasses(final int position) throws ParserException {
        List<ClassDef> classDefs = new ArrayList<>();
        List<MethodDef> methodDefs = new ArrayList<>();
        List<ConstructorDef> constructorDefs = new ArrayList<>();
        List<InstanceDec> instanceDecs = new ArrayList<>();
        final Token token = getToken(position);
        String extendsName = "", className = "";
        int currentPosition = position;
        boolean shouldRun = true;

        while (shouldRun) {
            if (token instanceof ClassToken) {
                if (getToken(currentPosition + 1) instanceof VariableToken) {
                    className = ((VariableToken) getToken(currentPosition + 1)).name;
                    //System.out.println("Token: "+getToken(currentPosition+2).toString());
                    if (getToken(currentPosition + 2) instanceof ExtendsToken) {
                        if (getToken(currentPosition + 3) instanceof VariableToken) {
                            extendsName = ((VariableToken) getToken(currentPosition + 3)).name;
                            currentPosition += 4;
                        } else {
                            throw new ParserException("expected extend name; received " + getToken(currentPosition + 3));
                        }
                    } else { 
                        currentPosition += 2;
                    }
                    assertTokenHereIs(currentPosition, new LeftCurlyToken());
                    currentPosition++;

                    while (!(getToken(currentPosition) instanceof RightCurlyToken)) {
                        // Handles constructor
                        if (getToken(currentPosition) instanceof ConstructorToken) {
                            assertTokenHereIs(currentPosition + 1, new LeftParenToken());
                            ParseResult<List<Vardec>> params = parseMethodParameters(currentPosition + 2);
                            assertTokenHereIs(params.position, new RightParenToken());
                            ParseResult<Stmt> body = parseStmt(params.position + 1);
                            constructorDefs.add(new ConstructorDef(params.result, body.result));
                            currentPosition = body.position;
                        } else if (getToken(currentPosition + 2) instanceof VariableToken) {
                            ParseResult<AccessType> accessType = parseAccessType(currentPosition);
                            ParseResult<Type> returnType = parseReturnType(currentPosition + 1);
                            String methodName = ((VariableToken) getToken(currentPosition + 2)).name;
                                // Handles methods
                            if (getToken(currentPosition + 3) instanceof LeftParenToken) {
                                ParseResult<List<Vardec>> params = parseMethodParameters(currentPosition + 4);
                                assertTokenHereIs(params.position, new RightParenToken());
                                methodDefs = new ArrayList<>();

                                final ParseResult<Stmt> body = parseStmt(params.position + 1);
                                methodDefs.add(new MethodDef(
                                    accessType.result,
                                    returnType.result,
                                    new MethodName(methodName),
                                    params.result,
                                    body.result
                                ));
                                currentPosition = body.position;

                                // Handles instance declarations
                            } else if (getToken(currentPosition + 3) instanceof SemiColonToken) {
                                instanceDecs = new ArrayList<>();
                                //System.out.println("SemiColon: "+getToken(currentPosition+3).toString());
                                instanceDecs.add(
                                    new InstanceDec(
                                        accessType.result,
                                        new Vardec(returnType.result, new VariableExp(methodName))
                                    )
                                );
                                //.out.println("Instance Dec "+ instanceDecs.get(0));
                                currentPosition+=4;
                            } else {
                                throw new ParserException("expected `(` or `;`; received " + getToken(currentPosition + 3));
                            }

                        } else {
                            throw new ParserException("expected constructor or access modifier; received " + getToken(currentPosition));
                        }
                    }
                    currentPosition++;
                    classDefs.add(
                        new ClassDef(
                            new ClassName(className),
                            new ClassName(extendsName),
                            constructorDefs,
                            methodDefs,
                            instanceDecs
                        )
                    );

                    try {
                        assertTokenHereIs(currentPosition, new ClassToken());
                    } catch (final ParserException e) {
                        shouldRun = false;
                    }
                } else {
                    throw new ParserException("expected class name; received " + getToken(position + 1));
                }
            } else {
                throw new ParserException("expected class; received " + token);
            }
        }

        return new ParseResult<>(classDefs, currentPosition);
    }

    public ParseResult<Program> parseProgram(final int position) throws ParserException {
        final ParseResult<List<ClassDef>> classDef = parseClasses(position);

        return new ParseResult<>(new Program(classDef.result), classDef.position);
    }

    public Program parseProgram() throws ParserException {
        final ParseResult<Program> program = parseProgram(0);

        if (program.position == tokens.size()) {
            return program.result;
        } else {
            System.out.println(tokens.size());
            throw new ParserException("Tokens still exist");
        }
    }


    private Vardec getVardecStatement(Token token) {

        return null;
    }
}
