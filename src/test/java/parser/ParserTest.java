package parser;

import lexer.Tokenizer;
import lexer.TokenizerException;
import lexer.tokens.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    public List<Token> tokenizes(final String input) throws TokenizerException{
        final Tokenizer tokenizer = new Tokenizer(input);
        return tokenizer.tokenize();
    }
    public void assertParses(final List<Token> input, final ParseResult<Exp> expected) throws ParserException {
        //List<Token> tokensList =  Arrays.asList(input);
        final Parser parser = new Parser(input);
        assertEquals(expected,parser.parseExp(0));
    }

    public void assertParsesStmt(final List<Token> input, final ParseResult<Stmt> expected) throws ParserException {
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
        // Test #1 - Checking:
        //      1 + 1

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
        // Test #2 - Checking:
        //      1 - 1

        final OpExp first = new OpExp(new IntegerExp(1),
                new MinusOp(),
                new IntegerExp(1));
        final OpExp second = new OpExp(new IntegerExp(1),
                new MinusOp(),
                new IntegerExp(1));

        assertEquals(first, second);
    }

    @Test
    public void testNumbersExp() throws ParserException, TokenizerException {
        // Test #3 - Checking:
        //      10

        String input = "10";

        ParseResult<Exp> expected = new ParseResult<>(
            new IntegerExp(10),
            1
        );

        assertParses(tokenizes(input), expected);
    }

    @Test
    public void testAdditionExp() throws ParserException, TokenizerException {
        // Test #4 - Checking:
        //      2 + 10

        String input = "2 + 10";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new IntegerExp(2),
                new PlusOp(),
                new IntegerExp(10)
            ),
            3
        );

        assertParses(tokenizes(input), expected);
    }

    @Test
    public void testThreeAdditions() throws ParserException, TokenizerException {
        // Test #5 - Checking:
        //      1 + 2 + 3

        String input = "1 + 2 + 3";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new OpExp(
                    new IntegerExp(1),
                    new PlusOp(),
                    new IntegerExp(2)
                ),
                new PlusOp(),
                new IntegerExp(3)
            ),
            5
        );

        assertParses(tokenizes(input), expected);
    }

    @Test
    public void testThreeSubtractions() throws ParserException, TokenizerException {
        // Test #6 - Checking:
        //      6 - 2 - 1

        String input = "6 - 2 - 1";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new OpExp(
                    new IntegerExp(6),
                    new MinusOp(),
                    new IntegerExp(2)
                ),
                new MinusOp(),
                new IntegerExp(1)
            ),
            5
        );

        assertParses(tokenizes(input), expected);
    }
    @Test
    public void testThreeMultiplications() throws ParserException, TokenizerException {
        // Test #7 - Checking:
        //      6 * 2 / 1

        String input = "6 * 2 / 1";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new OpExp(
                    new IntegerExp(6),
                    new MultiplicationOp(),
                    new IntegerExp(2)
                ),
                new DivisionOp(),
                new IntegerExp(1)
            ),
            5
        );

        assertParses(tokenizes(input), expected);
    }
    @Test
    public void testLessThan() throws ParserException, TokenizerException {
        // Test #8 - Checking:
        //      6 < 2

        String input = "6 < 2";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new IntegerExp(6),
                new LessThanOp(),
                new IntegerExp(2)
            ),
            3
        );

        assertParses(tokenizes(input), expected);
    }
    @Test
    public void testGreaterThan() throws ParserException, TokenizerException {
        // Test #9 - Checking:
        //      6 > 2

        String input = "6 > 2";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new IntegerExp(6),
                new GreaterThanOp(),
                new IntegerExp(2)
            ),
            3
        );

        assertParses(tokenizes(input), expected);
    }
    @Test
    public void testDoubleEquals() throws ParserException, TokenizerException {
        // Test #10 - Checking:
        //      6 == 2

        String input = "6 == 2";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new IntegerExp(6),
                new DoubleEqualsOp(),
                new IntegerExp(2)
            ),
            3
        );

        assertParses(tokenizes(input), expected);
    }
    @Test
    public void testNotEquals() throws ParserException, TokenizerException {
        // Test #11 - Checking:
        //      6 != 2

        String input = "6 != 2";

        ParseResult<Exp> expected = new ParseResult<>(
            new OpExp(
                new IntegerExp(6),
                new NotEqualsOp(),
                new IntegerExp(2)
            ),
            3
        );

        assertParses(tokenizes(input), expected);
    }
    @Test 
    public void checkTrueBool() throws ParserException, TokenizerException {
        // Test #12 - Checking:
        //      true

        String input = "true";

        ParseResult<Exp> expected = new ParseResult<>(
            new BooleanLiteralExp(true),
            1
        );

        assertParses(tokenizes(input), expected);
    }
    @Test 
    public void checkFalseBool() throws ParserException, TokenizerException {
        // Test #13 - Checking:
        //      false

        String input = "false";

        ParseResult<Exp> expected = new ParseResult<>(
            new BooleanLiteralExp(false),
            1
        );

        assertParses(tokenizes(input), expected);
    }
    @Test 
    public void checkParenExp() throws ParserException, TokenizerException {
        // Test #14 - Checking:
        //      (10)

        String input = "(10)";

        ParseResult<Exp> expected = new ParseResult<>(
            new IntegerExp(10),
            3
        );

        assertParses(tokenizes(input), expected);
    }

    @Test 
    public void checkStringExp() throws ParserException, TokenizerException {
        // Test #15 - Checking:
        //      ("hello")

        String input = "(\"hello\")";

        ParseResult<Exp> expected = new ParseResult<>(
            new StringExp("\"hello\""),
            3
        );

        assertParses(tokenizes(input), expected);
    }


    @Test 
    public void checkVariableExp() throws ParserException, TokenizerException {
        // Test #16 - Checking:
        //      (i)

        String input = "(i)";

        ParseResult<Exp> expected = new ParseResult<>(
            new VariableExp("i"),
            3
        );

        assertParses(tokenizes(input), expected);
    }
    @Test (expected= ParserException.class)
    public void checkErrorsExp() throws ParserException, TokenizerException {
        // Test #17 - Checking fail:
        //      ;i

        String input = ";i";

        ParseResult<Exp> expected = new ParseResult<>(
            new VariableExp("i"),
            1
        );

        assertParses(tokenizes(input), expected);
    }
    @Test (expected= ParserException.class)
    public void checkErrorExp() throws ParserException, TokenizerException {
        // Test #18 - Checking fail:
        //      (i.

        String input = "(i.";

        ParseResult<Exp> expected = new ParseResult<>(
            new VariableExp("i"),
            3
        );

        assertParses(tokenizes(input), expected);
    } 

    @Test
    public void testIfStmt() throws ParserException, TokenizerException {
        // Test #20 - Checking:
        //      if (6 < 2) {
        //          print(0);
        //      } else {
        //          print(0);
        //      }

        String input =
            "if (6 < 2) {" +
                "print(0);" +
            "} else {" +
                "print(0);" +
            "}";

        ParseResult<Stmt> expected = new ParseResult<>(
            new IfStmt(
                new OpExp(
                    new IntegerExp(6),
                    new LessThanOp(),
                    new IntegerExp(2)
                ),
                new BlockStmt(
                    Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                ),
                new BlockStmt(
                    Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                )
            ), 21
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testWhileStmt() throws ParserException, TokenizerException {
        // Test #21 - Checking:
        //      while (6 < 2) {
        //          print(0);
        //      }

        String input =
            "while (6 < 2) {" +
                "print(0);" +
            "}";

        ParseResult<Stmt> expected = new ParseResult<>(
            new WhileStmt(
                new OpExp(
                    new IntegerExp(6),
                    new LessThanOp(),
                    new IntegerExp(2)
                ),
                new BlockStmt(
                    Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                )
            ), 13
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testBreakStmt() throws ParserException, TokenizerException {
        // Test #22 - Checking:
        //      break;

        String input = "break;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new BreakStmt(),
            1
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testReturnStmt() throws ParserException, TokenizerException {
        // Test #23 - Checking:
        //      return 2 > 1;

        String input = "return 2 > 1;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new ReturnStmt(
                new OpExp(
                    new IntegerExp(2),
                    new GreaterThanOp(),
                    new IntegerExp(1))
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecIntDec() throws ParserException, TokenizerException {
        // Test #24 - Checking:
        //      Int x;

        String input = "Int x;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new IntType(),
                new VariableExp("x")
            ),
            2
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecBoolDec() throws ParserException, TokenizerException {
        // Test #26 {Checking bool x; }
        // Test #26 - Checking:
        //      Boolean x;

        String input = "Boolean x;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new BooleanType(),
                new VariableExp("x")
            ),
            2
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testVardecBoolDecFail() throws ParserException, TokenizerException {
        // Test #27 - Checking fail:
        //      Boolean 1;

        String input = "Boolean 1;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new BooleanType(),
                new VariableExp("x")
            ),
            3
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecStringDec() throws ParserException, TokenizerException {
        // Test #28 - Checking:
        //      strg x;

        String input = "strg x;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new StringType(),
                new VariableExp("x")
            ),
            2
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testVardecStringDecFail() throws ParserException, TokenizerException {
        // Test #29 - Checking fail:
        //      strg 1;

        String input = "strg 1;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new StringType(),
                new VariableExp("x")
            ),
            3
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test (expected= ParserException.class)
    public void testVariableDecInt() throws ParserException, TokenizerException {
        // Test #30 - Checking fail:
        //      Int "a";

        String input = "Int \"a\"";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new IntType(),
                new VariableExp("x")
            ),
            3
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test (expected= ParserException.class)
    public void testVariableDecBool() throws ParserException, TokenizerException {
        // Test #31 - Checking fail:
        //      Boolean "a";

        String input = "Boolean \"a\"";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new BooleanType(),
                new VariableExp("x")
            ),
            3
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test (expected= ParserException.class)
    public void testVariableDec() throws ParserException, TokenizerException {
        // Test #32 - Checking fail:
        //      strg "a";

        String input = "strg \"a\"";

        ParseResult<Stmt> expected = new ParseResult<>(
            new Vardec(
                new StringType(),
                new VariableExp("x")
            ),
            3
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testPrintStmt() throws ParserException, TokenizerException {
        // Test #33 - Checking:
        //      print(2 + 10);

        String input = "print(2 + 10);";

        ParseResult<Stmt> expected = new ParseResult<>(
            new PrintStmt(
                new OpExp(
                    new IntegerExp(2),
                    new PlusOp(),
                    new IntegerExp(10)
                )
            ),
            6
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testMethodWithDot() throws ParserException, TokenizerException {
        // Test #36 - Checking:
        //      a.methodA(23)

        String input = "(a).methodA(23)";

        ParseResult<Exp> expected = new ParseResult<>(
            new FunctionCallExp(
                new MethodName("methodA"),
                new VariableExp("a"),
                Collections.singletonList(new IntegerExp(23))
            ),
            8
        );

        assertParses(tokenizes(input), expected);
    }

    @Test
    public void testClassCall() throws ParserException, TokenizerException {
        // Test #37 - Checking:
        //      new classA(23)

        String input = "new classA(23)";

        ParseResult<Exp> expected = new ParseResult<>(
            new NewExp(
                new ClassName("classA"),
                Collections.singletonList(new IntegerExp(23))
            ),
            5
        );

        assertParses(tokenizes(input), expected);
    }

    @Test (expected= ParserException.class)
    public void testClassCallError() throws ParserException, TokenizerException {
        // Test #38 - Checking fail:
        //      new classA;23)

        String input = "new classA;23)";

        ParseResult<Exp> expected = new ParseResult<>(
            new ClassCallExp(
                new ClassName("classA"),
                Collections.singletonList(new IntegerExp(23))
            ),
            5
        );

        assertParses(tokenizes(input), expected);
    }

    @Test
    public void testClassMultipleInputsCall() throws ParserException, TokenizerException {
        // Test #39 - Checking:
        //      new classB(23, "hello")

        String input = "new classB(23, \"hello\")";

        ParseResult<Exp> expected = new ParseResult<>(
            new NewExp(
                new ClassName("classB"),
                Arrays.asList(
                    new IntegerExp(23),
                    new StringExp("\"hello\"")
                )
            ),
            7
        );

        assertParses(tokenizes(input), expected);
    }

    @Test (expected= ParserException.class)
    public void testExtendsError() throws ParserException, TokenizerException {
        // Test #40 - Checking fail:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class A extends(){}

        String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class A extends (){}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("A"),
                    new ClassName(""),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testClassError() throws ParserException, TokenizerException {
        // Test #41 - Checking fail:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends class {
        //          public Int a;
        //          constructor(){}}
        //      }

        String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends class {" +
                "public Int a;" +
                "constructor(){}}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("Int"),
                    new ClassName("class"),
                    Collections.singletonList(
                        new ConstructorDef(
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testClassErrortwo() throws ParserException, TokenizerException {
        // Test #42 - Checking fail:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class Int extends class {
        //          public int a;
        //          constructor(){}}
        //      }

        String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class Int extends class {" +
                "public int a;" +
                "constructor(){}}" +
            "}";


        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("Int"),
                    new ClassName("class"),
                    Collections.singletonList(
                        new ConstructorDef(
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testClassErrorChecking() throws ParserException, TokenizerException {
        // Test #43 - Checking fail:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends class {
        //          public Int a){(){}}
        //      }

        String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends class {" +
                "public Int a){(){}}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testClassErrorCheckingConstructor() throws ParserException, TokenizerException {
        // Test #44 - Checking fail:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends class {
        //          Int a){(){}}
        //      }

        String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends class {" +
                "Int a){(){}}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Collections.singletonList(
                        new ConstructorDef(
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test
    public void testExtends() throws ParserException, TokenizerException {
        // Test #45 - Checking:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends classA {
        //          public Int a;
        //          constructor(){}
        //      }

        String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends classA {" +
                "public Int a;" +
                "constructor(){Int x;}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("classA"),
                    Collections.singletonList(
                        new ConstructorDef(
                            new ArrayList<>(),
                            new BlockStmt(
                                    Arrays.asList(new Vardec(new IntType(), new VariableExp("x")))
                            )
                        )
                    ),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );
        assertParseProgram(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testExtendsErrorChecking() throws ParserException, TokenizerException {
        // Test #46 - Checking fail:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends class {
        //          int int a;
        //          constructor(){}
        //      }

        final String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends class {" +
                "int int a;" +
                "constructor(){}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("class"),
                    Collections.singletonList(
                        new ConstructorDef(
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test
    public void testMultipleInstanceDeclarations() throws TokenizerException, ParserException {
        // Test #47 - Checking:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends classA {
        //          public Int a;
        //          public Int b;
        //          public Int c;
        //      }

        final String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends classA {" +
                "public Int a;" +
                "public Int b;" +
                "public Int c;" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("classA"),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    Arrays.asList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        ),
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("b")
                            )
                        ),
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("c")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }
    
    @Test
    public void testProgram() throws ParserException, TokenizerException {
        // Test #48 - Checking:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends classA {
        //          public Int myMethod(Int x) {}
        //          public Int myMethod(Int x, Boolean x, strg y) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          private strg myMethod(Int x) {}
        //          public Int myMethod(Int x, Boolean x, strg y) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          protected Boolean myMethod(Int x) {
        //              print(0);
        //          }
        //      }

        final String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends classA {" +
                "public Int myMethod(Int x){}" +
                "public Int myMethod(Int x, Boolean x, strg y) {" +
                    "print(0);" +
                "}" +
            "}" +
            "class myClass {" +
                "private strg myMethod(Int x){}" +
                "public Int myMethod(Int x, Boolean x, strg y) {" +
                    "print(0);" +
                "}" +
            "}" +
            "class myClass {" +
                "protected Boolean myMethod(Int x){" +
                    "print(0);" +
                "}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("classA"),
                    new ArrayList<>(),
                    Arrays.asList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        ),
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod"),
                            Arrays.asList(
                                new Vardec(new IntType(), new VariableExp("x")),
                                new Vardec(new BooleanType(), new VariableExp("x")),
                                new Vardec(new StringType(), new VariableExp("y"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Arrays.asList(
                        new MethodDef(
                            new PrivateType(),
                            new StringType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        ),
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod"),
                            Arrays.asList(
                                new Vardec(new IntType(), new VariableExp("x")),
                                new Vardec(new BooleanType(), new VariableExp("x")),
                                new Vardec(new StringType(), new VariableExp("y"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new ProtectedType(),
                            new BooleanType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }
    @Test(expected=ParserException.class)
    public void testProgramError() throws ParserException, TokenizerException {
        // Test #49 - Checking fail:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass {
        //          public Int myMethod(Int) {
        //              print(0);
        //          }
        //          public Int myMethod(Int x, Int x) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          private strg myMethod(Int x) {}
        //      }
        //      class myClass {
        //          protected Boolean myMethod(Int x) {
        //              print(0);
        //          }
        //      }

        final String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass {" +
                "public Int myMethod(Int) {" +
                    "print(0);" +
                "}" +
                "public Int myMethod(Int x, Int x) {" +
                    "print(0);" +
                "}" +
            "}" +
            "class myClass {" +
                "public strg myMethod(Int x) {}" +
            "}" +
            "class myClass {" +
                "protected Boolean myMethod(Int x) {" +
                    "print(0);" +
                "}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod"),
                            Arrays.asList(
                                new Vardec(new IntType(), new VariableExp("x")),
                                new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PrivateType(),
                            new StringType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new ProtectedType(),
                            new BooleanType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test
    public void testStringToken()  throws ParserException, TokenizerException{
        // Test #50 - Checking:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass {
        //          public Int a;
        //          constructor(){}
        //      }

        final String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends classA {" +
                "public Int a;" +
                "constructor(){}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("classA"),
                    Collections.singletonList(
                        new ConstructorDef(
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new InstanceDec(
                            new PublicType(),
                            new Vardec(
                                new IntType(),
                                new VariableExp("a")
                            )
                        )
                    )
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test
    public void testMultipleTokensWithStringToken() throws ParserException, TokenizerException{
        // Test #51 - Checking:
        //      class mainClass {
        //          public Int main(){}
        //      }
        //      class myClass extends classA {
        //          public Int myMethod(Int x) {
        //              print(0);
        //          }
        //          public Int myMethod(Int x, Boolean x, strg y) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          private strg myMethod(Int x) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          protected Boolean myMethod(Int x) {
        //              print(0);
        //          }
        //      }

        final String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass extends classA {" +
                "public Int myMethod(Int x){" +
                    "print(0);" +
                "}" +
                "public Int myMethod(Int x, Boolean x, strg y) {" +
                    "print(0);" +
                "}" +
            "}" +
            "class myClass {" +
                "private strg myMethod(Int x) {" +
                    "print(0);" +
                "}" +
            "}" +
            "class myClass {" +
                "protected Boolean myMethod(Int x) {" +
                    "print(0);" +
                "}" +
            "}";

        Program expected = new Program(
            Arrays.asList(
                new ClassDef(
                    new ClassName("mainClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("main"),
                            new ArrayList<>(),
                            new BlockStmt(
                                new ArrayList<>()
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName("classA"),
                    new ArrayList<>(),
                    Arrays.asList(
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        ),
                        new MethodDef(
                            new PublicType(),
                            new IntType(),
                            new MethodName("myMethod"),
                            Arrays.asList(
                                new Vardec(new IntType(), new VariableExp("x")),
                                new Vardec(new BooleanType(), new VariableExp("x")),
                                new Vardec(new StringType(), new VariableExp("y"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new PrivateType(),
                            new StringType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                ),
                new ClassDef(
                    new ClassName("myClass"),
                    new ClassName(""),
                    new ArrayList<>(),
                    Collections.singletonList(
                        new MethodDef(
                            new ProtectedType(),
                            new BooleanType(),
                            new MethodName("myMethod"),
                            Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                            new BlockStmt(
                                Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                            )
                        )
                    ),
                    new ArrayList<>()
                )
            ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test
    public void testThisFunction() throws ParserException, TokenizerException {
        // Test #52 - Checking:
        //      this.methodA(23)

        String input = "(this).methodA(23)";

        ParseResult<Exp> expected = new ParseResult<>(
            new FunctionCallExp(
                new MethodName("methodA"),
                new ThisExp(),
                Collections.singletonList(new IntegerExp(23))
            ),
            8
        );

        assertParses(tokenizes(input), expected);
    }

    @Test
    public void testVardecIntInitializeInt() throws ParserException, TokenizerException {
        // Test #54 - Checking:
        //      Int x = 4;

        String input = "Int x = 4;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new IntType(),
                    new VariableExp("x")
                ),
                new IntegerExp(4)
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecIntInitializeVariable() throws ParserException, TokenizerException {
        // Test #55 - Checking:
        //      Int x = y;

        String input = "Int x = y;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new IntType(),
                    new VariableExp("x")
                ),
                new VariableExp("y")
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }
    @Test(expected = ParserException.class)
    public void testVarDecError() throws TokenizerException, ParserException{
        String input = "Int x . 5";
        ParseResult<Stmt> expected = new ParseResult<>(new VardecStmt(new Vardec(new IntType(), new VariableExp("x")),new IntegerExp(5)),4);
        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecBoolInitializeInt() throws ParserException, TokenizerException {
        // Test #56 - Checking:
        //      Boolean x = true;

        String input = "Boolean x = true;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new BooleanType(),
                    new VariableExp("x")
                ),
                new BooleanLiteralExp(true)
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecBoolInitializeVariable() throws ParserException, TokenizerException {
        // Test #57 - Checking:
        //      Boolean x = y;

        String input = "Boolean x = y;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new BooleanType(),
                    new VariableExp("x")
                ),
                new VariableExp("y")
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecStringInitializeInt() throws ParserException, TokenizerException {
        // Test #58 - Checking:
        //      strg x = "hello";

        String input = "strg x = \"hello\";";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new StringType(),
                    new VariableExp("x")
                ),
                new StringExp("\"hello\"")
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecStringInitializeVariable() throws ParserException, TokenizerException {
        // Test #59 - Checking:
        //      strg x = y;

        String input = "strg x = y;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new StringType(),
                    new VariableExp("x")
                ),
                new VariableExp("y")
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecClassnameInitializeClassname() throws ParserException, TokenizerException {
        // Test #60 - Checking:
        //      classA x = new classA();

        String input = "classA x = new classA();";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new ClassNameType(new ClassName("classA")),
                    new VariableExp("x")
                ),
                new NewExp(new ClassName("classA"), new ArrayList<>())
            ),
            7
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecClassnameInitializeClassnameParams() throws ParserException, TokenizerException {
        // Test #61 - Checking:
        //      classA x = new classA(y, z);

        String input = "classA x = new classA(y, z);";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new ClassNameType(new ClassName("classA")),
                    new VariableExp("x")
                ),
                new NewExp(
                    new ClassName("classA"),
                    Arrays.asList(
                        new VariableExp("y"),
                        new VariableExp("z")
                    ))
            ),
            10
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test
    public void testVardecClassnameInitializeVariable() throws ParserException, TokenizerException {
        // Test #62 - Checking:
        //      classA x = y;

        String input = "classA x = y;";

        ParseResult<Stmt> expected = new ParseResult<>(
            new VardecStmt(
                new Vardec(
                    new ClassNameType(new ClassName("classA")),
                    new VariableExp("x")
                ),
                new VariableExp("y")
            ),
            4
        );

        assertParsesStmt(tokenizes(input), expected);
    }


    @Test(expected = ParserException.class)
    public void testVardecStringInitDecFail() throws ParserException, TokenizerException {
        //      strg x = 2;

        String input = "strg x 2;";

        ParseResult<Stmt> expected = new ParseResult<>(
                new VardecStmt(
                        new Vardec(
                                new StringType(),
                                new VariableExp("x")
                        ),
                        new IntegerExp(2)
                ),
                4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testVardecBooleanInitDecFail() throws ParserException, TokenizerException {
        //      Boolean x = 2;

        String input = "Boolean x 2;";

        ParseResult<Stmt> expected = new ParseResult<>(
                new VardecStmt(
                        new Vardec(
                                new BooleanType(),
                                new VariableExp("x")
                        ),
                        new IntegerExp(2)
                ),
                4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testMultipleMainMethods() throws ParserException, TokenizerException{
        // Test #51 - Checking:
        //      class mainClass {
        //          public Int main(){}
        //          public Int main(){}
        //      }
        //      class myClass extends classA {
        //          public Int myMethod(Int x) {
        //              print(0);
        //          }
        //          public Int myMethod(Int x, Boolean x, strg y) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          private strg myMethod(Int x) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          protected Boolean myMethod(Int x) {
        //              print(0);
        //          }
        //      }

        final String input =
                "class mainClass {" +
                        "public Int main(){} public Int main(){}" +
                        "}" +
                        "class myClass extends classA {" +
                        "public Int myMethod(Int x){" +
                        "print(0);" +
                        "}" +
                        "public Int myMethod(Int x, Boolean x, strg y) {" +
                        "print(0);" +
                        "}" +
                        "}" +
                        "class myClass {" +
                        "private strg myMethod(Int x) {" +
                        "print(0);" +
                        "}" +
                        "}" +
                        "class myClass {" +
                        "protected Boolean myMethod(Int x) {" +
                        "print(0);" +
                        "}" +
                        "}";

        Program expected = new Program(
                Arrays.asList(
                        new ClassDef(
                                new ClassName("mainClass"),
                                new ClassName(""),
                                new ArrayList<>(),
                                Collections.singletonList(
                                        new MethodDef(
                                                new PublicType(),
                                                new IntType(),
                                                new MethodName("main"),
                                                new ArrayList<>(),
                                                new BlockStmt(
                                                        new ArrayList<>()
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        ),
                        new ClassDef(
                                new ClassName("myClass"),
                                new ClassName("classA"),
                                new ArrayList<>(),
                                Arrays.asList(
                                        new MethodDef(
                                                new PublicType(),
                                                new IntType(),
                                                new MethodName("myMethod"),
                                                Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        ),
                                        new MethodDef(
                                                new PublicType(),
                                                new IntType(),
                                                new MethodName("myMethod"),
                                                Arrays.asList(
                                                        new Vardec(new IntType(), new VariableExp("x")),
                                                        new Vardec(new BooleanType(), new VariableExp("x")),
                                                        new Vardec(new StringType(), new VariableExp("y"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        ),
                        new ClassDef(
                                new ClassName("myClass"),
                                new ClassName(""),
                                new ArrayList<>(),
                                Collections.singletonList(
                                        new MethodDef(
                                                new PrivateType(),
                                                new StringType(),
                                                new MethodName("myMethod"),
                                                Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        ),
                        new ClassDef(
                                new ClassName("myClass"),
                                new ClassName(""),
                                new ArrayList<>(),
                                Collections.singletonList(
                                        new MethodDef(
                                                new ProtectedType(),
                                                new BooleanType(),
                                                new MethodName("myMethod"),
                                                Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        )
                ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testNoMainMethod() throws ParserException, TokenizerException{
        // Test #51 - Checking:
        //      class mainClass {
        //          public Int ourMethod(){}
        //      }
        //      class myClass extends classA {
        //          public Int myMethod(Int x) {
        //              print(0);
        //          }
        //          public Int myMethod(Int x, Boolean x, strg y) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          private strg myMethod(Int x) {
        //              print(0);
        //          }
        //      }
        //      class myClass {
        //          protected Boolean myMethod(Int x) {
        //              print(0);
        //          }
        //      }

        final String input =
                "class mainClass {" +
                        "public Int ourMethod(){}" +
                        "}" +
                        "class myClass extends classA {" +
                        "public Int myMethod(Int x){" +
                        "print(0);" +
                        "}" +
                        "public Int myMethod(Int x, Boolean x, strg y) {" +
                        "print(0);" +
                        "}" +
                        "}" +
                        "class myClass {" +
                        "private strg myMethod(Int x) {" +
                        "print(0);" +
                        "}" +
                        "}" +
                        "class myClass {" +
                        "protected Boolean myMethod(Int x) {" +
                        "print(0);" +
                        "}" +
                        "}";

        Program expected = new Program(
                Arrays.asList(
                        new ClassDef(
                                new ClassName("mainClass"),
                                new ClassName(""),
                                new ArrayList<>(),
                                Collections.singletonList(
                                        new MethodDef(
                                                new PublicType(),
                                                new IntType(),
                                                new MethodName("main"),
                                                new ArrayList<>(),
                                                new BlockStmt(
                                                        new ArrayList<>()
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        ),
                        new ClassDef(
                                new ClassName("myClass"),
                                new ClassName("classA"),
                                new ArrayList<>(),
                                Arrays.asList(
                                        new MethodDef(
                                                new PublicType(),
                                                new IntType(),
                                                new MethodName("myMethod"),
                                                Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        ),
                                        new MethodDef(
                                                new PublicType(),
                                                new IntType(),
                                                new MethodName("myMethod"),
                                                Arrays.asList(
                                                        new Vardec(new IntType(), new VariableExp("x")),
                                                        new Vardec(new BooleanType(), new VariableExp("x")),
                                                        new Vardec(new StringType(), new VariableExp("y"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        ),
                        new ClassDef(
                                new ClassName("myClass"),
                                new ClassName(""),
                                new ArrayList<>(),
                                Collections.singletonList(
                                        new MethodDef(
                                                new PrivateType(),
                                                new StringType(),
                                                new MethodName("myMethod"),
                                                Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        ),
                        new ClassDef(
                                new ClassName("myClass"),
                                new ClassName(""),
                                new ArrayList<>(),
                                Collections.singletonList(
                                        new MethodDef(
                                                new ProtectedType(),
                                                new BooleanType(),
                                                new MethodName("myMethod"),
                                                Collections.singletonList(new Vardec(new IntType(), new VariableExp("x"))),
                                                new BlockStmt(
                                                        Collections.singletonList(new PrintStmt(new IntegerExp(0)))
                                                )
                                        )
                                ),
                                new ArrayList<>()
                        )
                ), new BlockStmt(new ArrayList<>())
        );

        assertParseProgram(tokenizes(input), expected);
    }

    @Test(expected = ParserException.class)
    public void testCommaFail() throws ParserException, TokenizerException {
        //      Boolean x = 2;

        String input = "a b.";

        ParseResult<Stmt> expected = new ParseResult<>(
                new VardecStmt(
                        new Vardec(
                                new BooleanType(),
                                new VariableExp("x")
                        ),
                        new IntegerExp(2)
                ),
                4
        );

        assertParsesStmt(tokenizes(input), expected);
    }

}
