import org.objectweb.asm.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

public class AdaptingMethodVisitor extends MethodVisitor implements Opcodes {

    private final String OWNER = "java/lang/Class";
    private final String NAME = "forName";
    private final String DESCRIPTOR = "(Ljava/lang/String;)Ljava/lang/Class;";
    private String className;

    public AdaptingMethodVisitor(MethodVisitor mv, String className) {
        super(Opcodes.ASM9, mv);
        this.className = className;
    }

    @Override
    public void visitCode() {
        mv.visitCode();
    }

    @Override
    public void visitMethodInsn(
            final int opcode,
            final String owner,
            final String name,
            final String descriptor,
            final boolean isInterface) {

        if (opcode == INVOKESTATIC && owner.equals(OWNER) && name.equals(NAME)
                && descriptor.equals(DESCRIPTOR)) {
            try {
                processMethod();
            } catch (Exception e) {
                e.printStackTrace(); // TODO better exception handling
            }
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }

    }

    private void processMethod() throws Exception {
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
                if (n.getOpcode() != Opcodes.INVOKESTATIC) continue;

                if (n instanceof MethodInsnNode) {
                    MethodInsnNode m = (MethodInsnNode) n;
                    if (m.owner.equals(OWNER) && m.name.equals(NAME)
                            && m.desc.equals(DESCRIPTOR)) {
                        BasicValue arg = getStackValue(i, 0, analyzer.getFrames());
                        if (arg != null && arg instanceof StringValue && ((StringValue)arg).getContents() != null) {
                            String clazz = ((StringValue) arg).getContents();
                            System.out.println("reflected class " + clazz);

                            /* Change bytecode */
                            replaceBytecodeWithLdc(mv, clazz);
                        }
                    }
                }
            }
        }
    }

    private void replaceBytecodeWithLdc(MethodVisitor mv, String className) {
        mv.visitCode();
        Label label0 = new Label();
        mv.visitLabel(label0);
        mv.visitLineNumber(10, label0);
        mv.visitLdcInsn(Type.getObjectType(className));
        mv.visitMaxs(1, 0);
    }

    private BasicValue getStackValue(int instructionIndex, int frameIndex, Frame<BasicValue>[] frames) throws AnalyzerException {
        Frame<BasicValue> f = frames[instructionIndex];
        if (f == null) {
            return null;
        }
        int top = f.getStackSize() - 1;
        return frameIndex <= top ? f.getStack(top - frameIndex) : null;
    }

}
