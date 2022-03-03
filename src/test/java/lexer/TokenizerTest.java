package lexer;

import java.beans.Transient;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class TokenizerTest {
    public void assertTokenizes(final String input, final Token[] expected){
        try{
            final Tokenizer tokenizer = new Tokenizer(input);
            final List<Token> received = tokenizer.tokenize();
            assertArrayEquals(expected,received.toArray(new Token[received.size()]));
        }catch(final TokenizerException e){
            fail(e+"Tokenizer threw exception");
        }
    }

    @Test
    public void testEmptyString() {
        // Test # 1
        // check that tokenizing empty string works
        // Tokenizer tokenizer = new Tokenizer("");
        // List<Token> tokens = tokenizer.tokenize();
        // assertEquals(0, tokens.size());
        assertTokenizes("", new Token[0]);
    }

    @Test
    public void testOnlyWhitespace() {
        // Test #2 (checking whitespace)
        assertTokenizes("   ", new Token[0]);
    }
    @Test
    public void testTrueByItself() {
        // Test #3 (checking true token)
        assertTokenizes("true", new Token[] {new TrueToken()});
    }
    @Test
    public void testFalseByItself() {
        // Test #4 (checking false token)
        assertTokenizes("false", new Token[] {new FalseToken()});
    }
    @Test
    public void testLeftParenByItself() {
        // Test #5 (checking false token)
        assertTokenizes("(", new Token[] {new LeftParenToken()});
    }
    @Test
    public void testRightParenByItself() {
        // Test #6 (checking false token)
        assertTokenizes(")", new Token[] {new RightParenToken()});
    }
     @Test
     public void testLeftCurlyByItself() {
         // Test #7 (checking false token)
        assertTokenizes("{", new Token[] {new LeftCurlyToken()});
    }
    @Test
    public void testRightCurlyByItself() {
        // Test #8 (checking false token)
        assertTokenizes("}", new Token[] {new RightCurlyToken()});
    }
    @Test
    public void testStringByItself() {
        // Test #9 (checking false token)
        assertTokenizes("strg", new Token[] {new StringToken()});
    }
    @Test
    public void testBooleanByItself() {
        // Test #10 (checking false token)
        assertTokenizes("Boolean", new Token[] {new BooleanToken()});
    }
    @Test
    public void testIntByItself() {
        // Test #11 (checking false token)
        assertTokenizes("Int", new Token[] {new IntegerToken()});
    }
    @Test
    public void testThisByItself() {
        // Test #12 (checking false token)
        assertTokenizes("this", new Token[] {new ThisToken()});
    }
    @Test
    public void testPrintByItself() {
        assertTokenizes("print", new Token[] {new PrintToken()});
    }
    @Test
    public void testBreakByItself() {
        // Test #14 (checking false token)
        assertTokenizes("break", new Token[] {new BreakToken()});
    }
    @Test
    public void testSemiColonByItself() {
        // Test #15 (checking false token)
        assertTokenizes(";", new Token[] {new SemiColonToken()});
    }
    @Test
    public void testReturnByItself() {
        // Test #16 (checking false token)
        assertTokenizes("return", new Token[] {new ReturnToken()});
    }
    @Test
    public void testNewByItself() {
        // Test #17 (checking false token)
        assertTokenizes("new", new Token[] {new NewToken()});
    }
    @Test
    public void testPublicByItself() {
        // Test #18 (checking false token)
        assertTokenizes("public", new Token[] {new PublicToken()});
    }
    @Test
    public void testProtectedByItself() {
        // Test #19 (checking false token)
        assertTokenizes("protected", new Token[] {new ProtectedToken()});
    }
    @Test
    public void testPrivateByItself() {
        // Test #20 (checking false token)
        assertTokenizes("private", new Token[] {new PrivateToken()});
    }
    @Test
    public void testVariableByItself() {
        // Test #20 (checking false token)
        assertTokenizes("foo", new Token[] {new VariableToken()});
    }
    @Test
    public void testIfParenTrueTokens() {
        // Test #21 (checking false token)
        assertTokenizes("if(true)", new Token[] {new IfToken(),new LeftParenToken(),new TrueToken(),new RightParenToken()});
    }
    @Test
    public void testIfParenFalseTokens() {
        // Test #20 (checking false token)
        assertTokenizes("if(false)", new Token[] {new IfToken(),new LeftParenToken(),new FalseToken(),new RightParenToken()});
    }

    @Test
    public void testVariableTokens() {
        // Test #20 (checking variable token)
        assertTokenizes("foo", new Token[] { new VariableToken("foo") });
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