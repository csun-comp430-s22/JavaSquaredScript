package typechecker;
import parser.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;


public class Typechecker {
    public static final String BASE_CLASS_NAME = "Object";
    public final Map<ClassName, ClassDef> classes;
    public final Map<ClassName, Map<MethodName, MethodDef>> methods;

    public final Program program;

    public static ClassDef getClass(final ClassName className,
                                    final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        if (className.name.equals(BASE_CLASS_NAME)) {
            return null;
        } else {
            final ClassDef classDef = classes.get(className);
            if (classDef == null) {
                throw new TypeErrorException("no such class: " + className);
            } else {
                return classDef;
            }
        }
    }

    public ClassDef getClass(final ClassName className) throws TypeErrorException {
        return getClass(className, classes);
    }

    // gets the parent class definition for the class with the given name
    // Throws an exception if the class doesn't exist, or if its parent
    // doesn't exist.  Returns null if the parent is Object.
    public static ClassDef getParent(final ClassName className,
                                     final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final ClassDef classDef = getClass(className, classes);
        return getClass(classDef.extendedName, classes);
    }

    public ClassDef getParent(final ClassName className) throws TypeErrorException {
        return getParent(className, classes);
    }

    public static void assertInheritanceNonCyclicalForClass(final ClassDef classDef,
                                                            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final Set<ClassName> seenClasses = new HashSet<>();
        seenClasses.add(classDef.className);
        ClassDef parentClassDef = getParent(classDef.className, classes);
        while (parentClassDef != null) {
            final ClassName parentClassName = parentClassDef.className;
            if (seenClasses.contains(parentClassName)) {
                throw new TypeErrorException("cyclic inheritance involving: " + parentClassName);
            }
            seenClasses.add(parentClassName);
            parentClassDef = getParent(parentClassName, classes);
        }
    }

    public static void assertInheritanceNonCyclical(final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        for (final ClassDef classDef : classes.values()) {
            assertInheritanceNonCyclicalForClass(classDef, classes);
        }
    }

    // includes inherited methods
    // duplicates are not permitted within the same class, but it's ok to override a superclass' method
    public static Map<MethodName, MethodDef> methodsForClass(final ClassName className,
                                                             final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final ClassDef classDef = getClass(className, classes);
        if (classDef == null) {
            return new HashMap<>();
        } else {
            final Map<MethodName, MethodDef> retval = methodsForClass(classDef.extendedName, classes);
            final Set<MethodName> methodsOnThisClass = new HashSet<>();
            for (final MethodDef methodDef : classDef.methods) {
                final MethodName methodName = methodDef.methodName;
                if (methodsOnThisClass.contains(methodName)) {
                    throw new TypeErrorException("duplicate method: " + methodName);
                }
                methodsOnThisClass.add(methodName);
                retval.put(methodName, methodDef);
            }
            return retval;
        }
    }

    public static Map<ClassName, Map<MethodName, MethodDef>> makeMethodMap(final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final Map<ClassName, Map<MethodName, MethodDef>> retval = new HashMap<>();
        for (final ClassName className : classes.keySet()) {
            retval.put(className, methodsForClass(className, classes));
        }
        return retval;
    }
    public static Map<ClassName, ClassDef> makeClassMap(final List<ClassDef> classes) throws TypeErrorException {
        final Map<ClassName, ClassDef> retval = new HashMap<>();
        for (final ClassDef classDef : classes) {
            final ClassName className = classDef.className;
            if (retval.containsKey(classDef.className)) {
                throw new TypeErrorException("Duplicate class name: " + className);
            }
        }

        assertInheritanceNonCyclical(retval);

        return retval;
    }

    public Typechecker(final Program program) throws TypeErrorException {
        this.program = program;
        classes = makeClassMap(program.classes);
        methods = makeMethodMap(classes);
    }

    public Type typeofVariable(final VariableExp exp,
                               final Map<VariableExp, Type> typeEnvironment) throws TypeErrorException {
        final Type mapType = typeEnvironment.get(exp);
        if (mapType == null) {
            throw new TypeErrorException("Used variable not in scope: " + exp.name);
        } else {
            return mapType;
        }
    }

    public Type typeofThis(final ClassName classWeAreIn) throws TypeErrorException {
        if (classWeAreIn == null) {
            throw new TypeErrorException("this used in the entry point");
        } else {
            return new ClassNameType(classWeAreIn);
        }
    }

