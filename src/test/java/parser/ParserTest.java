package parser;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lexer.tokens.IntegerToken;
import lexer.tokens.*;
import org.junit.Test;

import java.beans.Transient;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.management.StringValueExp;

import static org.junit.Assert.*;

public class ParserTest {
    public void assertParses(final List<Token> input, final ParseResult<Exp> expected) throws ParserException{
        //List<Token> tokensList =  Arrays.asList(input);
        final Parser parser = new Parser(input);
        assertEquals(expected,parser.parseExp(0));
    }

    public void assertParsesStmt(final List<Token> input, final ParseResult<Stmt> expected) throws ParserException{
        //List<Token> tokensList =  Arrays.asList(input);
        final Parser parser = new Parser(input);
        assertEquals(expected,parser.parseStmt(0));
    }

    public void assertParseProgram(final List<Token> input, final Program expected) throws ParserException {
        final Parser parser = new Parser(input);
        assertEquals(expected, parser.parseProgram());
    }

    @Test
    public void testEqualsOpExp() {
        // 1 + 1 == 1 + 1
        final OpExp first = new OpExp(new IntegerExp(1),
                                      new PlusOp(),
                                      new IntegerExp(1));
        final OpExp second = new OpExp(new IntegerExp(1),
                                      new PlusOp(),
                                      new IntegerExp(1));
        assertEquals(first, second);
    }

    @Test
    public void testMinusOpExp() {
        final OpExp first = new OpExp(new IntegerExp(1),
                new MinusOp(),
                new IntegerExp(1));
        final OpExp second = new OpExp(new IntegerExp(1),
                new MinusOp(),
                new IntegerExp(1));
        assertEquals(first, second);
    }

