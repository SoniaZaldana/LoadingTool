import org.objectweb.asm.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

import java.util.ArrayList;
import java.util.List;

public class ConstantVisitor extends ClassVisitor {
    private String className;
    private List<String> classReferences;

    public ConstantVisitor(ClassVisitor cv, String className) {
        super(Opcodes.ASM9, cv);
        this.className = className;
        this.classReferences = new ArrayList<>();
    }


    @Override
    public void visitEnd() {
        try {
            fillOutClassReferences();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.visitEnd();

    }

    public List<String> getClassReferences() {
        return this.classReferences;
    }

    private void fillOutClassReferences() throws Exception {
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
                if (! (n instanceof MethodInsnNode)) continue;
                MethodInsnNode m = (MethodInsnNode) n;
                classReferences.add(m.owner);
            }
        }
    }

}
