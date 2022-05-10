package typechecker;

import lexer.Tokenizer;
import lexer.TokenizerException;
import lexer.tokens.Token;
import org.junit.Test;
import parser.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TypecheckerTest {
    
    public static final Map<VariableExp, Type> emptyTypeEnvironment = new HashMap<VariableExp, Type>();
    public static Type typeof(final Exp exp) throws TypeErrorException{
        final Typechecker emptyTypechecker = new Typechecker(new Program(new ArrayList<ClassDef>()));
        return emptyTypechecker.typeof(exp, emptyTypeEnvironment, new ClassName(""));
    }
    public static Map<VariableExp, Type> isWellTypedStmt(final Stmt stmt) throws TypeErrorException{
        final Typechecker emptyTypechecker = new Typechecker(new Program(new ArrayList<ClassDef>()));
        return emptyTypechecker.isWellTypedStmt(stmt, emptyTypeEnvironment,new ClassName(""), new ClassNameType(new ClassName("")));
    }
    public List<Token> tokenizes(final String input) throws TokenizerException {
        final Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        return received;
    }

    @Test
    public void testTypeofBooleanTrue() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofBooleanFalse() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "false";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofInteger() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofString() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "\"hello\"";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new StringType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofAddInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5+5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofAddIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5+true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofSubInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5-5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofSubIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5-true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofMultInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5*5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofMultIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5*true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofDivInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5/5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofDivIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5/true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new IntType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofLessInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5<5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofLessIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5<true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofGreaterInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5>5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofGreaterIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5>true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }

    @Test
    public void testTypeofDoubleEqualInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5==5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofDoubleEqualBoolean() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "true==false";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofNotEqualIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5==true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofNotEqualInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5!=5";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test
    public void testTypeofNotEqualBoolean() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "true!=false";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }
    @Test (expected= TypeErrorException.class)
    public void testTypeofDoubleEqualIntError() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "5!=true";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(new BooleanType(), typeof(parser.parseExp(0).result));
    }

    @Test
    public void testTypeofInt() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "Int x;";
        final Parser parser = new Parser(tokenizes(input));
        emptyTypeEnvironment.put(new VariableExp("x"), new IntType());
        assertEquals(emptyTypeEnvironment, isWellTypedStmt(parser.parseStmt(0).result));
    }
    @Test
    public void testTypeofWhile() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "while(true){}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, isWellTypedStmt(parser.parseStmt(0).result));
    }
    @Test
    public void testTypeofIf() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "if(true){}else{}";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, isWellTypedStmt(parser.parseStmt(0).result));
    }
    @Test
    public void testTypeofPrint() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "print(5);";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, isWellTypedStmt(parser.parseStmt(0).result));
    }
    /*
    @Test
    public void testTypeofReturn() throws TypeErrorException, ParserException, TokenizerException{
        final String input = "return x;";
        final Parser parser = new Parser(tokenizes(input));
        assertEquals(emptyTypeEnvironment, isWellTypedStmt(parser.parseStmt(0).result));
    }
    */
}
