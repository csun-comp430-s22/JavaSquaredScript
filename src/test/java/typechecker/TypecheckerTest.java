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
        //System.out.println(parser.parseProgram());
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
    public void testTypeofClass() throws TypeErrorException, ParserException, TokenizerException{
        assertEquals(new ClassNameType(new ClassName("foo")), emptyTypechecker().typeofThis(new ClassName("foo")));
    }
    @Test(expected = TypeErrorException.class)
    public void testThisNotInClass() throws TypeErrorException {
        emptyTypechecker().typeofThis(null);
    }

    @Test(expected = TypeErrorException.class)
    public void testVariableOutOfScope() throws TypeErrorException {
        final Map<VariableExp, Type> typeEnvironment = new HashMap<VariableExp, Type>();
        emptyTypechecker().typeofVariable(new VariableExp("x"),
                typeEnvironment);
    }
    @Test
    public void testVariableInitBool() throws TypeErrorException, ParserException, TokenizerException {
        final String input = "Boolean type = true";
        final Map<VariableExp, Type> stmt = new HashMap<>();
        stmt.put(new VariableExp("type"), new BooleanType());
        final Map<VariableExp, Type> receivedStmt = emptyTypechecker().isWellTypedStmt(new Parser(tokenizes(input)).parseStmt(0).result,
                emptyTypeEnvironment,
                new ClassName(""),
                new ClassNameType(new ClassName("")));
        assertEquals(stmt, receivedStmt);
    }
    @Test
    public void testVariableInitInt() throws TypeErrorException, ParserException, TokenizerException {
        final String input = "Int x=1;";
        final Map<VariableExp, Type> stmt = new HashMap<>();
        stmt.put(new VariableExp("x"), new IntType());
        final Map<VariableExp, Type> receivedStmt = emptyTypechecker().isWellTypedStmt(new Parser(tokenizes(input)).parseStmt(0).result,
                emptyTypeEnvironment,
                new ClassName(""),
                new ClassNameType(new ClassName("")));

        assertEquals(stmt, receivedStmt);
    }
    @Test
    public void testVariableInitString() throws TypeErrorException, ParserException, TokenizerException {
        final String input = "strg init =\"hello\";";
        final Map<VariableExp, Type> stmt = new HashMap<>();
        stmt.put(new VariableExp("init"), new StringType());
        final Map<VariableExp, Type> receivedStmt = emptyTypechecker().isWellTypedStmt(new Parser(tokenizes(input)).parseStmt(0).result,
                emptyTypeEnvironment,
                new ClassName(""),
                new ClassNameType(new ClassName("")));

        assertEquals(stmt, receivedStmt);
    }
    @Test
    public void testTypeofInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "Int x;";
        final Map<VariableExp,Type> stmt = new HashMap<>();
        stmt.put(new VariableExp("x"),new IntType());
        final Map<VariableExp,Type> notEmptyTypeEnvironment = new HashMap<>();
        notEmptyTypeEnvironment.put(new VariableExp("x"), new IntType());
        final Map<VariableExp, Type> receivedStmt = emptyTypechecker().isWellTypedStmt(new Parser(tokenizes(input)).parseStmt(0).result,
                notEmptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName("")));
        assertEquals(stmt, receivedStmt);
    }

    @Test
    public void testTypeofReturn() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{return p;}";
        final Map<VariableExp, Type> stmt = new HashMap<>();
        stmt.put(new VariableExp("p"), new IntType());
        final Map<VariableExp, Type> notEmptyTypeEnvironment = new HashMap<>();
        notEmptyTypeEnvironment.put(new VariableExp("p"), new IntType());
        final Map<VariableExp, Type> receiveStmt = emptyTypechecker().isWellTypedStmt(new Parser(tokenizes(input)).parseStmt(0).result,
                notEmptyTypeEnvironment, new ClassName(""), new IntType());
        assertEquals(stmt, receiveStmt);
    }
    @Test
    public void testTypeofBreak() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{break;}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }
    @Test
    public void testTypeofWhile() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "while(true){}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }
    @Test
    public void test() throws TypeErrorException, ParserException, TokenizerException{
       final String input = "new A();";
       final Type expectedType = new ClassNameType(new ClassName("A"));
       final Parser parser = new Parser(tokenizes(input));
       assertEquals(expectedType, methodCallTypechecker().typeof(parser.parseExp(0).result,emptyTypeEnvironment,new ClassName("")));
    }
    @Test
    public void testTypeofInitInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{Int x = 5;}";
        final Parser parser = new Parser(tokenizes(input));
        final Map<VariableExp,Type> notEmptyTypeEnvironment = new HashMap<>();
        notEmptyTypeEnvironment.put(new VariableExp("x"), new IntType());
        assertEquals(notEmptyTypeEnvironment, methodCallTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }
    @Test
    public void testA() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{A a = new A();}";
        final Type expectedType = new ClassNameType(new ClassName("A"));
        final Map<VariableExp, Type> stmt = new HashMap<>();
        stmt.put(new VariableExp("a"), new ClassNameType(new ClassName("A")));
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(stmt,methodCallTypechecker().isWellTypedStmt(parser.parseStmt(0).result,emptyTypeEnvironment, new ClassName(""), new ClassNameType(new ClassName(""))));
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
    public void testMethodCall() throws TypeErrorException {
        HashMap<MethodName, MethodDef> hashMap = new HashMap<>();
        hashMap.put(
            new MethodName("method"),
            new MethodDef(
                new PublicType(),
                new IntType(),
                new MethodName("method"),
                new ArrayList<>(),
                new BlockStmt(
                    new ArrayList<>()
                )
            )
        );

        Typechecker typechecker = emptyTypechecker();
        typechecker.methods.put(
            new ClassName("myClass"),
            hashMap
        );

        assertEquals(
            new IntType(),
            typechecker.typeof(new FunctionCallExp(new MethodName("method"), new ThisExp(),
                new ArrayList<>()), emptyTypeEnvironment, new ClassName("myClass"))
        );

    }

}
