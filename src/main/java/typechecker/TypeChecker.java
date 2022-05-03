package typechecker;
import parser.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.HashMap;
public class TypeChecker {
    public final List<ClassDef> classes;
    public final Program program;

    public TypeChecker(final Program program){
        this.program = program;
        this.classes = program.classes;
    }
    public Type typeOfOp(final OpExp exp,
                            final Map<Variable,Type> typeEnvironment,
                            final ClassName classWeAreIn) throws TypeErrorException{
        final Type leftType = typeOf(exp.left, typeEnvironment,classWeAreIn);
        final Type rightType = typeOf(exp.right, typeEnvironment, classWeAreIn);
        if(exp.op instanceof PlusOp){
            if(leftType instanceof IntType && rightType instanceof IntType){
                return new IntType();
            }else{
                throw new TypeErrorException("Operand type mismatch for +");
            }
        }else if(exp.op instanceof LessThanOp){
            if(leftType instanceof IntType && rightType instanceof IntType){
                return new BooleanType();
            }else{
                throw new TypeErrorException("Operand type mismatch for <");
            }
        }else if(exp.op instanceof DoubleEqualsOp){
            if(leftType instanceof IntType && rightType instanceof IntType){
                return new BooleanType();
            }else{
                throw new TypeErrorException("Operand type mismatch for ==");
            }
        }else{
            throw new TypeErrorException("Unsupported operation: "+exp.op);
        }
    }

    public Type typeOfThis(final ClassName classWeAreIn) throws TypeErrorException{
        if(classWeAreIn ==null){
            throw new TypeErrorException("this is used in the entry point");
        }else{
            return new ClassNameType(classWeAreIn);
        }
    }

    public Type typeOfVariable(final VariableExp exp, final Map<Variable, Type> typeEnvironment) throws TypeErrorException{
        final Type mapType = typeEnvironment.get(exp.variable);
        if(mapType == null){
            throw new TypeErrorException("Used variable not in scope: "+exp.variable.name);
        }else{
            return mapType;
        }
    }

    public Type expectedReturnTypeForClassAndMethod(final ClassName className,
                                                    final MethodName methodName){
        // WRONG - needs to find the given class and method, and return the expected
        // return type for this
        return null;
    }

    public Type typeOfMethodCall(final FunctionCallExp exp, final Map<Variable, Type> typeEnvironment, final ClassName classWeAreIn) throws TypeErrorException{
        final Type typeTarget = typeOf(exp.target, typeEnvironment, classWeAreIn);
        if(typeTarget instanceof classNameType){
            final ClassName classname = ((ClassNameType)typeTarget).classname;

        }else{
            throw new TypeErrorException("Called method on non-class type: "+ typeTarget);
        }
    }

    public Type typeOf(final Exp exp, 
                        final Map<Variable,Type> typeEnvironment,
                        final ClassName classWeAreIn) throws TypeErrorException{
        if(exp instanceof IntegerExp){
            return new IntType();
        }else if(exp instanceof VariableExp){
            return typeOfVariable((VariableExp)exp, typeEnvironment);
        } else if(exp instanceof BooleanLiteralExp){
            return new BooleanType();
        } else if(exp instanceof ThisExp){
            typeOfThis(classWeAreIn);
        }else if(exp instanceof OpExp){
            return typeOfOp((OpExp)exp, 
                            typeEnvironment, 
                            classWeAreIn);
        }else if(exp instanceof FunctionCallExp){
            return typeOfMethodCall((FunctionCallExp)exp,typeEnvironment,classWeAreIn);
        }
    }

    public Map<Variable, Type> isWellTypedVar(final Vardec stmt, final Map<Variable, Type> typeEnvironment, 
                                                final ClassName classWeAreIn, final Type functionReturnType) throws TypeErrorException{
        final Type expType = typeOf(stmt.exp, typeEnvironment, classWeAreIn);
        isEqualOrSubtypeOf(expType, stmt.vardec.type);
        addToMap(typeEnvironment, asVar.vardec.variable, stmt.vardec.type);
    
    }

    public Map<Variable, Type> isWellTypedIf(final IfStmt stmt, final Map<Variable, Type> typeEnvironment,
                                             final ClassName classWeAreIn, final Type functionReturnType) throws TypeErrorException{
        if(typeOf(stmt.guard, typeEnvironment,classWeAreIn) instanceof BooleanType){
            isWellTyped(stmt.trueBranch, typeEnvironment, classWeAreIn,functionReturnType);
            isWellTyped(stmt.falseBranch, typeEnvironment, classWeAreIn,functionReturnType);
            return typeEnvironment;
        }else{
            throw new TypeErrorException("guard of if is not boolean: "+ stmt);
        }
    }

    public Map<Variable, Type> isWellTypedWhile(final WhileStmt stmt, final Map<Variable, Type> typeEnvironment, 
                                                final ClassName classWeAreIn, final Type functionReturnType) throws TypeErrorException{
        if(typeOf(stmt.guard, typeEnvironment, classWeAreIn) instanceof BooleanType){
            isWellTyped(stmt.body, typeEnvironment, classWeAreIn,functionReturnType);
            return typeEnvironment;
        }else{
            throw new TypeErrorException("guard of while is not a boolean: "+stmt);
        }
    }

    public Map<Variable, Type> isWellTypedReturn(final ReturnStmt stmt, final Map<Variable, Type> typeEnvironment, 
                                                final ClassName classWeAreIn, final Type functionReturnType) throws TypeErrorException{
        if(functionReturnType ==null){
            throw new TypeErrorException("return in program entry point"); 
        }
                                                    final Type receivedType = typeOf(stmt.exp,typeEnvironment,classWeAreIn,functionReturnType);
        isEqualOrSubtypeOf(receivedType,functionReturnType);

    }

    public Map<Variable, Type> isWellTyped(final Stmt stmt, final Map<Variable, Type> typeEnvironment, 
                                            final ClassName classWeAreIn, final Type functionReturnType) throws TypeErrorException{
        if(stmt instanceof VardecStmt){
            return isWellTypedVar((Vardec)stmt, typeEnvironment, classWeAreIn,functionReturnType);
        }else if(stmt instanceof IfStmt){
            return isWellTypedIf((IfStmt)stmt, typeEnvironment, classWeAreIn,functionReturnType);
        }else if(stmt instanceof WhileStmt){
            return isWellTypedWhile((WhileStmt)stmt, typeEnvironment,classWeAreIn,functionReturnType);
        }else if(stmt instanceof ReturnStmt){
            return isWellTypedReturn((ReturnStmt)stmt, typeEnvironment,classWeAreIn,functionReturnType);
        }
    }
}
