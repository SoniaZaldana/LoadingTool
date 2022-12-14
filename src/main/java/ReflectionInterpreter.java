import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;

import static org.objectweb.asm.tree.analysis.BasicValue.REFERENCE_VALUE;

public class ReflectionInterpreter extends BasicInterpreter {
    public ReflectionInterpreter() {
        super(ASM8);
    }

    @Override
    public BasicValue newOperation(AbstractInsnNode insn) throws AnalyzerException
    {
        if (insn instanceof LdcInsnNode) {
            LdcInsnNode ldc = (LdcInsnNode)insn;
            Object cst = ldc.cst;
            if (cst instanceof String) {
                return new StringValue((String)cst);
            } else if (cst instanceof Type) {
                return new ClassValue(((Type)cst).getInternalName());
            }
        }
        return super.newOperation(insn);
    }

    @Override
    public BasicValue naryOperation(AbstractInsnNode insn, List<? extends BasicValue> values) throws AnalyzerException {
        return this.naryOperation(insn, values, null);
    }
    public BasicValue naryOperation(AbstractInsnNode insn, List<? extends BasicValue> values, final InterospectiveFrame frame) throws AnalyzerException {
        if (insn instanceof MethodInsnNode) {
            MethodInsnNode m = (MethodInsnNode) insn;
            if (m.getOpcode() == INVOKESTATIC) {
                if (m.owner.equals("java/lang/Class")
                        && m.name.equals("forName")
                        && m.desc.equals("(Ljava/lang/String;)Ljava/lang/Class;")
                        && values.get(0) instanceof StringValue
                        && ((StringValue) values.get(0)).getContents() != null) {
                    return new ClassValue(((StringValue) values.get(0)).getContents());
                }
            }
        }
        return super.naryOperation(insn, values);
    }


    @Override
    public BasicValue merge(BasicValue v1, BasicValue v2) {
        if (v1 instanceof StringValue
                && v2 instanceof StringValue
                && v1.equals(v2)) {
            return new StringValue((StringValue)v1);
        }
        return super.merge(degradeValue(v1), degradeValue(v2));
    }

    private BasicValue degradeValue(BasicValue v) {
        if (v instanceof StringValue) {
            return REFERENCE_VALUE;
        }
        return v;
    }
}