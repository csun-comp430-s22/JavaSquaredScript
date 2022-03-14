package lexer;

import java.lang.annotation.Target;
import java.util.List;

import javax.swing.tree.VariableHeightLayoutCache;

import java.beans.Transient;
import java.io.*;
import java.util.*;

import lexer.tokens.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class TokenizerTest {
    public void assertTokenizes(final String input, final Token[] expected) throws TokenizerException{
        final Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        assertArrayEquals(expected,received.toArray(new Token[received.size()]));
    }

    @Test
    public void testEmptyString() throws TokenizerException{
        // Test # 1
        // check that tokenizing empty string works
        // Tokenizer tokenizer = new Tokenizer("");
        // List<Token> tokens = tokenizer.tokenize();
        // assertEquals(0, tokens.size());
        assertTokenizes("", new Token[0]);
    }

    @Test
    public void testOnlyWhitespace() throws TokenizerException{
        // Test #2 (checking whitespace)
        assertTokenizes("   ", new Token[0]);
    }
    @Test
    public void testTrueByItself() throws TokenizerException{
        // Test #3 (checking true token)
        assertTokenizes("true", new Token[] {new TrueToken()});
    }
    @Test
    public void testFalseByItself() throws TokenizerException{
        // Test #4 (checking false token)
        assertTokenizes("false", new Token[] {new FalseToken()});
    }
    @Test
    public void testLeftParenByItself() throws TokenizerException{
        // Test #5 (checking LeftParen token)
        assertTokenizes("(", new Token[] {new LeftParenToken()});
    }
    @Test
    public void testRightParenByItself() throws TokenizerException{
        // Test #6 (checking RightParen token)
        assertTokenizes(")", new Token[] {new RightParenToken()});
    }
     @Test
     public void testLeftCurlyByItself() throws TokenizerException{
         // Test #7 (checking LeftCurly token)
        assertTokenizes("{", new Token[] {new LeftCurlyToken()});
    }
    @Test
    public void testRightCurlyByItself() throws TokenizerException{
        // Test #8 (checking RightCurly token)
        assertTokenizes("}", new Token[] {new RightCurlyToken()});
    }
    @Test
    public void testStringByItself() throws TokenizerException{
        // Test #9 (checking String token)
        assertTokenizes("strg", new Token[] {new StringToken()});
    }
    @Test
    public void testBooleanByItself() throws TokenizerException{
        // Test #10 (checking Boolean token)
        assertTokenizes("Boolean", new Token[] {new BooleanToken()});
    }
    @Test
    public void testIntByItself() throws TokenizerException{
        // Test #11 (checking Int token)
        assertTokenizes("Int", new Token[] {new IntegerToken()});
    }
    @Test
    public void testThisByItself() throws TokenizerException{
        // Test #12 (checking This token)
        assertTokenizes("this", new Token[] {new ThisToken()});
    }
    @Test
    public void testPrintByItself() throws TokenizerException{
        // Test #13 (checking Print Token)
        assertTokenizes("print", new Token[] {new PrintToken()});
    }
    @Test
    public void testBreakByItself() throws TokenizerException{
        // Test #14 (checking break token)
        assertTokenizes("break", new Token[] {new BreakToken()});
    }
    @Test
    public void testSemiColonByItself() throws TokenizerException{
        // Test #15 (checking ; token)
        assertTokenizes(";", new Token[] {new SemiColonToken()});
    }
    @Test
    public void testReturnByItself() throws TokenizerException{
        // Test #16 (checking return token)
        assertTokenizes("return", new Token[] {new ReturnToken()});
    }
    @Test
    public void testNewByItself() throws TokenizerException{
        // Test #17 (checking new token)
        assertTokenizes("new", new Token[] {new NewToken()});
    }
    @Test
    public void testPublicByItself() throws TokenizerException{
        // Test #18 (checking public token)
        assertTokenizes("public", new Token[] {new PublicToken()});
    }
    @Test
    public void testProtectedByItself() throws TokenizerException{
        // Test #19 (checking protected token)
        assertTokenizes("protected", new Token[] {new ProtectedToken()});
    }
    @Test
    public void testPrivateByItself() throws TokenizerException{
        // Test #20 (checking private token)
        assertTokenizes("private", new Token[] {new PrivateToken()});
    }
    @Test
    public void testVariableByItself() throws TokenizerException{
        // Test #21 (checking variable token)
        assertTokenizes("foo", new Token[] {new VariableToken("foo")});
    }
    @Test 
    public void testStringValueToken() throws TokenizerException{
        // Test #22 (checking Empty String)
        assertTokenizes("\"\"", new Token[]{new StringValueToken("\"\"")});
    }
    @Test 
    public void testPlusTokenByItself() throws TokenizerException{
        // Test #23 (checking plus Token)
        assertTokenizes("+", new Token[]{new PlusToken()});
    }
    @Test 
    public void testMinusTokenByItself() throws TokenizerException{
        // Test #24 (checking minus Token)
        assertTokenizes("-", new Token[]{new MinusToken()});
    }
    @Test 
    public void testTimesTokenByItself() throws TokenizerException{
        // Test #25 (checking times Token)
        assertTokenizes("*", new Token[]{new TimesToken()});
    }
    @Test 
    public void testDivisionTokenByItself() throws TokenizerException{
        // Test #26 (checking division Token)
        assertTokenizes("/", new Token[]{new DivisionToken()});
    }
    @Test 
    public void testGreaterThanTokenByItself() throws TokenizerException{
        // Test #27 (checking greater-than Token)
        assertTokenizes(">", new Token[]{new GreaterThanToken()});
    }
    @Test 
    public void testLessThanTokenByItself() throws TokenizerException{
        // Test #28 (checking less-than Token)
        assertTokenizes("<", new Token[]{new LessThanToken()});
    }
    @Test 
    public void testEqualsTokenByItself() throws TokenizerException{
        // Test #29 (checking equals Token)
        assertTokenizes("=", new Token[]{new EqualsToken()});
    }
    @Test 
    public void testDoubleEqualsTokenByItself() throws TokenizerException{
        // Test #30 (checking double-equals Token)
        assertTokenizes("==", new Token[]{new DoubleEqualsToken()});
    }
    @Test
    public void testNotEqualsTokenByItself() throws TokenizerException{
        // Test #31 (checking not-equals Token)
        assertTokenizes("!=", new Token[]{new NotEqualsToken()});
    }
    @Test 
    public void testExtendsTokenByItself() throws TokenizerException{
        // Test #32 (checking extends Token)
        assertTokenizes("extends", new Token[]{new ExtendsToken()});
    }
    @Test 
    public void testClassTokenByItself() throws TokenizerException{
        // Test #33 (checking class Token)
        assertTokenizes("class", new Token[]{new ClassToken()});
    }
    @Test
    public void testIfParenTrueTokens() throws TokenizerException{
        // Test #34 (checking if(true) token)
        assertTokenizes("if(true)", new Token[] {new IfToken(),new LeftParenToken(),new TrueToken(),new RightParenToken()});
    }
    @Test
    public void testIfParenFalseTokens() throws TokenizerException{
        // Test #35 (checking if(false) token)
        assertTokenizes("if(false)", new Token[] {new IfToken(),new LeftParenToken(),new FalseToken(),new RightParenToken()});
    }
    @Test
    public void testReturnSemiTokens() throws TokenizerException{
        // Test #36 (checking return; token)
        assertTokenizes("if;", new Token[] {new IfToken(), new SemiColonToken()});
    }
    @Test
    public void testWhileTokens() throws TokenizerException{
        // Test #37 (checking while token)
        assertTokenizes("while", new Token[] {new WhileToken()});
    }

    @Test
    public void testIntegerTokens() throws TokenizerException{
        // Test #38 (checking return; token)
        assertTokenizes("1", new Token[] {new NumbersToken(1)});
    }
    @Test
    public void testMultipleIntegerTokens() throws TokenizerException{
        // Test #39 (checking return; token)
        assertTokenizes("123", new Token[] {new NumbersToken(123)});
    }
    @Test
    public void testMultipleTokens() throws TokenizerException{
        // Test #40 (checking if(true){strg i = "hello"})
        assertTokenizes("if(true){strg i = \"hello\"}", new Token[] {new IfToken(), new LeftParenToken(), 
            new TrueToken(), new RightParenToken(), new LeftCurlyToken(), new StringToken(),
            new VariableToken("i"),new EqualsToken() ,new StringValueToken("\"hello\""), new RightCurlyToken()});
    }
    @Test
    public void testMultipleTokensAndSemiColonToken() throws TokenizerException{
        // Test #41 (checking if(true){strg i = "hello";})
        assertTokenizes("if(true){strg i = \"hello\";}", new Token[] {new IfToken(), new LeftParenToken(), 
            new TrueToken(), new RightParenToken(), new LeftCurlyToken(), new StringToken(),
            new VariableToken("i"),new EqualsToken() ,new StringValueToken("\"hello\""), new SemiColonToken(), new RightCurlyToken()});
    }
    @Test
    public void testMultipleTokensAndSemiColon() throws TokenizerException{
        // Test #42 (checking if(true){strg i = "best";})
        assertTokenizes("if(true){strg i = \"best\";}", new Token[] {new IfToken(), new LeftParenToken(), 
            new TrueToken(), new RightParenToken(), new LeftCurlyToken(), new StringToken(),
            new VariableToken("i"),new EqualsToken() ,new StringValueToken("\"best\""), new SemiColonToken(), new RightCurlyToken()});
    }

    @Test
    public void testWhileAndOtherTokens() throws TokenizerException{
        // Test #43 (checking while(a!=0){strg i = "best";})
        assertTokenizes("while(a!=0){strg i = \"best\";}", new Token[] {new WhileToken(), new LeftParenToken(), 
            new VariableToken("a"), new NotEqualsToken(), new NumbersToken(0), new RightParenToken(), new LeftCurlyToken(), new StringToken(),
            new VariableToken("i"),new EqualsToken() ,new StringValueToken("\"best\""), new SemiColonToken(), new RightCurlyToken()});
    }

    @Test(expected = TokenizerException.class)
    public void testInvalid() throws TokenizerException{
        // Test #44 (checking invalid input)
        assertTokenizes("$", null);
    }

    @Test(expected= TokenizerException.class)
    public void testInvalidString() throws TokenizerException{
        // Test #45 (checking invalid String)
        assertTokenizes("\"hello", null);
    }

    @Test(expected= TokenizerException.class)
    public void testInvalidStringEnd() throws TokenizerException{
        // Test #46 (checking invalid String)
        assertTokenizes("hello\"", null);
    }

    @Test
    public void testingMultipleTokens() throws TokenizerException{
        // Test #47 (checking multiple Tokens)
        assertTokenizes("class A { public Int main(){ print(1); return 15;}}", new Token[]{
            new ClassToken(), new VariableToken("A"), new LeftCurlyToken(),
            new PublicToken(), new IntegerToken(), new MainToken(), new LeftParenToken(),
            new RightParenToken(), new LeftCurlyToken(), new PrintToken(), new LeftParenToken(),
            new NumbersToken(1), new RightParenToken(), new SemiColonToken(), new ReturnToken(),
            new NumbersToken(15), new SemiColonToken(), new RightCurlyToken(), new RightCurlyToken()
        });
    }
    @Test
    public void testWholeProgram()throws TokenizerException{
        // Test #48 (checking multiple Tokens)
        assertTokenizes("class CompilerCheck{ public Int main(){ Int num1 = 2; Int num2 = 3; " +
            "return checkMax(num1, num2); } private Int checkMax(Int num1, Int num2){ if(num1>num2)"+
            " return num1; else return num2;}}", new Token[]{
                new ClassToken(), new VariableToken("CompilerCheck"), new LeftCurlyToken(), 
                new PublicToken(), new IntegerToken(), new MainToken(), new LeftParenToken(), 
                new RightParenToken(), new LeftCurlyToken(), new IntegerToken(), new VariableToken("num1"),
                new EqualsToken(), new NumbersToken(2), new SemiColonToken(), new IntegerToken(),
                new VariableToken("num2"), new EqualsToken(), new NumbersToken(3), new SemiColonToken(),
                new ReturnToken(), new VariableToken("checkMax"), new LeftParenToken(), new VariableToken("num1"),
                new CommaToken(),new VariableToken("num2"), new RightParenToken(), new SemiColonToken(), new RightCurlyToken(),
                new PrivateToken(), new IntegerToken(), new VariableToken("checkMax"), new LeftParenToken(), new IntegerToken(),
                new VariableToken("num1"), new CommaToken(), new IntegerToken(), new VariableToken("num2"),
                new RightParenToken(), new LeftCurlyToken(), new IfToken(), new LeftParenToken(), new VariableToken("num1"),
                new GreaterThanToken(), new VariableToken("num2"), new RightParenToken(), new ReturnToken(), 
                new VariableToken("num1"), new SemiColonToken(), new ElseToken(), new ReturnToken(), new VariableToken("num2"),
                new SemiColonToken(), new RightCurlyToken(), new RightCurlyToken()
        });
    }
    @Test
    public void testPeriodTokenByItself() throws TokenizerException{
        //Test #49
        assertTokenizes(".", new Token[]{new PeriodToken()});
    }
    @Test
    public void testConstructorTokenByItself() throws TokenizerException{
        //Test #50
        assertTokenizes("constructor", new Token[]{new ConstructorToken()});
    }


    // Test-driven development : write tests first
    // 1. TokenizerTest. Compile and run.
    // 2. Tokens/Tokenizer
    class TestingKeywords{
        
    }
}