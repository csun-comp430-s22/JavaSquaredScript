package typechecker;
import lexer.Tokenizer;
import lexer.TokenizerException;
import lexer.tokens.Token;
import org.junit.Test;
import parser.*;
import parser.AccesModTypes.PublicType;
import parser.Declarations.Vardec;
import parser.Def.ClassDef;
import parser.Def.MethodDef;
import parser.ExpCalls.*;
import parser.Names.ClassName;
import parser.Names.MethodName;
import parser.OpCalls.EqualsOp;
import parser.ReturnTypes.BooleanType;
import parser.ReturnTypes.ClassNameType;
import parser.ReturnTypes.IntType;
import parser.ReturnTypes.StringType;
import parser.StmtCalls.BlockStmt;
import parser.StmtCalls.MainStmt;
import parser.StmtCalls.PrintStmt;
import parser.interfaces.Exp;
import parser.interfaces.Type;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TypecheckerTest {

    public static Typechecker emptyTypechecker() throws TypeErrorException{
        return new Typechecker(new Program(new ArrayList<ClassDef>(),new MainStmt(new ClassName(""))));
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
    @Test(expected = TypeErrorException.class)
    public void testTypeofWhileError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "while(1){}";
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
    public void testIfStmt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "if(true){}else{}";
        Parser parser = new Parser(tokenizes(input));
        final Map<VariableExp, Type> empty = new HashMap<>();
        assertEquals(empty, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result,emptyTypeEnvironment,new ClassName(""), new ClassNameType(new ClassName(""))));
    }
    @Test(expected = TypeErrorException.class)
    public void testIfStmtError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "if(2){}else{}";
        Parser parser = new Parser(tokenizes(input));
        final Map<VariableExp, Type> empty = new HashMap<>();
        assertEquals(empty, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result,emptyTypeEnvironment,new ClassName(""), new ClassNameType(new ClassName(""))));
    }
    @Test
    public void testPrintStmt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "print(5);";
        Parser parser = new Parser(tokenizes(input));
        final Map<VariableExp, Type> empty = new HashMap<>();
        assertEquals(empty, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result,emptyTypeEnvironment,new ClassName(""), new ClassNameType(new ClassName(""))));
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
    public static Typechecker typeCheckerClass() throws TokenizerException, ParserException, TypeErrorException{
        final String input = "class mainClass{public Int x; constructor(Boolean y){} private Int test(){print(5);}public Int main(){} public Boolean test(Int a){}} " +
                "class A extends mainClass{} class C extends mainClass{}";
        Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        Parser parser = new Parser(received);
        return new Typechecker(parser.parseProgram());
    }
    @Test(expected = TypeErrorException.class)
    public void testingMethodCalls() throws TokenizerException, ParserException, TypeErrorException{
        final Map<VariableExp, Type> typeEnv = new HashMap<>();
        typeEnv.put(new VariableExp("dog"), new ClassNameType(new ClassName("mainClass")));
        Exp target = new VariableExp("dog");
        MethodName methodName = new MethodName("testing");
        List<Exp> params = new ArrayList<>();
        params.add(new IntegerExp(23));
        final Type receivedType = typeCheckerClass().typeof(new FunctionCallExp(methodName,target,params),typeEnv,new ClassName(""));
    }
    @Test
    public void testNewExp()throws TokenizerException, ParserException, TypeErrorException{
        final Type expected = new ClassNameType(new ClassName("mainClass"));
        final Map<VariableExp, Type> typeEnv = new HashMap<>();
        ClassName className = new ClassName("mainClass");
        List<Exp> params = new ArrayList<>();
        params.add(new BooleanLiteralExp(true));
        final Type receivedType = typeCheckerClass().typeof(new NewExp(className,params),typeEnv,new ClassName(""));
        assertEquals(expected,receivedType);
    }
    @Test(expected = TypeErrorException.class)
    public void testingNoExtends()throws TokenizerException, ParserException, TypeErrorException{
        final String input = "class A extends B{public Int main(){}}";
        Parser parser = new Parser(tokenizes(input));
        new Typechecker(parser.parseProgram());
        //System.out.println();
    }

    @Test(expected = TypeErrorException.class)
    public void testFunctionCallExp()throws TokenizerException, ParserException, TypeErrorException{
        final Type type = new IntType();
        final Map<VariableExp, Type> typeEnv = new HashMap<>();
        typeEnv.put(new VariableExp("dog"), new ClassNameType(new ClassName("mainClass")));
        Exp target = new VariableExp("dog");
        MethodName methodName = new MethodName("test");
        List<Exp> params = new ArrayList<>();
        final Type receivedType = typeCheckerClass().typeof(new FunctionCallExp(methodName, target, params), typeEnv,new ClassName(""));
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
        final String input =
            "class mainClass { " +
                "public Int main(){}" +
            "} " +
            "class A{}" +
            "class B extends A{}";

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

        typechecker.classes.put(
            new ClassName("B"),
            new ClassDef(
                new ClassName("B"),
                new ClassName("A"),
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
    public void testThisInClass() throws TypeErrorException {
        assertEquals(new ClassNameType(new ClassName("foo")),
                emptyTypechecker().typeofThis(new ClassName("foo")));
    }
    @Test
    public void testThis() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "this";
        Parser parser = new Parser(tokenizes(input));
        final Type type = new ClassNameType(new ClassName(""));
        assertEquals(type, emptyTypechecker().typeof(parser.parseExp(0).result, emptyTypeEnvironment,new ClassName("")));
    }
    public static Typechecker typechecker() throws ParserException,TypeErrorException, TokenizerException{
        final String input = "class mainClass{ public Int main(){} constructor(strg y){print(true);}" +
                "public Int testMethod(){} public Boolean testMethod(Int y){}} class A extends mainClass{}";
        Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        Parser parser = new Parser(received);
        //System.out.println(parser.parseProgram());
        return new Typechecker(parser.parseProgram());
    }
    @Test(expected = TypeErrorException.class)
    public void testDuplicate() throws TypeErrorException,ParserException,TokenizerException{
        final Type expectedType = new IntType();
        final Map<VariableExp, Type> typeEnv = new HashMap<>();
        typeEnv.put(new VariableExp("dog"), new ClassNameType(new ClassName("mainClass")));
        Exp target = new VariableExp("dog");
        MethodName methodName = new MethodName("testMethod");
        List<Exp> params = new ArrayList<>();
        params.add(new BooleanLiteralExp(true));
        final Type receivedType = typechecker().typeof(new FunctionCallExp(methodName,target,params),typeEnv,new ClassName("mainClass"));
    }
    @Test
    public void testMethod()throws TypeErrorException,ParserException,TokenizerException{
        final Type expectedType = new BooleanType();
        final Map<VariableExp, Type> typeEnv = new HashMap<>();
        typeEnv.put(new VariableExp("dog"), new ClassNameType(new ClassName("mainClass")));
        Exp target = new VariableExp("dog");
        MethodName methodName = new MethodName("test");
        List<Exp> params = new ArrayList<>();
        params.add(new IntegerExp(1));
        final Type receivedType = typeCheckerClass().typeof(new FunctionCallExp(methodName,target,params),typeEnv,new ClassName("A"));
        assertEquals(expectedType,receivedType);
    }

    @Test
    public void testSubtyping() throws TypeErrorException,ParserException,TokenizerException{
        typeCheckerClass().assertEqualOrSubtypeOf(new ClassNameType(new ClassName("C")),new ClassNameType(new ClassName("mainClass")));
    }
    @Test (expected = TypeErrorException.class)
    public void testWrongExpression() throws TypeErrorException{
        final Type type = emptyTypechecker().typeof(new ClassNameExp("A"),emptyTypeEnvironment,new ClassName(""));
    }
    @Test
    public void testClass()throws TypeErrorException,ParserException,TokenizerException{
        final String input = "class A{" +
                                "private Int a; " +
                                "public Int b;" +
                                "constructor(Int x){} constructor(Boolean y){} " +
                                "public Int main(){} public Boolean test(Int x){print(5);}}";
        Parser parser = new Parser(tokenizes(input));
        new Typechecker(parser.parseProgram()).isWellTypedProgram();
    }

    @Test (expected = TypeErrorException.class)
    public void testOpError() throws TypeErrorException,ParserException,TokenizerException{
        final Type type = emptyTypechecker().typeof(new OpExp(new IntegerExp(1), new EqualsOp(), new IntegerExp(5)),emptyTypeEnvironment,new ClassName(""));
    }
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

    @Test
    public void testTypeofIf() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "if(true){}else{}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }

    @Test
    public void testTypeofPrint() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "{print(0);}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, emptyTypechecker().isWellTypedStmt(parser.parseStmt(0).result, emptyTypeEnvironment, new ClassName(""), new IntType()));
    }

    @Test
    public void testInstanceVariables() throws TypeErrorException, TokenizerException, ParserException {

        final String input =
            "class mainClass {" +
                "public Int main(){}" +
            "}" +
            "class myClass {" +
                "public Int a;" +
                "public Int b;" +
                "public Int c;" +
            "}";

        Typechecker typechecker = new Typechecker(new Parser(tokenizes(input)).parseProgram());

        HashMap<VariableExp, Type> expected = new HashMap<>();
        expected.put(
            new VariableExp("a"),
            new IntType()
        );
        expected.put(
            new VariableExp("b"),
            new IntType()
        );
        expected.put(
            new VariableExp("c"),
            new IntType()
        );

        assertEquals(expected, typechecker.baseTypeEnvironmentForClass(new ClassName("myClass")));
    }
}
