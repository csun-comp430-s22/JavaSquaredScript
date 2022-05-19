package compiler;
import codegenerator.CodeGenerator;
import codegenerator.CodeGeneratorException;
import lexer.Tokenizer;
import lexer.TokenizerException;
import lexer.tokens.Token;
import parser.ParserException;
import parser.*;
import typechecker.TypeErrorException;
import typechecker.Typechecker;

import java.io.*;
import java.util.List;

public class Compiler {
    public static void printUsage() {
        System.out.println("Takes the following params:");
        System.out.println("-Input filename (.j^2s)");
        System.out.println("-Output filename (.js)");
    }

    public static String fileContentsAsString(final String inputFilename) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            return builder.toString();
        } finally {
            reader.close();
        }
    }

    public static void compile(final String inputFilename,
                               final String outputFilename)
            throws IOException,
            TokenizerException,
            ParserException,
            TypeErrorException,
            CodeGeneratorException {
        final String input = fileContentsAsString(inputFilename);
        Parser parser = new Parser(new Tokenizer(input).tokenize());
        final Program program = parser.parseProgram();
        new Typechecker(program).isWellTypedProgram();
        final PrintWriter output =
                new PrintWriter(new BufferedWriter(new FileWriter(outputFilename)));
        try {
            CodeGenerator.generateCode(program, output);
        } finally {
            output.close();
        }
    }

    public List<Token> tokenizes(final String input) throws TokenizerException{
        final Tokenizer tokenizer = new Tokenizer(input);
        return tokenizer.tokenize();
    }

    public static void main(final String[] args)
            throws IOException,
            TokenizerException,
            ParserException,
            TypeErrorException,
            CodeGeneratorException {
        if (args.length != 2) {
            printUsage();
        } else {
            compile(args[0], args[1]);
        }
    }
}
