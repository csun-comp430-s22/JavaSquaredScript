package codegenerator;

import lexer.Tokenizer;
import lexer.TokenizerException;
import org.junit.Test;
import parser.*;
import parser.AccesModTypes.PublicType;
import parser.Declarations.Vardec;
import parser.Def.ClassDef;
import parser.Def.MethodDef;
import parser.ExpCalls.*;
import parser.Names.ClassName;
import parser.Names.MethodName;
import parser.OpCalls.*;
import parser.ReturnTypes.BooleanType;
import parser.ReturnTypes.ClassNameType;
import parser.ReturnTypes.IntType;
import parser.ReturnTypes.StringType;
import parser.StmtCalls.*;
import parser.interfaces.Stmt;
import typechecker.TypeErrorException;
import typechecker.Typechecker;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CodeGeneratorTest {

	private static final String FILE_NAME = "program.js";

	private static final String DEFAULT_OUTPUT =
		"function makeObject(vtable, constructor, ...params) {\n" +
			"let self = {};\n" +
			"  self.vtable = vtable;\n" +
			"  params.unshift(self);\n" +
			"  constructor.apply(this, params);\n" +
			"  return self;\n" +
			"}\n\n" +
			"function doCall(self, index, ...params) {\n" +
			"  params.unshift(self);\n" +
			"  return self.vtable[index].apply(this, params);\n" +
			"}\n\n" +
			"function Object_constructor(self) {}\n\n";

	public static Program constructProgram(final ArrayList<Stmt> stmts) {
		return new Program(
			Collections.singletonList(
				new ClassDef(
					new ClassName("mainClass"),
					new ClassName(""),
					new ArrayList<>(),
					Collections.singletonList(
						new MethodDef(
							new PublicType(),
							new IntType(), new MethodName("main"),
							new ArrayList<>(),
							new BlockStmt(
								stmts
							)
						)
					),
					new ArrayList<>()
				)
			), new MainStmt(new ClassName("mainClass"))
		);
	}

	public static ArrayList<String> constructExpected(final String expected) {
		return new ArrayList<>(Arrays.asList((DEFAULT_OUTPUT +
			"let vtable_ = [];\n" +
			"let vtable_mainClass = [mainClass_main];\n" +
			"function mainClass_constructor(self) {\n" +
			"_constructor(self);\n" +
			"}\n" +
			"function mainClass_main(self) {\n" +
			expected + "\n" +
			"}\n" +
			"mainClass_main()").split("\\r?\\n")));
	}

	public static ArrayList<String> runTest(final ArrayList<Stmt> statements)
		throws CodeGeneratorException, IOException, TypeErrorException {

		try (PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME)))) {
			CodeGenerator.generateCode(constructProgram(statements), output);
		}

		final BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
		ArrayList<String> fileContent = new ArrayList<>();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			fileContent.add(line);
		}
		reader.close();

		if (new File(FILE_NAME).delete()) {
			return fileContent;
		} else {
			return new ArrayList<>();
		}
	}

	public static void assertGeneratorOutput(final ArrayList<Stmt> statements,
		final String expectedOutput)
		throws CodeGeneratorException, IOException, TypeErrorException {

		// Prints output
		//assertHelper(constructExpected(expectedOutput), runTest(statements));

		assertEquals(constructExpected(expectedOutput), runTest(statements));
	}

	public static void assertHelper(ArrayList<String> expected, ArrayList<String> actual) {
		System.out.println("-- EXPECTED --");
		for (String string : expected) {
			System.out.println(string);
		}
		System.out.println("-- EXPECTED --");

		System.out.println("-- ACTUAL --");
		for (String string : actual) {
			System.out.println(string);
		}
		System.out.println("-- ACTUAL --");
	}

	@Test
	public void testPrintStmt() throws TypeErrorException, CodeGeneratorException, IOException {

		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new PrintStmt(new IntegerExp(3))
		));

		assertGeneratorOutput(
			stmts,
			"\tconsole.log(3);"
		);
	}

	@Test
	public void testReturnStmt() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new ReturnStmt(new IntegerExp(1))
		));

		assertGeneratorOutput(
			stmts,
			"return 1;"
		);
	}

	@Test
	public void testWhileStmt() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new WhileStmt(
				new OpExp(
					new IntegerExp(3),
					new GreaterThanOp(),
					new IntegerExp(2)
				),
				new PrintStmt(new IntegerExp(0))
			)
		));

		assertGeneratorOutput(
			stmts,
			"\twhile ((3 > 2)) {\n\t\tconsole.log(0);\n\t}"
		);
	}

	@Test
	public void testIfStmt() throws TokenizerException, ParserException, TypeErrorException, CodeGeneratorException, IOException {
		final String input = "if(3>2){print(0);}else{print(1);}";
		Parser parser = new Parser(new Tokenizer(input).tokenize());
		ArrayList<Stmt> stmt = new ArrayList<>(Arrays.asList(parser.parseStmt(0).result));
		//System.out.println(parser.parseStmt(0).result);
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new IfStmt(
				new OpExp(
					new IntegerExp(3),
					new GreaterThanOp(),
					new IntegerExp(2)
				),
				new PrintStmt(new IntegerExp(0)),
				new PrintStmt(new IntegerExp(1))
			)
		));
		assertGeneratorOutput(
			stmt,
			"\tif ((3 > 2)) {\n\t\tconsole.log(0);\n\t} else {\n\t\tconsole.log(1);\n\t}"
		);
	}

	@Test
	public void testVardecStmt() throws ParserException, TokenizerException, TypeErrorException, CodeGeneratorException, IOException {
		final String input = "Int x=3;";
		Parser parser = new Parser(new Tokenizer(input).tokenize());
		ArrayList<Stmt> stmt = new ArrayList<>(Arrays.asList(parser.parseStmt(0).result));
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new IntType(),
					new VariableExp("x")
				),
				new IntegerExp(3)
			)
		));
		assertGeneratorOutput(
			stmt,
			"\tlet x = 3;"
		);
	}

	@Test
	public void testExpStmt() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new ExpStmt(
				new VariableExp("x")
			)
		));

		assertGeneratorOutput(
			stmts,
			"self.x;"
		);
	}

	@Test
	public void testPlusOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new IntType(),
					new VariableExp("x")
				),
				new OpExp(
					new IntegerExp(3),
					new PlusOp(),
					new IntegerExp(4)
				)
			)
		));

		assertGeneratorOutput(
			stmts,
			"\tlet x = (3 + 4);"
		);
	}

	@Test
	public void testMinusOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new IntType(),
					new VariableExp("x")
				),
				new OpExp(
					new IntegerExp(3),
					new MinusOp(),
					new IntegerExp(4)
				)
			)
		));

		assertGeneratorOutput(
			stmts,
			"\tlet x = (3 - 4);"
		);
	}

	@Test
	public void testMultiplicationOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new IntType(),
					new VariableExp("x")
				),
				new OpExp(
					new IntegerExp(3),
					new MultiplicationOp(),
					new IntegerExp(4)
				)
			)
		));

		assertGeneratorOutput(
			stmts,
			"\tlet x = (3 * 4);"
		);
	}

	@Test
	public void testDivisionOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new IntType(),
					new VariableExp("x")
				),
				new OpExp(
					new IntegerExp(3),
					new DivisionOp(),
					new IntegerExp(4)
				)
			)
		));

		assertGeneratorOutput(
			stmts,
			"\tlet x = (3 / 4);"
		);
	}

	@Test
	public void testLessThanOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new BooleanType(),
					new VariableExp("x")
				),
				new OpExp(
					new IntegerExp(3),
					new LessThanOp(),
					new IntegerExp(4)
				)
			)
		));

		assertGeneratorOutput(
			stmts,
			"\tlet x = (3 < 4);"
		);
	}

	@Test
	public void testDoubleEqualsOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new WhileStmt(
				new OpExp(
					new BooleanLiteralExp(true),
					new DoubleEqualsOp(),
					new BooleanLiteralExp(false)
				),
				new PrintStmt(new IntegerExp(0))
			)
		));

		assertGeneratorOutput(
			stmts,
			"\twhile ((true == false)) {\n\t\tconsole.log(0);\n\t}"
		);
	}
	@Test(expected = CodeGeneratorException.class)
	public void testErrorOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
				new WhileStmt(
						new OpExp(
								new BooleanLiteralExp(true),
								new PeriodOp(),
								new BooleanLiteralExp(false)
						),
						new PrintStmt(new IntegerExp(0))
				)
		));

		assertGeneratorOutput(
				stmts,
				"\twhile ((true == false)) {\n\t\tconsole.log(0);\n\t}"
		);
	}

	@Test
	public void testNotEqualsOp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new WhileStmt(
				new OpExp(
					new BooleanLiteralExp(true),
					new NotEqualsOp(),
					new BooleanLiteralExp(false)
				),
				new PrintStmt(new IntegerExp(0))
			)
		));

		assertGeneratorOutput(
			stmts,
			"\twhile ((true != false)) {\n\t\tconsole.log(0);\n\t}"
		);
	}

	@Test
	public void testStringExp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new StringType(),
					new VariableExp("x")
				),
				new StringExp("hello")
			)
		));

		assertGeneratorOutput(
			stmts,
			"\tlet x = hello;"
		);
	}

	@Test
	public void testThisExp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
			new VardecStmt(
				new Vardec(
					new StringType(),
					new VariableExp("x")
				),
				new ThisExp()
			)
		));

		assertGeneratorOutput(
			stmts,
			"\tlet x = self;"
		);
	}

	@Test
	public void testNewExp() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
				new VardecStmt(
						new Vardec(
								new ClassNameType(new ClassName("mainClass")),
								new VariableExp("x")
						),
						new NewExp(new ClassName("mainClass"),new ArrayList<>())
				)
		));

		assertGeneratorOutput(
				stmts,
				"\tlet x = makeObject(vtable_mainClass, mainClass_constructor);"
		);

	}
	@Test
	public void testNewExpParams() throws TypeErrorException, CodeGeneratorException, IOException {
		ArrayList<Stmt> stmts = new ArrayList<>(Arrays.asList(
				new VardecStmt(
						new Vardec(
								new ClassNameType(new ClassName("mainClass")),
								new VariableExp("x")
						),
						new NewExp(new ClassName("mainClass"),Arrays.asList(new IntegerExp(5), new BooleanLiteralExp(false)))
				)
		));

		assertGeneratorOutput(
				stmts,
				"\tlet x = makeObject(vtable_mainClass, mainClass_constructor, 5, false);"
		);
	}
	@Test
	public void testFunctionCallExp() throws TypeErrorException, CodeGeneratorException, IOException, TokenizerException, ParserException {
		String input = "class A{constructor(Int x, Boolean y){} public Int main(){} public Int test(strg p){" +
				"A a = new A(5,true); Int x = (a).test(\"hello\"); break;}}";
		Parser parser = new Parser(new Tokenizer(input).tokenize());
		final Program program = parser.parseProgram();
		new Typechecker(program).isWellTypedProgram();
		final PrintWriter output =
				new PrintWriter(new BufferedWriter(new FileWriter("output_testing.js")));
		try {
			CodeGenerator.generateCode(program, output);
		} finally {
			output.close();
		}
	}
}
