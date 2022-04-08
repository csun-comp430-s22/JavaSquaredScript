package parser;

import lexer.tokens.IntegerToken;
import lexer.tokens.*;
import org.junit.Test;

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
        // Test #1 {Checking 1 + 1}
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
        // Test #2 {Checking 1 - 1}
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
        // Test #3 {Checking 10}
        assertParses(Arrays.asList(new NumbersToken(10)), new ParseResult<Exp>(new IntegerExp(10), 1));
    }

    @Test
    public void testAdditionExp() throws ParserException{
        // Test #4 {Checking 2 + 10} using Tokens
        assertParses(Arrays.asList(new NumbersToken(2), new PlusToken(), new NumbersToken(10)), 
        new ParseResult<Exp>(new OpExp(new IntegerExp(2), new PlusOp(), new IntegerExp(10)), 3));
    }

    @Test
    public void testThreeAdditions() throws ParserException{
        // Test #5 {Checking (1 + 2) + 3}
        assertParses(Arrays.asList(new NumbersToken(1),new PlusToken(), new NumbersToken(2), new PlusToken(),new NumbersToken(3)), 
        new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)), new PlusOp(), new IntegerExp(3)), 5));
    }

    @Test
    public void testThreeSubtractions() throws ParserException{
        // Test #6 {Checking 6 - 2 - 1}
        assertParses(Arrays.asList(new NumbersToken(6), new MinusToken(), new NumbersToken(2), new MinusToken(), new NumbersToken(1)),
         new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(6), new MinusOp(), new IntegerExp(2)), new MinusOp(), new IntegerExp(1)), 5));
    }
    @Test
    public void testThreeMultiplications() throws ParserException{
        // Test #7 {Checking 6 * 2 / 1}
        assertParses(Arrays.asList(new NumbersToken(6), new TimesToken(), new NumbersToken(2), new DivisionToken(), new NumbersToken(1)),
         new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(6), new MultiplicationOp(), new IntegerExp(2)), new DivisionOp(), new IntegerExp(1)), 5));
    }
    @Test
    public void testLessThan() throws ParserException{
        // Test #8 {Checking 6 < 2}
        assertParses(Arrays.asList(new NumbersToken(6), new LessThanToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new LessThanOp(), new IntegerExp(2)), 3));
    }
    @Test
    public void testGreaterThan() throws ParserException{
        // Test #9 {Checking 6 > 2}
        assertParses(Arrays.asList(new NumbersToken(6), new GreaterThanToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new GreaterThanOp(), new IntegerExp(2)), 3));
    }
    @Test
    public void testDoubleEquals() throws ParserException{
        // Test #10 {Checking 6 == 2}
        assertParses(Arrays.asList(new NumbersToken(6), new DoubleEqualsToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new DoubleEqualsOp(), new IntegerExp(2)), 3));
    }
    @Test
    public void testNotEquals() throws ParserException{
        // Test #11 {Checking 6 != 2}
        assertParses(Arrays.asList(new NumbersToken(6), new NotEqualsToken(), new NumbersToken(2)),
         new ParseResult<Exp>(new OpExp(new IntegerExp(6), new NotEqualsOp(), new IntegerExp(2)), 3));
    }
    @Test 
    public void checkTrueBool() throws ParserException{
        // Test #12 {Checking true}
        assertParses(Arrays.asList(new TrueToken()), new ParseResult<Exp>(new BooleanLiteralExp(true),1));
    }
    @Test 
    public void checkFalseBool() throws ParserException{
        // Test #13 {Checking false}
        assertParses(Arrays.asList(new FalseToken()), new ParseResult<Exp>(new BooleanLiteralExp(false),1));
    }
    @Test 
    public void checkParenExp() throws ParserException{
        // Test #14 {Checking (10)}
        assertParses(Arrays.asList(new LeftParenToken(), new NumbersToken(10), new RightParenToken()), new ParseResult<Exp>(new IntegerExp(10),3));
    }
    @Test 
    public void checkStringExp() throws ParserException{
        //Test #15 {Checking ("hello")}
        assertParses(Arrays.asList(new LeftParenToken(), new StringValueToken("\"hello\""), new RightParenToken()), new ParseResult<Exp>(new StringExp("\"hello\""),3));
    }
    @Test 
    public void checkVariableExp() throws ParserException{
        // Test #16 {Checking (i))}
        assertParses(Arrays.asList(new LeftParenToken(), new VariableToken("i"), new RightParenToken()), new ParseResult<Exp>(new VariableExp("i"),3));
    }
    @Test (expected= ParserException.class)
    public void checkErrorsExp() throws ParserException{
        // Test #17 {Checking ;i} which should error
        assertParses(Arrays.asList(new SemiColonToken()), new ParseResult<Exp>(new VariableExp("i"),1));
    }
    @Test (expected= ParserException.class)
    public void checkErrorExp() throws ParserException{
        // Test #18 {Checking (i.} which should error
        assertParses(Arrays.asList(new LeftParenToken(), new VariableToken("i"), new PeriodToken()), new ParseResult<Exp>(new VariableExp("i"),3));
    } 
    @Test
    public void checkAssignment() throws ParserException{
        // Test #19 {Checking x = 23}
        assertParses(Arrays.asList(new VariableToken("x"),new EqualsToken(),new NumbersToken(23)), new ParseResult<Exp>(new OpExp(new VariableExp("x"), new EqualsOp(), new IntegerExp(23)),3));
    }
    @Test
    public void testIfStmt() throws ParserException {
        // Test #20 {Checking if(6 < 2) {print(0);} else{print(0);} }
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
        // Test #21 {Checking while(6 < 2){ print(0);} }
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
        // Test #22 {Checking break; }
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
        // Test #23 {Checking return 2 > 1; }
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
        // Test #24 {Checking int x; }
        assertParsesStmt(Arrays.asList(new IntegerToken(), new VariableToken("x"), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new IntType(), new VariableExp("x")), 3));
    }

    @Test(expected = ParserException.class)
    public void testVardecIntDecFail() throws ParserException {
        // Test #25 {Checking int 1; } which should fail
        assertParsesStmt(Arrays.asList(new IntegerToken(), new NumbersToken(1), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new IntType(), new VariableExp("x")), 3));
    }

    @Test
    public void testVardecBoolDec() throws ParserException {
        // Test #26 {Checking bool x; }
        assertParsesStmt(Arrays.asList(new BooleanToken(), new VariableToken("x"), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new BooleanType(), new VariableExp("x")), 3));
    }

    @Test(expected = ParserException.class)
    // Test #27 {Checking bool 1; } which should fail
    public void testVardecBoolDecFail() throws ParserException {
        assertParsesStmt(Arrays.asList(new BooleanToken(), new NumbersToken(1), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new BooleanType(), new VariableExp("x")), 3));
    }

    @Test
    public void testVardecStringDec() throws ParserException {
        // Test #28 {Checking String x; }
        assertParsesStmt(Arrays.asList(new StringToken(), new VariableToken("x"), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new StringType(), new VariableExp("x")), 3));
    }

    @Test(expected = ParserException.class)
    public void testVardecStringDecFail() throws ParserException {
        // Test #29 {Checking String 1; } which should fail
        assertParsesStmt(Arrays.asList(new StringToken(), new NumbersToken(1), new SemiColonToken()),
            new ParseResult<Stmt>(new Vardec(new StringType(), new VariableExp("x")), 3));
    }

    @Test
    public void testPrintStmt() throws ParserException {
        // Test #30 {Checking print(2+10); }
        assertParsesStmt(Arrays.asList(new PrintToken(), new LeftParenToken(), new NumbersToken(2),
                new PlusToken(), new NumbersToken(10), new RightParenToken(), new SemiColonToken()),
            new ParseResult<>(new PrintStmt(new OpExp(new IntegerExp(2),
            new PlusOp(), new IntegerExp(10))), 6));
    }

    @Test (expected= ParserException.class)
    public void testVariableDecInt() throws ParserException{
        // Test #31 {Checking int "a"; } which should fail
        assertParsesStmt(Arrays.asList(new IntegerToken(), new StringValueToken("a"), new SemiColonToken()),
         new ParseResult<Stmt>(new Vardec(new IntType(),new VariableExp("x")),3));
    }

    @Test (expected= ParserException.class)
    public void testVariableDecBool() throws ParserException{
        // Test #32 {Checking bool "a"; } which should fail
        assertParsesStmt(Arrays.asList(new BooleanToken(), new StringValueToken("a"), new SemiColonToken()),
         new ParseResult<Stmt>(new Vardec(new BooleanType(),new VariableExp("x")),3));
    }

    @Test (expected= ParserException.class)
    public void testVariableDec() throws ParserException{
        // Test #33 {Checking String "a"; } which should fail
        assertParsesStmt(Arrays.asList(new StringToken(), new StringValueToken("a"), new SemiColonToken()),
         new ParseResult<Stmt>(new Vardec(new StringType(),new VariableExp("x")),3));
    }

    @Test
    public void testMethodCall() throws ParserException{
        // Test #34 {Checking methodA(23) }
        assertParses(Arrays.asList(new VariableToken("methodA"),new LeftParenToken(),new NumbersToken(23), new RightParenToken()), 
        new ParseResult<Exp>(new FunctionCallExp(new FunctionName("methodA"),Arrays.asList(new IntegerExp(23))),4));
    }

    @Test
    public void testMethodMultipleInputsCall() throws ParserException{
        // Test #35 {Checking methodB(23,"hello") }
        assertParses(Arrays.asList(new VariableToken("methodB"),new LeftParenToken(),new NumbersToken(23),new CommaToken(),
        new StringValueToken("\"hello\""),new RightParenToken()), new ParseResult<Exp>(new FunctionCallExp(new FunctionName("methodB"),Arrays.asList(new IntegerExp(23),new StringExp("\"hello\""))),6));
    }

    @Test
    public void testMethodWithDot() throws ParserException{
        // Test #36 {Checking a.methodA(23) }
        assertParses(Arrays.asList(new VariableToken("a"),new PeriodToken(),new VariableToken("methodA"),new LeftParenToken(),new NumbersToken(23), new RightParenToken()), 
        new ParseResult<Exp>(new OpExp(new VariableExp("a"),new PeriodOp(), new FunctionCallExp(new FunctionName("methodA"),Arrays.asList(new IntegerExp(23)))),6));
    }

    @Test
    public void testClassCall() throws ParserException{
        // Test #37 {Checking new classA(23) }
        assertParses(Arrays.asList(new NewToken(),new VariableToken("classA"),new LeftParenToken(),new NumbersToken(23), new RightParenToken()),
        new ParseResult<Exp>(new ClassCallExp(new ClassName("classA"),Arrays.asList(new IntegerExp(23))),5));
    }

    @Test (expected= ParserException.class)
    public void testClassCallError() throws ParserException{
        // Test #38 {Checking new classA(23) }
        assertParses(Arrays.asList(new NewToken(),new VariableToken("classA"),new SemiColonToken(),new NumbersToken(23), new RightParenToken()),
        new ParseResult<Exp>(new ClassCallExp(new ClassName("classA"),Arrays.asList(new IntegerExp(23))),5));
    }

    @Test
    public void testClassMultipleInputsCall() throws ParserException{
        // Test #39 {Checking new classB(23, "hello") }
        assertParses(Arrays.asList(new NewToken(),new VariableToken("classB"),new LeftParenToken(),new NumbersToken(23),new CommaToken(),
        new StringValueToken("\"hello\""),new RightParenToken()), new ParseResult<Exp>(new ClassCallExp(new ClassName("classB"),Arrays.asList(new IntegerExp(23),new StringExp("\"hello\""))),7));
    }

    @Test (expected= ParserException.class)
    public void testExtendsError() throws ParserException{
        // Test #40 {Checking class A extends(){} } which should fail
        assertParseProgram(Arrays.asList(new ClassToken(),new VariableToken("A"),
        new ExtendsToken(), new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken()),
        new Program(Arrays.asList(new ClassDef(new ClassName("A"), 
        new ClassName("B"), 
        new ArrayList<>(), 
        new ArrayList<>(), 
        new ArrayList<>()))));
    }

    @Test(expected = ParserException.class)
    public void testClassError() throws ParserException{
        // Test #41 {Checking class myClass extends class {public int a; constructor(){}} } which should fail
        List<Token> tokens = Arrays.asList(new PublicToken(),
            new ClassToken(), new VariableToken("myClass"), new ExtendsToken(), new VariableToken("class"),new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new VariableToken("a"), new SemiColonToken(),
            new ConstructorToken(), new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken(),
            new RightCurlyToken());
        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Arrays.asList(new ConstructorDef(new ArrayList<>(), new BlockStmt(new ArrayList<>()))),
                    new ArrayList<>(),
                    Arrays.asList(new InstanceDec(new PublicType(), new Vardec(new IntType(), new VariableExp("a"))))
                )));
        assertParseProgram(tokens, expected);
    }

    @Test(expected = ParserException.class)
    public void testClassErrortwo() throws ParserException{
        // Test #42 {Checking class int extends class {public int a; constructor(){}} } which should fail
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new IntegerToken(), new ExtendsToken(), new VariableToken("class"),new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new VariableToken("a"), new SemiColonToken(),
            new ConstructorToken(), new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken(),
            new RightCurlyToken());
        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Arrays.asList(new ConstructorDef(new ArrayList<>(), new BlockStmt(new ArrayList<>()))),
                    new ArrayList<>(),
                    Arrays.asList(new InstanceDec(new PublicType(), new Vardec(new IntType(), new VariableExp("a"))))
                )));
        assertParseProgram(tokens, expected);
    }

    @Test(expected = ParserException.class)
    public void testClassErrorChecking() throws ParserException{
        // Test #43 {Checking class myClass extends class {public int a){(){}} } which should fail
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myClass"), new ExtendsToken(), new VariableToken("class"),new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new VariableToken("a"), new RightParenToken(),
            new LeftCurlyToken(), new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken(),
            new RightCurlyToken());
        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Arrays.asList(new ConstructorDef(new ArrayList<>(), new BlockStmt(new ArrayList<>()))),
                    new ArrayList<>(),
                    Arrays.asList(new InstanceDec(new PublicType(), new Vardec(new IntType(), new VariableExp("a"))))
                )));
        assertParseProgram(tokens, expected);
    }

    @Test(expected = ParserException.class)
    public void testClassErrorCheckingConstructor() throws ParserException{
        // Test #44 {Checking class myClass extends class{int a){(){}} } which should fail
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myClass"), new ExtendsToken(), new VariableToken("class"),new LeftCurlyToken(),
            new IntegerToken(), new VariableToken("a"), new RightParenToken(),
            new LeftCurlyToken(), new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken(),
            new RightCurlyToken());
        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Arrays.asList(new ConstructorDef(new ArrayList<>(), new BlockStmt(new ArrayList<>()))),
                    new ArrayList<>(),
                    Arrays.asList(new InstanceDec(new PublicType(), new Vardec(new IntType(), new VariableExp("a"))))
                )));
        assertParseProgram(tokens, expected);
    }

    @Test
    public void testExtends() throws ParserException{
        // Test #45 {Checking class myClass extends class {public int a; constructor(){}} }
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myClass"), new ExtendsToken(), new VariableToken("class"),new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new VariableToken("a"), new SemiColonToken(),
            new ConstructorToken(), new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken(),
            new RightCurlyToken());
        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Arrays.asList(new ConstructorDef(new ArrayList<>(), new BlockStmt(new ArrayList<>()))),
                    new ArrayList<>(),
                    Arrays.asList(new InstanceDec(new PublicType(), new Vardec(new IntType(), new VariableExp("a"))))
                )));
        assertParseProgram(tokens, expected);
    }

    @Test(expected = ParserException.class)
    public void testExtendsErrorChecking() throws ParserException{
        // Test #46 {Checking class myClass extends class {int int a; constructor(){}} } which should fail
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myClass"), new ExtendsToken(), new VariableToken("class"),new LeftCurlyToken(),
            new IntegerToken(), new IntegerToken(), new VariableToken("a"), new SemiColonToken(),
            new ConstructorToken(), new LeftParenToken(), new RightParenToken(), new LeftCurlyToken(), new RightCurlyToken(),
            new RightCurlyToken());
        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Arrays.asList(new ConstructorDef(new ArrayList<>(), new BlockStmt(new ArrayList<>()))),
                    new ArrayList<>(),
                    Arrays.asList(new InstanceDec(new PublicType(), new Vardec(new IntType(), new VariableExp("a"))))
                )));
        assertParseProgram(tokens, expected);
    }
    
    @Test
    public void testProgram() throws ParserException {
        // Test #47 {Checking class myClass {public int myMethod(int x){print(0);}
        //                      public int myMethod(int x, bool x, String y){print(0);}}
        //                      class myClass{private String myMethod(int x){print(0);}}
        //                      class myClass {protected bool myMethod(int x){print(0);}} }
        //This tests the Program as well as all Access Modifiers and Type Declarations
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myClass"), new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new VariableToken("myMethod"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new RightParenToken(), new LeftCurlyToken(),
            new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
            new RightCurlyToken(),
                new PublicToken(), new IntegerToken(), new VariableToken("myMethod"), new LeftParenToken(), 
                new IntegerToken(), new VariableToken("x"),new CommaToken(), 
                new BooleanToken(), new VariableToken("x"), new CommaToken(),
                new StringToken(), new VariableToken("y"), new RightParenToken(),
                new LeftCurlyToken(),new PrintToken(), new LeftParenToken(), 
                new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
            new RightCurlyToken(),
            new ClassToken(), new VariableToken("myClass"), new LeftCurlyToken(),
            new PrivateToken(), new StringToken(), new VariableToken("myMethod"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new RightParenToken(), new LeftCurlyToken(),
            new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
            new RightCurlyToken(),
            new RightCurlyToken(),
            new ClassToken(), new VariableToken("myClass"), new LeftCurlyToken(),
            new ProtectedToken(), new BooleanToken(), new VariableToken("myMethod"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new RightParenToken(), new LeftCurlyToken(),
            new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
            new RightCurlyToken(),
            new RightCurlyToken()
        );

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Arrays.asList(new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("myMethod"),
                        Arrays.asList(new Vardec(new IntType(), new VariableExp("x")),
                                      new Vardec(new BooleanType(), new VariableExp("x")),
                                      new Vardec(new StringType(), new VariableExp("y"))),
                        new BlockStmt(
                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                        )
                    )),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Arrays.asList(new MethodDef(
                        new PrivateType(),
                        new StringType(),
                        new MethodName("myMethod"),
                        Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                        new BlockStmt(
                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                        )
                    )),
                    new ArrayList<>()
                ),
                    new ClassDef(
                            new ClassName("myClass"),
                            new ClassName(""),
                            new ArrayList<>(),
                            Arrays.asList(new MethodDef(
                                    new ProtectedType(),
                                    new BooleanType(),
                                    new MethodName("myMethod"),
                                    Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                                    new BlockStmt(
                                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                                    )
                            )),
                            new ArrayList<>()
                    )
            )
        );
        assertParseProgram(tokens, expected);
    }
    @Test(expected=ParserException.class)
    public void testProgramError() throws ParserException {
        // Test #48 {Checking class myClass {public int myMethod(int){print(0);}
        //                                      public int myMethod(int x, int x){print(0);}}
        //                      class myClass{private String myMethod(int x){print(0);}}
        //                      class myClass {protected boolean myMethod(int x){print(0);}} } which should fail
        List<Token> tokens = Arrays.asList(
            new ClassToken(), new VariableToken("myClass"), new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new VariableToken("myMethod"), new LeftParenToken(),new IntegerToken(), new RightParenToken(), new LeftCurlyToken(),
            new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
            new RightCurlyToken(),
                new PublicToken(), new IntegerToken(), new VariableToken("myMethod"), new LeftParenToken(), 
                new IntegerToken(), new VariableToken("x"),new CommaToken(), new IntegerToken(), new VariableToken("x"), new RightParenToken(), new LeftCurlyToken(),
                new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
                new RightCurlyToken(),
            new RightCurlyToken(),
            new ClassToken(), new VariableToken("myClass"), new LeftCurlyToken(),
            new PrivateToken(), new StringToken(), new VariableToken("myMethod"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new RightParenToken(), new LeftCurlyToken(),
            new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
            new RightCurlyToken(),
            new RightCurlyToken(),
            new ClassToken(), new VariableToken("myClass"), new LeftCurlyToken(),
            new ProtectedToken(), new BooleanToken(), new VariableToken("myMethod"), new LeftParenToken(), new IntegerToken(), new VariableToken("x"), new RightParenToken(), new LeftCurlyToken(),
            new PrintToken(), new LeftParenToken(), new NumbersToken(0), new RightParenToken(), new SemiColonToken(),
            new RightCurlyToken(),
            new RightCurlyToken()
        );

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Arrays.asList(new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("myMethod"),
                        Arrays.asList(new Vardec(new IntType(), new VariableExp("x")),
                                      new Vardec(new IntType(), new VariableExp("x"))),
                        new BlockStmt(
                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                        )
                    )),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Arrays.asList(new MethodDef(
                        new PrivateType(),
                        new StringType(),
                        new MethodName("myMethod"),
                        Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                        new BlockStmt(
                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                        )
                    )),
                    new ArrayList<>()
                ),
                    new ClassDef(
                            new ClassName("myClass"),
                            new ClassName(""),
                            new ArrayList<>(),
                            Arrays.asList(new MethodDef(
                                    new ProtectedType(),
                                    new BooleanType(),
                                    new MethodName("myMethod"),
                                    Arrays.asList(new Vardec(new IntType(), new VariableExp("x"))),
                                    new BlockStmt(
                                            Arrays.asList(new PrintStmt(new IntegerExp(0)))
                                    )
                            )),
                            new ArrayList<>()
                    )
            )
        );
        assertParseProgram(tokens, expected);
    }
}
