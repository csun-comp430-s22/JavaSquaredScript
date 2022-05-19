package codegenerator;

import org.junit.Test;
import parser.*;
import parser.AccesModTypes.PublicType;
import parser.Declarations.Vardec;
import parser.Def.ClassDef;
import parser.Def.MethodDef;
import parser.ExpCalls.ExpStmt;
import parser.ExpCalls.IntegerExp;
import parser.ExpCalls.OpExp;
import parser.ExpCalls.VariableExp;
import parser.Names.ClassName;
import parser.Names.MethodName;
import parser.OpCalls.GreaterThanOp;
import parser.ReturnTypes.IntType;
import parser.StmtCalls.*;
import parser.interfaces.Stmt;
import typechecker.TypeErrorException;

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
		assertHelper(constructExpected(expectedOutput), runTest(statements));

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
	public void testIfStmt() throws TypeErrorException, CodeGeneratorException, IOException {
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
			stmts,
			"\tif ((3 > 2)) {\n\t\tconsole.log(0);\n\t} else {\n\t\tconsole.log(1);\n\t}"
		);
	}

	@Test
	public void testVardecStmt() throws TypeErrorException, CodeGeneratorException, IOException {
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
			stmts,
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
}
