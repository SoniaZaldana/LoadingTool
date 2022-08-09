import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

import java.util.ArrayList;
import java.util.List;

/**
 * Fills out the data structure containing arguments for Class.forName
 * that are statically known constants.
 */
public class ConstantVisitor extends ClassVisitor {
    private String className;
    private List<String> parameters;
    private final String OWNER = "java/lang/Class";
    private final String NAME = "forName";
    private final String DESCRIPTOR = "(Ljava/lang/String;)Ljava/lang/Class;";

    public ConstantVisitor(ClassVisitor cv, String className) {
        super(Opcodes.ASM9, cv);
        this.className = className;
        this.parameters = new ArrayList<>();
    }


    @Override
    public void visitEnd() {
        try {
            fillOutParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.visitEnd();

    }

    public List<String> getParameters() { return this.parameters; }

    private BasicValue getStackValue(int instructionIndex, int frameIndex, Frame<BasicValue>[] frames) throws AnalyzerException {
        Frame<BasicValue> f = frames[instructionIndex];
        if (f == null) {
            return null;
        }
        int top = f.getStackSize() - 1;
        return frameIndex <= top ? f.getStack(top - frameIndex) : null;
    }

    private void fillOutParameters() throws Exception {
        ClassReader cr = new ClassReader(className);
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
                if (n instanceof MethodInsnNode) {
                    MethodInsnNode m = (MethodInsnNode) n;

                    /* Adding parameters to forName in order of analysis */
                    if (m.owner.equals(OWNER) && m.name.equals(NAME)
                            && m.desc.equals(DESCRIPTOR)) {
                        BasicValue arg = getStackValue(i, 0, analyzer.getFrames());
                        if (arg != null && arg instanceof StringValue && ((StringValue) arg).getContents() != null) {
                            String clazz = ((StringValue) arg).getContents();
                            System.out.println("reflected class parameter fetched from stack: " + clazz);
                            parameters.add(clazz);
                        }
                    }
                }
            }
        }
    }


}
