import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

import java.io.IOException;

public class Tool {

    public static void main(String[] args) {
        try {
            analyze();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BasicValue getStackValue(int instructionIndex, int frameIndex, Frame<BasicValue>[] frames) throws AnalyzerException {
        Frame<BasicValue> f = frames[instructionIndex];
        if (f == null) {
            return null;
        }
        int top = f.getStackSize() - 1;
        return frameIndex <= top ? f.getStack(top - frameIndex) : null;
    }

    private static void analyze() throws IOException, AnalyzerException {
        ClassReader cr = new ClassReader("Test");
        ClassNode cn = new ClassNode(Opcodes.ASM9);
        cr.accept(cn, 0);

        for (MethodNode mn : cn.methods) {

             InterospectiveAnalyzer analyzer = new InterospectiveAnalyzer(new ReflectionInterpreter());
             analyzer.analyze(cn.name, mn);

            int i = -1;
            for (Frame<BasicValue> frame : analyzer.getFrames()) {
                i++;
                if (frame == null) continue;
                AbstractInsnNode n = mn.instructions.get(i);
                if (n.getOpcode() != Opcodes.INVOKESTATIC) continue;

                if (n instanceof MethodInsnNode) {
                    MethodInsnNode m = (MethodInsnNode) n;
                    if (m.owner.equals("java/lang/Class") && m.name.equals("forName")
                            && m.desc.equals("(Ljava/lang/String;)Ljava/lang/Class;")) {
                        BasicValue arg = getStackValue(i, 0, analyzer.getFrames());
                        if (arg != null && arg instanceof StringValue && ((StringValue)arg).getContents() != null) {
                            String clazz = ((StringValue) arg).getContents();
                            System.out.println("reflected class " + clazz);
                        }
                    }
                }
            }
        }
    }
}
