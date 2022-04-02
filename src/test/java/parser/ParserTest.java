package parser;

import com.sun.org.apache.xpath.internal.operations.Minus;
import lexer.tokens.IntegerToken;
import lexer.tokens.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    public void assertParses(final List<Token> input, final ParseResult<Exp> expected) throws ParserException{
        //List<Token> tokensList =  Arrays.asList(input);
        final Parser parser = new Parser(input);
        final ParseResult<Exp> received = parser.parseExp(0);
        assertEquals(expected.result,received.result);
    }
    @Test
    public void testEqualsOpExp() {
        // 1 + 1 == 1 + 1
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
        assertParses(Arrays.asList(new NumbersToken(10)), new ParseResult<Exp>(new IntegerExp(10), 0));
        
    }

    @Test
    public void testAdditionExp() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(2), new PlusToken(), new NumbersToken(10)), 
        new ParseResult<Exp>(new OpExp(new IntegerExp(2), new PlusOp(), new IntegerExp(10)), 0));
    }

    @Test
    public void testThreeAdditions() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(1),new PlusToken(), new NumbersToken(2), new PlusToken(),new NumbersToken(3)), 
        new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)), new PlusOp(), new IntegerExp(3)), 0));
    }

    @Test
    public void testThreeSubtractions() throws ParserException{
        assertParses(Arrays.asList(new NumbersToken(6), new MinusToken(), new NumbersToken(2), new MinusToken(), new NumbersToken(1)),
         new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(6), new MinusOp(), new IntegerExp(2)), new MinusOp(), new IntegerExp(1)), 0));
    }
}
