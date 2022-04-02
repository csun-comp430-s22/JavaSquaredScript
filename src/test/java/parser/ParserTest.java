package parser;

import com.sun.org.apache.xpath.internal.operations.Minus;
import lexer.tokens.IntegerToken;
import lexer.tokens.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    public void assertParses(final List<Token> input, final ParseResult<Exp> expected) throws ParserException{
        final Parser parser = new Parser(input);
        final ParseResult<Exp> received = parser.parseExp(0);
        assertEquals(expected,received);
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
        List<Token> tokensList = new ArrayList<>();
        tokensList.add(new NumbersToken(10));
        Parser parser = new Parser(tokensList);
        ParseResult<Exp> expected = new ParseResult<Exp>(new IntegerExp(10), 0);

        ParseResult<Exp> result = parser.parseExp(0);
        assertEquals(result.result, expected.result);
    }

    @Test
    public void testAdditionExp() throws ParserException{
        List<Token> tokensList = new ArrayList<>();
        tokensList.add(new NumbersToken(2));
        tokensList.add(new PlusToken());
        tokensList.add(new NumbersToken(10));
        Parser parser = new Parser(tokensList);
        ParseResult<Exp> expected = new ParseResult<Exp>(new OpExp(new IntegerExp(2), new PlusOp(), new IntegerExp(10)), 0);

        ParseResult<Exp> result = parser.parseExp(0);
        assertEquals(result.result, expected.result);
    }

    @Test
    public void testThreeAdditions() throws ParserException{
        List<Token> tokensList = new ArrayList<>();
        tokensList.add(new NumbersToken(1));
        tokensList.add(new PlusToken());
        tokensList.add(new NumbersToken(2));
        tokensList.add(new PlusToken());
        tokensList.add(new NumbersToken(3));
        Parser parser = new Parser(tokensList);
        ParseResult<Exp> expected = new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)), new PlusOp(), new IntegerExp(3)), 0);

        ParseResult<Exp> result = parser.parseExp(0);
        assertEquals(result.result, expected.result);
    }

    @Test
    public void testThreeSubtractions() throws ParserException{
        List<Token> tokensList = new ArrayList<>();
        tokensList.add(new NumbersToken(6));
        tokensList.add(new MinusToken());
        tokensList.add(new NumbersToken(2));
        tokensList.add(new MinusToken());
        tokensList.add(new NumbersToken(1));
        Parser parser = new Parser(tokensList);
        ParseResult<Exp> expected = new ParseResult<Exp>(new OpExp(new OpExp(new IntegerExp(6), new MinusOp(), new IntegerExp(2)), new MinusOp(), new IntegerExp(1)), 0);

        ParseResult<Exp> result = parser.parseExp(0);
        assertEquals(result.result, expected.result);
    }
}
