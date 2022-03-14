package parser;
public class OpExp implements Exp{
    public final Exp left;
    public final Op op;
    public final Exp right;
    public OpExp(final Exp left, final Op op, final Exp right){
        this.left = left;
        this.right = right;
        this.op = op;
    }
}
