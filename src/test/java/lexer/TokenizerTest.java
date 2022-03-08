package lexer;

import java.lang.annotation.Target;
import java.util.List;
import java.io.*;
import java.util.*;

import lexer.tokens.*;
import org.junit.Test;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class TokenizerTest {
    private Logger logger = Logger.getLogger(TokenizerTest.class);
    public void assertTokenizes(final String input, final Token[] expected){
        try{
            final Tokenizer tokenizer = new Tokenizer(input);
            final List<Token> received = tokenizer.tokenize();
            assertArrayEquals(expected,received.toArray(new Token[received.size()]));
        }catch(final TokenizerException e){
            fail("Tokenizer threw Exception");
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
        // Test #5 (checking LeftParen token)
        assertTokenizes("(", new Token[] {new LeftParenToken()});
    }
    @Test
    public void testRightParenByItself() {
        // Test #6 (checking RightParen token)
        assertTokenizes(")", new Token[] {new RightParenToken()});
    }
     @Test
     public void testLeftCurlyByItself() {
         // Test #7 (checking LeftCurly token)
        assertTokenizes("{", new Token[] {new LeftCurlyToken()});
    }
    @Test
    public void testRightCurlyByItself() {
        // Test #8 (checking RightCurly token)
        assertTokenizes("}", new Token[] {new RightCurlyToken()});
    }
    @Test
    public void testStringByItself() {
        // Test #9 (checking String token)
        assertTokenizes("strg", new Token[] {new StringToken()});
    }
    @Test
    public void testBooleanByItself() {
        // Test #10 (checking Boolean token)
        assertTokenizes("Boolean", new Token[] {new BooleanToken()});
    }
    @Test
    public void testIntByItself() {
        // Test #11 (checking Int token)
        assertTokenizes("Int", new Token[] {new IntegerToken()});
    }
    @Test
    public void testThisByItself() {
        // Test #12 (checking This token)
        assertTokenizes("this", new Token[] {new ThisToken()});
    }
    @Test
    public void testPrintByItself() {
        // Test #13 (checking Print Token)
        assertTokenizes("print", new Token[] {new PrintToken()});
    }
    @Test
    public void testBreakByItself() {
        // Test #14 (checking break token)
        assertTokenizes("break", new Token[] {new BreakToken()});
    }
    @Test
    public void testSemiColonByItself() {
        // Test #15 (checking ; token)
        assertTokenizes(";", new Token[] {new SemiColonToken()});
    }
    @Test
    public void testReturnByItself() {
        // Test #16 (checking return token)
        assertTokenizes("return", new Token[] {new ReturnToken()});
    }
    @Test
    public void testNewByItself() {
        // Test #17 (checking new token)
        assertTokenizes("new", new Token[] {new NewToken()});
    }
    @Test
    public void testPublicByItself() {
        // Test #18 (checking public token)
        assertTokenizes("public", new Token[] {new PublicToken()});
    }
    @Test
    public void testProtectedByItself() {
        // Test #19 (checking protected token)
        assertTokenizes("protected", new Token[] {new ProtectedToken()});
    }
    @Test
    public void testPrivateByItself() {
        // Test #20 (checking private token)
        assertTokenizes("private", new Token[] {new PrivateToken()});
    }
    @Test
    public void testVariableByItself() {
        // Test #21 (checking variable token)
        assertTokenizes("foo", new Token[] {new VariableToken("foo")});
    }
    // @Test 
    // public void testStringValueToken(){
    //     // Test #22 (checking Empty String)
    //     assertTokenizes("\"\"", new Token[]{new StringValueToken("\"\"")});
    // }
    @Test 
    public void testPlusTokenByItself(){
        // Test #23 (checking Empty String)
        assertTokenizes("+", new Token[]{new PlusToken()});
    }
    @Test 
    public void testMinusTokenByItself(){
        // Test #24 (checking Empty String)
        assertTokenizes("-", new Token[]{new MinusToken()});
    }
    @Test 
    public void testTimesTokenByItself(){
        // Test #25 (checking Empty String)
        assertTokenizes("*", new Token[]{new TimesToken()});
    }
    @Test 
    public void testDivisionTokenByItself(){
        // Test #26 (checking Empty String)
        assertTokenizes("/", new Token[]{new DivisionToken()});
    }
    @Test 
    public void testGreaterThanTokenByItself(){
        // Test #27 (checking Empty String)
        assertTokenizes(">", new Token[]{new GreaterThanToken()});
    }
    @Test 
    public void testLessThanTokenByItself(){
        // Test #28 (checking Empty String)
        assertTokenizes("<", new Token[]{new LessThanToken()});
    }
    @Test 
    public void testEqualsTokenByItself(){
        // Test #29 (checking Empty String)
        assertTokenizes("=", new Token[]{new EqualsToken()});
    }
    @Test 
    public void testDoubleEqualsTokenByItself(){
        // Test #29 (checking Empty String)
        assertTokenizes("==", new Token[]{new DoubleEqualsToken()});
    }
    @Test 
    public void testNotEqualsTokenByItself(){
        // Test #31 (checking Empty String)
        assertTokenizes("!=", new Token[]{new NotEqualsToken()});
    }
    @Test
    public void testIfParenTrueTokens() {
        // Test #32 (checking if(true) token)
        assertTokenizes("if(true)", new Token[] {new IfToken(),new LeftParenToken(),new TrueToken(),new RightParenToken()});
    }
    @Test
    public void testIfParenFalseTokens() {
        // Test #33 (checking if(false) token)
        assertTokenizes("if(false)", new Token[] {new IfToken(),new LeftParenToken(),new FalseToken(),new RightParenToken()});
    }
    @Test
    public void testReturnSemiTokens() {
        // Test #34 (checking return; token)
        assertTokenizes("if;", new Token[] {new IfToken(), new SemiColonToken()});
    }
    @Test
    public void testWhileTokens() {
        // Test #35 (checking while token)
        assertTokenizes("while", new Token[] {new WhileToken()});
    }

    @Test
    public void testIntegerTokens() {
        // Test #36 (checking return; token)
        assertTokenizes("1", new Token[] {new NumbersToken(1)});
    }
    @Test
    public void testMultipleIntegerTokens() {
        // Test #37 (checking return; token)
        assertTokenizes("123", new Token[] {new NumbersToken(123)});
    }
    @Test
    public void testMultipleTokens() {
        // Test #38 (checking if(true){strg i = "hello"})
        assertTokenizes("if(true){strg i = \"hello\"}", new Token[] {new IfToken(), new LeftParenToken(), 
            new TrueToken(), new RightParenToken(), new LeftCurlyToken(), new StringToken(),
            new VariableToken("i"),new EqualsToken() ,new StringValueToken("\"hello\""), new RightCurlyToken()});
    }
    @Test
    public void testMultipleTokensAndSemiColonToken() {
        // Test #38 (checking if(true){strg i = "hello";})
        assertTokenizes("if(true){strg i = \"hello\";}", new Token[] {new IfToken(), new LeftParenToken(), 
            new TrueToken(), new RightParenToken(), new LeftCurlyToken(), new StringToken(),
            new VariableToken("i"),new EqualsToken() ,new StringValueToken("\"hello\""), new SemiColonToken(), new RightCurlyToken()});
    }
    @Test
    public void testMultipleTokensAndSemiColon() {
        // Test #38 (checking if(true){strg i = "hello";})
        assertTokenizes("if(true){strg i = \"best\";}", new Token[] {new IfToken(), new LeftParenToken(), 
            new TrueToken(), new RightParenToken(), new LeftCurlyToken(), new StringToken(),
            new VariableToken("i"),new EqualsToken() ,new StringValueToken("\"best\""), new SemiColonToken(), new RightCurlyToken()});
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