package codegenerator;

import org.junit.Test;
import parser.*;
import parser.AccesModTypes.PublicType;
import parser.Def.ClassDef;
import parser.Def.MethodDef;
import parser.ExpCalls.IntegerExp;
import parser.Names.ClassName;
import parser.Names.MethodName;
import parser.ReturnTypes.IntType;
import parser.StmtCalls.BlockStmt;
import parser.StmtCalls.MainStmt;
import parser.StmtCalls.PrintStmt;
import parser.interfaces.Stmt;
import typechecker.TypeErrorException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public static String[] readUntilClose(final InputStream stream) throws IOException {
		return readUntilClose(new BufferedReader(new InputStreamReader(stream)));
	}

	public static String[] readUntilClose(final BufferedReader reader) throws IOException {
		final List<String> buffer = new ArrayList<String>();

		try {
			String currentLine = "";
			while ((currentLine = reader.readLine()) != null) {
				buffer.add(currentLine);
			}
			return buffer.toArray(new String[buffer.size()]);
		} finally {
			reader.close();
		}
	}

	public static Program constructProgram(final ArrayList<Stmt> stmts) {
		return new Program(
			Arrays.asList(
				new ClassDef(
					new ClassName("mainClass"),
					new ClassName(""),
					new ArrayList<>(),
					Arrays.asList(
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

		final PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME)));

		try {
			CodeGenerator.generateCode(constructProgram(statements), output);
		} finally {
			output.close();
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

		assertEquals(constructExpected(expectedOutput), runTest(statements));
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

}
