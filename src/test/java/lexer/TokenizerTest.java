package lexer;

import java.util.List;

import lexer.tokens.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenizerTest {
    @Test
    public void testEmptyString() throws TokenizerException {
        // Test # 1
        // check that tokenizing empty string works
        Tokenizer tokenizer = new Tokenizer("");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testOnlyWhitespace() throws TokenizerException {
        // Test #2 (checking whitespace)
        Tokenizer tokenizer = new Tokenizer("   ");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(0, tokens.size());
    }
    @Test
    public void testTrueByItself() throws TokenizerException {
        // Test #3 (checking true token)
        Tokenizer tokenizer = new Tokenizer("true");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token trueToken = tokens.get(0);
        assertTrue(trueToken instanceof TrueToken);
    }
    @Test
    public void testFalseByItself() throws TokenizerException {
        // Test #4 (checking false token)
        Tokenizer tokenizer = new Tokenizer("false");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token falseToken = tokens.get(0);
        assertTrue(falseToken instanceof FalseToken);
    }
    @Test
    public void testLeftParenByItself() throws TokenizerException {
        // Test #5 (checking false token)
        Tokenizer tokenizer = new Tokenizer("(");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token leftParenToken = tokens.get(0);
        assertTrue(leftParenToken instanceof LeftParenToken);
    }
    @Test
    public void testRightParenByItself() throws TokenizerException {
        // Test #6 (checking false token)
        Tokenizer tokenizer = new Tokenizer(")");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token rightParenToken = tokens.get(0);
        assertTrue(rightParenToken instanceof RightParenToken);
    }
    @Test
    public void testLeftCurlyByItself() throws TokenizerException {
        // Test #7 (checking false token)
        Tokenizer tokenizer = new Tokenizer("{");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token leftCurlyToken = tokens.get(0);
        assertTrue(leftCurlyToken instanceof LeftCurlyToken);
    }
    @Test
    public void testRightCurlyByItself() throws TokenizerException {
        // Test #8 (checking false token)
        Tokenizer tokenizer = new Tokenizer("}");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token rightCurlyToken = tokens.get(0);
        assertTrue(rightCurlyToken instanceof RightCurlyToken);
    }
    @Test
    public void testStringByItself() throws TokenizerException {
        // Test #9 (checking false token)
        Tokenizer tokenizer = new Tokenizer("strg");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token stringToken = tokens.get(0);
        assertTrue(stringToken instanceof StringToken);
    }
    @Test
    public void testBooleanByItself() throws TokenizerException {
        // Test #10 (checking false token)
        Tokenizer tokenizer = new Tokenizer("Boolean");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token booleanToken = tokens.get(0);
        assertTrue(booleanToken instanceof BooleanToken);
    }
    @Test
    public void testIntByItself() throws TokenizerException {
        // Test #11 (checking false token)
        Tokenizer tokenizer = new Tokenizer("Int");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token integerToken = tokens.get(0);
        assertTrue(integerToken instanceof IntegerToken);
    }
    @Test
    public void testThisByItself() throws TokenizerException {
        // Test #12 (checking false token)
        Tokenizer tokenizer = new Tokenizer("this");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token thisToken = tokens.get(0);
        assertTrue(thisToken instanceof ThisToken);
    }
    @Test
    public void testPrintByItself() throws TokenizerException {
        // Test #13 (checking false token)
        Tokenizer tokenizer = new Tokenizer("print");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token printToken = tokens.get(0);
        assertTrue(printToken instanceof PrintToken);
    }
    @Test
    public void testBreakByItself() throws TokenizerException {
        // Test #14 (checking false token)
        Tokenizer tokenizer = new Tokenizer("break");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token breakToken = tokens.get(0);
        assertTrue(breakToken instanceof BreakToken);
    }
    @Test
    public void testSemiColonByItself() throws TokenizerException {
        // Test #15 (checking false token)
        Tokenizer tokenizer = new Tokenizer(";");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token semiColonToken = tokens.get(0);
        assertTrue(semiColonToken instanceof SemiColonToken);
    }
    @Test
    public void testReturnByItself() throws TokenizerException {
        // Test #16 (checking false token)
        Tokenizer tokenizer = new Tokenizer("return");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token returnToken = tokens.get(0);
        assertTrue(returnToken instanceof ReturnToken);
    }
    @Test
    public void testNewByItself() throws TokenizerException {
        // Test #17 (checking false token)
        Tokenizer tokenizer = new Tokenizer("new");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token NewToken = tokens.get(0);
        assertTrue(NewToken instanceof NewToken);
    }
    @Test
    public void testPublicByItself() throws TokenizerException {
        // Test #18 (checking false token)
        Tokenizer tokenizer = new Tokenizer("public");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token publicToken = tokens.get(0);
        assertTrue(publicToken instanceof PublicToken);
    }
    @Test
    public void testProtectedByItself() throws TokenizerException {
        // Test #19 (checking false token)
        Tokenizer tokenizer = new Tokenizer("protected");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token protectedToken = tokens.get(0);
        assertTrue(protectedToken instanceof ProtectedToken);
    }
    @Test
    public void testPrivateByItself() throws TokenizerException {
        // Test #20 (checking false token)
        Tokenizer tokenizer = new Tokenizer("private");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1,tokens.size());
        Token privateToken = tokens.get(0);
        assertTrue(privateToken instanceof PrivateToken);
    }
    @Test
    public void testIfByItself() throws TokenizerException {
        // Test #21 (checking false token)
        Tokenizer tokenizer = new Tokenizer("if");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1, tokens.size());
        Token ifToken = tokens.get(0);
        assertTrue(ifToken instanceof IfToken);
    }
    @Test
    public void testWhileByItself() throws TokenizerException {
        // Test #22 (checking false token)
        Tokenizer tokenizer = new Tokenizer("while");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1, tokens.size());
        Token whileToken = tokens.get(0);
        assertTrue(whileToken instanceof WhileToken);
    }

    // Test-driven development : write tests first
    // 1. TokenizerTest. Compile and run.
    // 2. Tokens/Tokenizer

    /*
     * public static void main(String[] args) throws TokenizerException {
     * testOnlyWhitespace();
     * testEmptyString();
     * testTrueByItself();
     * }
     */
}