    public Type typeofOp(final OpExp exp,
                         final Map<VariableExp, Type> typeEnvironment,
                         final ClassName classWeAreIn) throws TypeErrorException {
        final Type leftType = typeof(exp.left, typeEnvironment, classWeAreIn);
        final Type rightType = typeof(exp.right, typeEnvironment, classWeAreIn);

        if (exp.op instanceof PlusOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operand type mismatch for +");
            }
        } else if (exp.op instanceof LessThanOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BooleanType();
            } else {
                throw new TypeErrorException("Operand type mismatch for <");
            }
        } else if (exp.op instanceof DoubleEqualsOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BooleanType();
            } else {
                throw new TypeErrorException("Operand type mismatch for ==");
            }
        } else {
            throw new TypeErrorException("Unsupported operation: " + exp.op);
        }
    }

    public MethodDef getMethodDef(final ClassName className,
                                  final MethodName methodName) throws TypeErrorException {
        final Map<MethodName, MethodDef> methodMap = methods.get(className);
        if (methodMap == null) {
            throw new TypeErrorException("Unknown class name: " + className);
        } else {
            final MethodDef methodDef = methodMap.get(methodName);
            if (methodDef == null) {
                throw new TypeErrorException("Unknown method name " + methodName + " for class " + className);
            } else {
                return methodDef;
            }
        }
    }

    public Type expectedReturnTypeForClassAndMethod(final ClassName className,
                                                    final MethodName methodName) throws TypeErrorException {
        return getMethodDef(className, methodName).returnType;
    }

    // Doesn't handle access modifiers right now; would be to know which class we
    // are calling from.
    //
    // class Base extends Object {
    //   public void basePublic() {}
    //   protected void baseProtected() {}
    //   private void basePrivate() {}
    // }
    // class Sub extends Base {
    //   public void foobar() {
    //     this.basePublic();  // should be ok
    //     this.baseProtected(); // should be ok
    //     this.basePrivate(); // should give an error
    //   }
    // }
    // class SomeOtherClass extends Object {
    //   public void test() {
    //     Sub sub = new Sub();
    //     sub.basePublic(); // should be ok
    //     sub.baseProtected(); // should give an error
    //     sub.basePrivate(); // should give an error
    //   }
    // }
    //
    // for every class:
    //   - Methods on that class
    //   - Methods on the parent of that class
    public List<Type> expectedParameterTypesForClassAndMethod(final ClassName className,
                                                              final MethodName methodName)
            throws TypeErrorException {
        final MethodDef methodDef = getMethodDef(className, methodName);
        final List<Type> retval = new ArrayList<Type>();
        for (final Vardec vardec : methodDef.arguments) {
            retval.add(vardec.type);
        }
        return retval;
    }

    public void assertEqualOrSubtypeOf(final Type first, final Type second) throws TypeErrorException {
        if (first.equals(second)) {
            return;
        } else if (first instanceof ClassNameType &&
                second instanceof ClassNameType) {
            final ClassDef parentClassDef = getParent(((ClassNameType)first).className);
            assertEqualOrSubtypeOf(new ClassNameType(parentClassDef.className), second);
        } else {
            throw new TypeErrorException("incompatible types: " + first + ", " + second);
        }
    }

    // List<Type> - expected types
    // List<Exp> - received expressions
    public void expressionsOk(final List<Type> expectedTypes,
                              final List<Exp> receivedExpressions,
                              final Map<VariableExp, Type> typeEnvironment,
                              final ClassName classWeAreIn) throws TypeErrorException {
        if (expectedTypes.size() != receivedExpressions.size()) {
            throw new TypeErrorException("Wrong number of parameters");
        }
        for (int index = 0; index < expectedTypes.size(); index++) {
            final Type paramType = typeof(receivedExpressions.get(index), typeEnvironment, classWeAreIn);
            final Type expectedType = expectedTypes.get(index);
            // myMethod(int, bool, int)
            // myMethod(  2, true,   3)
            //
            // myMethod2(BaseClass)
            // myMethod2(new SubClass())
            assertEqualOrSubtypeOf(paramType, expectedType);
        }
    }

    // 1.) target should be a class.
    // 2.) target needs to have the methodname method
    // 3.) need to know the expected parameter types for the method
    //
    // exp.methodname(exp*)
    // target.methodName(params)
    public Type typeofMethodCall(final FunctionCallExp exp,
                                 final Map<VariableExp, Type> typeEnvironment,
                                 final ClassName classWeAreIn) throws TypeErrorException {
        final Type targetType = typeof(exp, typeEnvironment, classWeAreIn);
        if (targetType instanceof ClassNameType) {
            final ClassName className = ((ClassNameType)targetType).className;
            final List<Type> expectedTypes =
                    expectedParameterTypesForClassAndMethod(className, exp.fname);
            expressionsOk(expectedTypes, exp.params, typeEnvironment, classWeAreIn);
            return expectedReturnTypeForClassAndMethod(className, exp.fname);
        } else {
            throw new TypeErrorException("Called method on non-class type: " + targetType);
        }
    }

    public List<Type> expectedConstructorTypesForClass(final ClassName className)
            throws TypeErrorException {
        final ClassDef classDef = getClass(className);
        final List<Type> retval = new ArrayList<Type>();
        if (classDef == null) { // Object
            return retval;
        } else {
            for (final ConstructorDef constructorDef : classDef.constructors) {
                for(final Vardec vardec: constructorDef.parameters) {
                    retval.add(vardec.type);
                }
            }
            return retval;
        }
    }

    // new classname(exp*)
    // new className(params)
    public Type typeofNew(final NewExp exp,
                          final Map<VariableExp, Type> typeEnvironment,
                          final ClassName classWeAreIn) throws TypeErrorException {
        // need to know what the constructor arguments for this class are
        final List<Type> expectedTypes = expectedConstructorTypesForClass(exp.className);
        expressionsOk(expectedTypes, exp.params, typeEnvironment, classWeAreIn);
        return new ClassNameType(exp.className);
    }

    // classWeAreIn is null if we are in the entry point
    public Type typeof(final Exp exp,
                       final Map<VariableExp, Type> typeEnvironment,
                       final ClassName classWeAreIn) throws TypeErrorException {
        if (exp instanceof IntegerExp) {
            return new IntType();
        } else if (exp instanceof VariableExp) {
            return typeofVariable((VariableExp)exp, typeEnvironment);
        } else if (exp instanceof BooleanLiteralExp) {
            return new BooleanType();
        } else if (exp instanceof ThisExp) {
            return typeofThis(classWeAreIn);
        } else if (exp instanceof OpExp) {
            return typeofOp((OpExp)exp, typeEnvironment, classWeAreIn);
        } else if (exp instanceof FunctionCallExp) {
            return typeofMethodCall((FunctionCallExp)exp, typeEnvironment, classWeAreIn);
        } else if (exp instanceof NewExp) {
            return typeofNew((NewExp)exp, typeEnvironment, classWeAreIn);
        } else {
            throw new TypeErrorException("Unrecognized expression: " + exp);
        }
    }

    public static Map<VariableExp, Type> addToMap(final Map<VariableExp, Type> map,
                                               final VariableExp variable,
                                               final Type type) {
        final Map<VariableExp, Type> result = new HashMap<VariableExp, Type>();
        result.putAll(map);
        result.put(variable, type);
        return result;
    }

    public Map<VariableExp, Type> isWellTypedVar(final VardecStmt stmt,
                                              final Map<VariableExp, Type> typeEnvironment,
                                              final ClassName classWeAreIn) throws TypeErrorException {
        final Type expType = typeof(stmt.exp, typeEnvironment, classWeAreIn);
        assertEqualOrSubtypeOf(expType, stmt.vardec.type);
        return addToMap(typeEnvironment, stmt.vardec.variable, stmt.vardec.type);
    }

    public Map<VariableExp, Type> isWellTypedIf(final IfStmt stmt,
                                             final Map<VariableExp, Type> typeEnvironment,
                                             final ClassName classWeAreIn,
                                             final Type functionReturnType) throws TypeErrorException {
        if (typeof(stmt.guard, typeEnvironment, classWeAreIn) instanceof BooleanType) {
            isWellTypedStmt(stmt.trueBranch, typeEnvironment, classWeAreIn, functionReturnType);
            isWellTypedStmt(stmt.falseBranch, typeEnvironment, classWeAreIn, functionReturnType);
            return typeEnvironment;
        } else {
            throw new TypeErrorException("guard of if is not a boolean: " + stmt);
        }
    }

    public Map<VariableExp, Type> isWellTypedWhile(final WhileStmt stmt,
                                                final Map<VariableExp, Type> typeEnvironment,
                                                final ClassName classWeAreIn,
                                                final Type functionReturnType) throws TypeErrorException {
        if (typeof(stmt.guard, typeEnvironment, classWeAreIn) instanceof BooleanType) {
            isWellTypedStmt(stmt.body, typeEnvironment, classWeAreIn, functionReturnType);
            return typeEnvironment;
        } else {
            throw new TypeErrorException("guard on while is not a boolean: " + stmt);
        }
    }

    public Map<VariableExp, Type> isWellTypedBlock(final BlockStmt stmt,
                                                Map<VariableExp, Type> typeEnvironment,
                                                final ClassName classWeAreIn,
                                                final Type functionReturnType) throws TypeErrorException {
        for (final Stmt bodyStmt : stmt.stmts) {
            typeEnvironment = isWellTypedStmt(bodyStmt, typeEnvironment, classWeAreIn, functionReturnType);
        }
        return typeEnvironment;
    }

    // return exp;
    public Map<VariableExp, Type> isWellTypedReturnNonVoid(final ReturnStmt stmt,
                                                        final Map<VariableExp, Type> typeEnvironment,
                                                        final ClassName classWeAreIn,
                                                        final Type functionReturnType) throws TypeErrorException {
        if (functionReturnType == null) {
            throw new TypeErrorException("return in program entry point");
        } else {
            final Type receivedType = typeof(stmt.exp, typeEnvironment, classWeAreIn);
            assertEqualOrSubtypeOf(receivedType, functionReturnType);
            return typeEnvironment;
        }
    }

    // bool x = true;
    // while (true) {
    //   int x = 17;
    //   break;
    // }
    public Map<VariableExp, Type> isWellTypedStmt(final Stmt stmt,
                                               final Map<VariableExp, Type> typeEnvironment,
                                               final ClassName classWeAreIn,
                                               final Type functionReturnType) throws TypeErrorException {
        if (stmt instanceof VardecStmt) {
            return isWellTypedVar((VardecStmt) stmt, typeEnvironment, classWeAreIn);
        } else if (stmt instanceof IfStmt) {
            return isWellTypedIf((IfStmt)stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof WhileStmt) {
            return isWellTypedWhile((WhileStmt)stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof ReturnStmt) {
            return isWellTypedReturnNonVoid((ReturnStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof PrintStmt) {
            typeof(((PrintStmt)stmt).exp, typeEnvironment, classWeAreIn);
            return typeEnvironment;
        } else if (stmt instanceof BlockStmt) {
            return isWellTypedBlock((BlockStmt)stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else {
            throw new TypeErrorException("Unsupported statement: " + stmt);
        }
    }

    // methoddef ::= type methodname(vardec*) stmt
    public void isWellTypedMethodDef(final MethodDef method,
                                     Map<VariableExp, Type> typeEnvironment, // instance variables
                                     final ClassName classWeAreIn) throws TypeErrorException {
        // starting type environment: just instance variables
        final Set<VariableExp> variablesInMethod = new HashSet<VariableExp>();
        for (final Vardec vardec : method.arguments) {
            if (variablesInMethod.contains(vardec.variable)) {
                throw new TypeErrorException("Duplicate variable in method definition: " + vardec.variable);
            }
            variablesInMethod.add(vardec.variable);
            // odd semantics: last variable declaration shadows prior one
            typeEnvironment = addToMap(typeEnvironment, vardec.variable, vardec.type);
        }

        isWellTypedStmt(method.body,
                typeEnvironment, // instance variables + parameters
                classWeAreIn,
                method.returnType);
    }

    // classdef ::= class classname extends classname {
    //            vardec*; // comma-separated instance variables
    //            constructor(vardec*) {
    //              super(exp*);
    //              stmt* // comma-separated
    //            }
    //            methoddef*
    //          }

    // puts all instance variables in scope for the class
    // includes parent classes
    // throws exception if there are any duplicate names in the chain
    public Map<VariableExp, Type> baseTypeEnvironmentForClass(final ClassName className) throws TypeErrorException {
        final ClassDef classDef = getClass(className);
        if (classDef == null) {
            return new HashMap<VariableExp, Type>();
        } else {
            final Map<VariableExp, Type> retval = baseTypeEnvironmentForClass(classDef.extendedName);
            for (final InstanceDec instanceDec : classDef.instances) {
                if (retval.containsKey(instanceDec.vardec)) {
                    throw new TypeErrorException("Duplicate instance variable (possibly inherited): " + instanceDec.vardec);
                }
                retval.put(instanceDec.vardec.variable, instanceDec.vardec.type);
            }
            return retval;
        }
    }

    // -Check constructor
    // -Check methods
    public void isWellTypedClassDef(final ClassDef classDef) throws TypeErrorException {
        final Map<VariableExp, Type> typeEnvironment = baseTypeEnvironmentForClass(classDef.className);

        // check constructor
        Map<VariableExp, Type> constructorTypeEnvironment = typeEnvironment;
        final Set<VariableExp> variablesInConstructor = new HashSet<VariableExp>();
        for (final ConstructorDef constructorDef : classDef.constructors) {
            for(final Vardec vardec : constructorDef.parameters) {
                if (variablesInConstructor.contains(vardec)) {
                    throw new TypeErrorException("Duplicate variable in constructor param: " + vardec);
                }
                variablesInConstructor.add(vardec.variable);
                constructorTypeEnvironment = addToMap(constructorTypeEnvironment, vardec.variable, vardec.type);
            }
        }

        // check methods
        for (final MethodDef method : classDef.methods) {
            isWellTypedMethodDef(method,
                    typeEnvironment,
                    classDef.className);
        }
    }

    // program ::= classdef* stmt
    public void isWellTypedProgram() throws TypeErrorException {
        for (final ClassDef classDef : program.classes) {
            isWellTypedClassDef(classDef);
        }

       /* isWellTypedStmt(program.entryPoint,
                new HashMap<VariableExp, Type>(),
                null,
                null);*/
    }
}