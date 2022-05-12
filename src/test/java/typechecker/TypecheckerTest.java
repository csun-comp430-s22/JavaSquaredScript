package typechecker;

import lexer.Tokenizer;
import lexer.TokenizerException;
import lexer.tokens.Token;
import org.junit.Test;
import parser.*;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TypecheckerTest {

    public static Typechecker emptyTypechecker() throws TypeErrorException{
        return new Typechecker(new Program(new ArrayList<ClassDef>(),new BlockStmt(new ArrayList<>())));
    }


    
    public static final Map<VariableExp, Type> emptyTypeEnvironment = new HashMap<VariableExp, Type>();
    public List<Token> tokenizes(final String input) throws TokenizerException {
        final Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        return received;
    }
    public static Typechecker methodCallTypechecker() throws TypeErrorException,TokenizerException, ParserException{
        final String input = "class A {public Int main(){} constructor(){} }";
        final Tokenizer tokenizer  = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        final Parser parser = new Parser(received);
        return new Typechecker(parser.parseProgram());
    }

    @Test
    public void testTypeofBooleanTrue() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofBooleanFalse() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "false";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofInteger() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofString() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "\"hello\"";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new StringType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofAddInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5+5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofAddIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5+true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofSubInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5-5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofSubIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5-true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofMultInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5*5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofMultIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5*true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofDivInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5/5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofDivIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5/true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofLessInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5<5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofLessIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5<true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofGreaterInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5>5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofGreaterIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5>true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofDoubleEqualInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5==5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofDoubleEqualBoolean() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "true==false";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofNotEqualIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5==true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofNotEqualInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5!=5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofNotEqualBoolean() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "true!=false";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofDoubleEqualIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5!=true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), emptyTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }

    @Test
    public void testTypeofInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "Int x;";
        final Parser parser = new Parser(tokenizes(input));
        emptyTypeEnvironment.put(new VariableExp("x"), new IntType());
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName(""))));
    }
    @Test
    public void testTypeofWhile() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "while(true){}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName(""))));
    }
    @Test
    public void testTypeofIf() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "if(true){}else{}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName(""))));
    }
    @Test
    public void testTypeofPrint() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "print(5);";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName(""))));
    }
    @Test
    public void testTypeofIntWithIf() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "Int x;" +
                "if(1==1){}else{}";
        final Parser parser = new Parser(tokenizes(input));
        emptyTypeEnvironment.put(new VariableExp("x"), new IntType());
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName(""))));
    }
    @Test
    public void testTypeofClass() throws TypeErrorException, ParserException, TokenizerException{
        assertEquals(new ClassNameType(new ClassName("foo")), emptyTypechecker().typeofThis(new ClassName("foo")));
    }
    @Test(expected = TypeErrorException.class)
    public void testThisNotInClass() throws TypeErrorException {
        emptyTypechecker().typeofThis(null);
    }
    @Test
    public void testVariableInScope() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<VariableExp, Type> typeEnvironment = new HashMap<VariableExp, Type>();
        typeEnvironment.put(new VariableExp("x"), new IntType());

        final Type receivedType =
                emptyTypechecker().typeofVariable(new VariableExp("x"),
                        typeEnvironment);
        assertEquals(expectedType, receivedType);
    }

    @Test(expected = TypeErrorException.class)
    public void testVariableOutOfScope() throws TypeErrorException {
        final Map<VariableExp, Type> typeEnvironment = new HashMap<VariableExp, Type>();
        emptyTypechecker().typeofVariable(new VariableExp("x"),
                typeEnvironment);
    }
    @Test
    public void testTypeofReturn() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{return x;}";
        final Parser parser = new Parser(tokenizes(input));
        emptyTypeEnvironment.put(new VariableExp("x"), new IntType());
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }
    @Test
    public void testTypeofBreak() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{break;}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }
    @Test
    public void testTypeofInitInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{Int x = 5;}";
        final Parser parser = new Parser(tokenizes(input));
        emptyTypeEnvironment.put(new VariableExp("x"), new IntType());
        assertEquals(emptyTypeEnvironment, methodCallTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }
    @Test
    public void testTypeofInitializedBoolean() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{Boolean x = true;}";
        final Parser parser = new Parser(tokenizes(input));
        emptyTypeEnvironment.put(new VariableExp("x"), new BooleanType());
        assertEquals(emptyTypeEnvironment, methodCallTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }
    @Test
    public void testTypeofInitString() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{strg x = \"hello\";}";
        final Parser parser = new Parser(tokenizes(input));
        emptyTypeEnvironment.put(new VariableExp("x"), new StringType());
        assertEquals(emptyTypeEnvironment, methodCallTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }

    @Test(expected = TypeErrorException.class)
    public void testDuplicateClasses() throws TokenizerException, ParserException, TypeErrorException {
        final String input = "class mainClass{public Int main(){}} class A{} class A{}";
        final Parser parser = new Parser(tokenizes(input));

        Typechecker typechecker = emptyTypechecker();

        typechecker.classes.put(
            new ClassName("mainClass"),
            new ClassDef(
                new ClassName("mainClass"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
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
            )
        );

        typechecker.classes.put(
            new ClassName("A"),
            new ClassDef(
                new ClassName("A"),
                new ClassName(""),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            )
        );

        assertEquals(typechecker.classes, new Typechecker(parser.parseProgram()).classes);
    }

    @Test
    public void testMultipleClasses() throws TokenizerException, ParserException, TypeErrorException {
        final String input = "class mainClass{public Int main(){}} class A{}";
        final Parser parser = new Parser(tokenizes(input));

        Typechecker typechecker = emptyTypechecker();

        typechecker.classes.put(
            new ClassName("mainClass"),
            new ClassDef(
                new ClassName("mainClass"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
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
            )
        );

        typechecker.classes.put(
            new ClassName("A"),
            new ClassDef(
                new ClassName("A"),
                new ClassName(""),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            )
        );

        assertEquals(typechecker.classes, new Typechecker(parser.parseProgram()).classes);
    }

    @Test
    public void testSingleMethod() throws TokenizerException, TypeErrorException, ParserException {
        final String input = "class mainClass {public Int main(){}}"  +
            "class A{public Int method(Int x, Boolean y){print(20);}}";
        final Parser parser = new Parser(tokenizes(input));

        Typechecker typechecker = emptyTypechecker();

        typechecker.classes.put(
            new ClassName("mainClass"),
            new ClassDef(
                new ClassName("mainClass"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
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
            )
        );

        typechecker.classes.put(
            new ClassName("A"),
            new ClassDef(
                new ClassName("A"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
                    new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("method"),
                        Arrays.asList(
                            new Vardec(new IntType(), new VariableExp("x")),
                            new Vardec(new BooleanType(), new VariableExp("y"))
                        ),
                        new BlockStmt(
                            Arrays.asList(
                                new PrintStmt(new IntegerExp(20))
                            )
                        )
                    )
                ),
                new ArrayList<>()
            )
        );

        assertEquals(typechecker.classes, new Typechecker(parser.parseProgram()).classes);
    }

    @Test(expected = TypeErrorException.class)
    public void testDuplicateMethods() throws TypeErrorException, TokenizerException, ParserException {
        final String input = "class mainClass{public Int main(){}} class A{public Int method(){} public Int" +
            " method(){}}";
        final Parser parser = new Parser(tokenizes(input));

        Typechecker typechecker = emptyTypechecker();

        typechecker.classes.put(
            new ClassName("mainClass"),
            new ClassDef(
                new ClassName("mainClass"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
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
            )
        );

        typechecker.classes.put(
            new ClassName("A"),
            new ClassDef(
                new ClassName("A"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
                    new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("method"),
                        new ArrayList<>(),
                        new BlockStmt(
                            new ArrayList<>()
                        )
                    ),
                    new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("method"),
                        new ArrayList<>(),
                        new BlockStmt(
                            new ArrayList<>()
                        )
                    )
                ),
                new ArrayList<>()
            )
        );

        assertEquals(typechecker.classes, new Typechecker(parser.parseProgram()).classes);
    }

    @Test
    public void testMultipleMethods() throws TypeErrorException, TokenizerException, ParserException {
        final String input = "class mainClass{public Int main(){}} class A{public Int method1(){} public " +
            "Int method2(){}}";
        final Parser parser = new Parser(tokenizes(input));

        Typechecker typechecker = emptyTypechecker();

        typechecker.classes.put(
            new ClassName("mainClass"),
            new ClassDef(
                new ClassName("mainClass"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
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
            )
        );

        typechecker.classes.put(
            new ClassName("A"),
            new ClassDef(
                new ClassName("A"),
                new ClassName(""),
                new ArrayList<>(),
                Arrays.asList(
                    new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("method1"),
                        new ArrayList<>(),
                        new BlockStmt(
                            new ArrayList<>()
                        )
                    ),
                    new MethodDef(
                        new PublicType(),
                        new IntType(),
                        new MethodName("method2"),
                        new ArrayList<>(),
                        new BlockStmt(
                            new ArrayList<>()
                        )
                    )
                ),
                new ArrayList<>()
            )
        );

        assertEquals(typechecker.classes, new Typechecker(parser.parseProgram()).classes);
    }

    @Test
    public void testMethodCall() throws TokenizerException, TypeErrorException, ParserException {
        final String input = "(this).method()";
        final Parser parser = new Parser(tokenizes(input));


        //assertEquals(emptyTypeEnvironment, emptyTypechecker()(parser.parseStmt(0).result,
        //emptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName(""))));

    }
}