    @Test
    public void testNumbersExp() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(10)), new ParseResult<Exp>(new IntegerExp(10), 1));
    }

    @Test
    public void testAdditionExp() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(2), new PlusToken(), new NumbersToken(10)), 
        new ParseResult<Exp>(new OpExp(new IntegerExp(2), new PlusOp(), new IntegerExp(10)), 3));
    }

    @Test
    public void testThreeAdditions() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(1),new PlusToken(), new NumbersToken(2), new PlusToken(),new NumbersToken(3)), 
        new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)), new PlusOp(), new IntegerExp(3)), 5));
    }

    @Test
    public void testThreeSubtractions() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(6), new MinusToken(), new NumbersToken(2), new MinusToken(), new NumbersToken(1)),
         new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(6), new MinusOp(), new IntegerExp(2)), new MinusOp(), new IntegerExp(1)), 5));
    }
    @Test
    public void testThreeMultiplications() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(6), new TimesToken(), new NumbersToken(2), new DivisionToken(), new NumbersToken(1)),
         new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(6), new MultiplicationOp(), new IntegerExp(2)), new DivisionOp(), new IntegerExp(1)), 5));
    }
    @Test
    public void testLessThan() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(6), new LessThanToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new LessThanOp(), new IntegerExp(2)), 3));
    }
    @Test
    public void testGreaterThan() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(6), new GreaterThanToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new GreaterThanOp(), new IntegerExp(2)), 3));
    }
    @Test
    public void testDoubleEquals() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(6), new DoubleEqualsToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new DoubleEqualsOp(), new IntegerExp(2)), 3));
    }
    @Test
    public void testNotEquals() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(6), new NotEqualsToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new NotEqualsOp(), new IntegerExp(2)), 3));
    }
    @Test 
    public void checkTrueBool() throws ParserException{
        assertParses(Arrays.asList(new TrueToken()), new ParseResult<Exp>(new BooleanLiteralExp(true),1));
    }
    @Test 
    public void checkFalseBool() throws ParserException{
        assertParses(Arrays.asList(new FalseToken()), new ParseResult<Exp>(new BooleanLiteralExp(false),1));
    }
    @Test 
    public void checkParenExp() throws ParserException{
        assertParses(Arrays.asList(new LeftParenToken(), new NumbersToken(10), new RightParenToken()), new ParseResult<Exp>(new IntegerExp(10),3));
    }
    @Test 
    public void checkStringExp() throws ParserException{
        assertParses(Arrays.asList(new LeftParenToken(), new StringValueToken("\"hello\""), new RightParenToken()), new ParseResult<Exp>(new StringExp("\"hello\""),3));
    }
    @Test 
    public void checkVariableExp() throws ParserException{
        assertParses(Arrays.asList(new LeftParenToken(), new VariableToken("i"), new RightParenToken()), new ParseResult<Exp>(new VariableExp("i"),3));
    }
    @Test (expected= ParserException.class)
    public void checkErrorsExp() throws ParserException{
        assertParses(Arrays.asList(new SemiColonToken()), new ParseResult<Exp>(new VariableExp("i"),1));
    }
    @Test (expected= ParserException.class)
    public void checkErrorExp() throws ParserException{
        assertParses(Arrays.asList(new LeftParenToken(), new VariableToken("i"), new PeriodToken()), new ParseResult<Exp>(new VariableExp("i"),3));
    } 
    @Test
    public void checkAssignment() throws ParserException{
        assertParses(Arrays.asList(new VariableToken("x"),new EqualsToken(),new NumbersToken(23)), new ParseResult<Exp>(new OpExp(new VariableExp("x"), new EqualsOp(), new IntegerExp(23)),3));
    }
    @Test
    public void testIfStmt() throws ParserException {

        assertParsesStmt(
            Arrays.asList(
                new IfToken(), new LeftParenToken(), new NumbersToken(6), new LessThanToken(),
                new NumbersToken(2), new RightParenToken(), new LeftCurlyToken(),
                new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(),
                new SemiColonToken(), new RightCurlyToken(), new ElseToken(), new LeftCurlyToken(), new PrintToken(),
                new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken()
            ),
            new ParseResult<>(
                new IfStmt(
                    new OpExp(new IntegerExp(6), new LessThanOp(), new IntegerExp(2)),
                    new BlockStmt(Collections.singletonList(
                        new PrintStmt(new IntegerExp(0)))),
                    new BlockStmt(Collections.singletonList(
                        new PrintStmt(new IntegerExp(0)))
                    )
                ), 21
            )
        );
    }

    @Test
    public void testWhileStmt() throws ParserException {
        assertParsesStmt(
            Arrays.asList(
                new WhileToken(), new LeftParenToken(), new NumbersToken(6), new LessThanToken(),
                new NumbersToken(2), new RightParenToken(), new LeftCurlyToken(), new PrintToken(),
                new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken()
            ),
            new ParseResult<>(
                new WhileStmt(
                    new OpExp(new IntegerExp(6), new LessThanOp(), new IntegerExp(2)),
                    new BlockStmt(Collections.singletonList(
                        new PrintStmt(new IntegerExp(0))))
                ), 13
            )
        );
    }

    @Test
    public void testBreakStmt() throws ParserException {
        assertParsesStmt(
            Arrays.asList(
                new BreakToken(), new SemiColonToken()
            ),
            new ParseResult<>(
                new BreakStmt(), 2
            )
        );
    }

    @Test
    public void testReturnStmt() throws ParserException {
        assertParsesStmt(
            Arrays.asList(
                new ReturnToken(), new NumbersToken(2), new GreaterThanToken(), new NumbersToken(1),
                new SemiColonToken()
            ),
            new ParseResult<>(
                new ReturnStmt(new OpExp(new IntegerExp(2), new GreaterThanOp(), new IntegerExp(1))), 5
            )
        );
    }

    @Test
    public void testVardecIntDec() throws ParserException {
        assertParsesStmt(Arrays.asList(new IntegerToken(), new VariableToken("x"), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new IntType(), new VariableExp("x")), 3));
    }

    @Test(expected = ParserException.class)
    public void testVardecIntDecFail() throws ParserException {
        assertParsesStmt(Arrays.asList(new IntegerToken(), new NumbersToken(1), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new IntType(), new VariableExp("x")), 3));
    }

    @Test
    public void testVardecBoolDec() throws ParserException {
        assertParsesStmt(Arrays.asList(new BooleanToken(), new VariableToken("x"), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new BooleanType(), new VariableExp("x")), 3));
    }

    @Test(expected = ParserException.class)
    public void testVardecBoolDecFail() throws ParserException {
        assertParsesStmt(Arrays.asList(new BooleanToken(), new NumbersToken(1), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new BooleanType(), new VariableExp("x")), 3));
    }

    @Test
    public void testVardecStringDec() throws ParserException {
        assertParsesStmt(Arrays.asList(new StringToken(), new VariableToken("x"), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new StringType(), new VariableExp("x")), 3));
    }

    @Test(expected = ParserException.class)
    public void testVardecStringDecFail() throws ParserException {
        assertParsesStmt(Arrays.asList(new StringToken(), new NumbersToken(1), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new StringType(), new VariableExp("x")), 3));
    }

    @Test
    public void testPrintStmt() throws ParserException {
        assertParsesStmt(Arrays.asList(new PrintToken(), new LeftParenToken(), new NumbersToken(2),
                new PlusToken(), new NumbersToken(10), new RightParenToken(), new SemiColonToken()),
            new ParseResult<>(new PrintStmt(new OpExp(new IntegerExp(2),
            new PlusOp(), new IntegerExp(10))), 6));
    }
    @Test (expected= ParserException.class)
    public void testVariableDecInt() throws ParserException{
        assertParsesStmt(Arrays.asList(new IntegerToken(), new StringValueToken("a"), new SemiColonToken()),
         new ParseResult<Stmt>(new Vardec(new IntType(),new VariableExp("x")),3));
    }
    @Test (expected= ParserException.class)
    public void testVariableDecBool() throws ParserException{
        assertParsesStmt(Arrays.asList(new BooleanToken(), new StringValueToken("a"), new SemiColonToken()),
         new ParseResult<Stmt>(new Vardec(new BooleanType(),new VariableExp("x")),3));
    }
    @Test (expected= ParserException.class)
    public void testVariableDec() throws ParserException{
        assertParsesStmt(Arrays.asList(new StringToken(), new StringValueToken("a"), new SemiColonToken()),
         new ParseResult<Stmt>(new Vardec(new StringType(),new VariableExp("x")),3));
    }

    @Test
    public void testMethodCall() throws ParserException{
        assertParses(Arrays.asList(new VariableToken("methodA"),new LeftParenToken(),new NumbersToken(23), new RightParenToken()), 
        new ParseResult<Exp>(new FunctionCallExp(new FunctionName("methodA"),Arrays.asList(new IntegerExp(23))),4));
        //methodA(23,i)
    }
    @Test
    public void testMethodMultipleInputsCall() throws ParserException{
        assertParses(Arrays.asList(new VariableToken("methodB"),new LeftParenToken(),new NumbersToken(23),new CommaToken(),
        new StringValueToken("\"hello\""),new RightParenToken()), new ParseResult<Exp>(new FunctionCallExp(new FunctionName("methodB"),Arrays.asList(new IntegerExp(23),new StringExp("\"hello\""))),6));
    }
    @Test
    public void testMethodWithDot() throws ParserException{
        assertParses(Arrays.asList(new VariableToken("a"),new PeriodToken(),new VariableToken("methodA"),new LeftParenToken(),new NumbersToken(23), new RightParenToken()), 
        new ParseResult<Exp>(new OpExp(new VariableExp("a"),new PeriodOp(), new FunctionCallExp(new FunctionName("methodA"),Arrays.asList(new IntegerExp(23)))),6));
    }

    @Test
    public void testClassCall() throws ParserException{
        assertParses(Arrays.asList(new NewToken(),new VariableToken("methodA"),new LeftParenToken(),new NumbersToken(23), new RightParenToken()), 
        new ParseResult<Exp>(new ClassCallExp(new ClassName("methodA"),Arrays.asList(new IntegerExp(23))),5));
        //methodA(23,i)
    }
    @Test
    public void testClassMultipleInputsCall() throws ParserException{
        assertParses(Arrays.asList(new NewToken(),new VariableToken("methodB"),new LeftParenToken(),new NumbersToken(23),new CommaToken(),
        new StringValueToken("\"hello\""),new RightParenToken()), new ParseResult<Exp>(new ClassCallExp(new ClassName("methodB"),Arrays.asList(new IntegerExp(23),new StringExp("\"hello\""))),7));
    }

    @Test
    public void testProgram() throws ParserException {
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myclass"), new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new VariableToken("myMethod"),
            new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new SemiColonToken(),
            new RightParenToken(), new LeftCurlyToken(),
            new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(),
            new SemiColonToken(), new RightCurlyToken(), new RightCurlyToken()
        );

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myclass"),
                    Arrays.asList(new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("myMethod"),
                        Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                        new BlockStmt(
                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                        )
                    ))
                )
            )
        );
        assertParseProgram(tokens, expected);
    }

    @Test
    public void testProgramMultiClasses() throws ParserException {
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myclass1"), new LeftCurlyToken(),
                new PublicToken(), new IntegerToken(), new VariableToken("myMethod"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new SemiColonToken(), new RightParenToken(), new LeftCurlyToken(),
                    new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
            new RightCurlyToken(),
            new ClassToken(), new VariableToken("myclass2"), new LeftCurlyToken(),
                new PublicToken(), new IntegerToken(), new VariableToken("myMethod"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new SemiColonToken(), new RightParenToken(), new LeftCurlyToken(),
                    new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
            new RightCurlyToken()
        );

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myclass1"),
                    Arrays.asList(new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("myMethod"),
                        Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                        new BlockStmt(
                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                        )
                    ))
                ),
                new ClassDef(
                    new ClassName("myclass2"),
                    Arrays.asList(new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("myMethod"),
                        Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                        new BlockStmt(
                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                        )
                    ))
                )
            )
        );
        assertParseProgram(tokens, expected);
    }

    @Test
    public void testProgramMultiClassesMultiMethods() throws ParserException {
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myclass1"), new LeftCurlyToken(),
                new PublicToken(), new IntegerToken(), new VariableToken("myMethod1"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new SemiColonToken(), new RightParenToken(), new LeftCurlyToken(),
                    new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
                new PrivateToken(), new BooleanToken(), new VariableToken("myMethod2"), new LeftParenToken(), new StringToken(), new VariableToken("y"), new SemiColonToken(), new RightParenToken(), new LeftCurlyToken(),
                    new PrintToken(), new LeftParenToken(), new NumbersToken(2), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
                new ProtectedToken(), new StringToken(), new VariableToken("myMethod3"), new LeftParenToken(), new BooleanToken(), new VariableToken("z"), new SemiColonToken(),
            new RightParenToken(), new LeftCurlyToken(),
                    new PrintToken(), new LeftParenToken(), new NumbersToken(2), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
            new RightCurlyToken(),
            new ClassToken(), new VariableToken("myclass2"), new LeftCurlyToken(),
                new PublicToken(), new IntegerToken(), new VariableToken("myMethod1"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new SemiColonToken(), new RightParenToken(), new LeftCurlyToken(),
                    new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
                new PrivateToken(), new BooleanToken(), new VariableToken("myMethod2"), new LeftParenToken(), new StringToken(), new VariableToken("y"), new SemiColonToken(), new RightParenToken(), new LeftCurlyToken(),
                    new PrintToken(), new LeftParenToken(), new NumbersToken(2), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
            new RightCurlyToken()
        );

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myclass1"),
                    Arrays.asList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod1"),
                            Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Arrays.asList(new PrintStmt(new IntegerExp(0)))
                            )
                        ),
                        new MethodDef(
                            new PrivateType(),
                            new BooleanType(),
                            new MethodName("myMethod2"),
                            Arrays.asList(new Vardec(new StringType(), new VariableExp("y"))),
                            new BlockStmt(
                                Arrays.asList(new PrintStmt(new IntegerExp(2)))
                            )
                        ),
                        new MethodDef(
                            new ProtectedType(),
                            new StringType(),
                            new MethodName("myMethod3"),
                            Arrays.asList(new Vardec(new BooleanType(), new VariableExp("z"))),
                            new BlockStmt(
                                Arrays.asList(new PrintStmt(new IntegerExp(2)))
                            )
                        )
                    )
                ),
                new ClassDef(
                    new ClassName("myclass2"),
                    Arrays.asList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod1"),
                            Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Arrays.asList(new PrintStmt(new IntegerExp(0)))
                            )
                        ),
                        new MethodDef(
                            new PrivateType(),
                            new BooleanType(),
                            new MethodName("myMethod2"),
                            Arrays.asList(new Vardec(new StringType(), new VariableExp("y"))),
                            new BlockStmt(
                                Arrays.asList(new PrintStmt(new IntegerExp(2)))
                            )
                        )
                    )
                )
            )
        );

        assertParseProgram(tokens, expected);
    }


    // methodA(5,a)
}
