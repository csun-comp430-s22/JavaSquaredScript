package parser;

import java.util.List;

public class ClassDef {
    public final ClassName className; 
    public final ClassName extendsClassName;
    public final List<InstanceDec> instanceDecList;
    public final ConstructorDef constructor;
    public final List<MethodDef> methodDefList;

    public ClassDef(final ClassName className, final ClassName extendsClassName, 
                    final List<InstanceDec> instanceDecList, final ConstructorDef constructor, 
                    final List<MethodDef> methodDefList) {
        this.className = className;
        this.extendsClassName = extendsClassName;
        this.instanceDecList = instanceDecList;
        this.constructor = constructor;
        this.methodDefList = methodDefList;
    }

    public int hashCode() {
        return (className.hashCode() + 
                extendsClassName.hashCode() +
                instanceDecList.hashCode() +
                constructor.hashCode()+ methodDefList.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassDef) {
            final ClassDef otherDef = (ClassDef)other;
            return (className.equals(otherDef.className) && 
                    (extendsClassName.equals(otherDef.extendsClassName)) &&
                    (instanceDecList.equals(otherDef.instanceDecList)) &&
                    (constructor.equals(otherDef.constructor)) &&
                    methodDefList.equals(otherDef.methodDefList));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ClassDef(" + 
                className.toString() + ", " +
                extendsClassName.toString() + ", " +
                instanceDecList.toString() + ", " +
                constructor.toString() + ", " +
                methodDefList.toString() + ")");
    }
}
