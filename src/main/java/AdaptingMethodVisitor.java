import org.objectweb.asm.*;

import java.util.List;

/**
 * Method visitor which transforms bytecode for Class.forName when
 * using statically known constants.
 */
public class AdaptingMethodVisitor extends MethodVisitor implements Opcodes {

    private final String OWNER = "java/lang/Class";
    private final String NAME = "forName";
    private final String DESCRIPTOR = "(Ljava/lang/String;)Ljava/lang/Class;";
    private LdcTracker tracker;

    public AdaptingMethodVisitor(MethodVisitor mv, LdcTracker tracker) {
        super(Opcodes.ASM9, mv);
        this.tracker = tracker;
    }

    @Override
    public void visitLdcInsn(final Object value) {
        List<InstructionTracker> ldcTrackerList = tracker.getLdcInstructionTracker();
        if (ldcTrackerList.size() != 0) {
            InstructionTracker tracker = ldcTrackerList.remove(0);
            if (tracker.isNextInstructionForName()) {
                System.out.println("Ldc instruction called with param " + value + " removed");
                return;
            }
        }
        super.visitLdcInsn(value);
    }

    @Override
    public void visitVarInsn(final int opcode, final int varIndex) {
        List<InstructionTracker> loadTracker = tracker.getLoadInstructionTracker();
        if (loadTracker.size() != 0) {
            InstructionTracker tracker = loadTracker.remove(0);
            if (tracker.isNextInstructionForName()) {
                System.out.println("Ignoring load instruction as it was called after Class.forName");
                return;
            }
        }
        super.visitVarInsn(opcode, varIndex);
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

            List<String> parameters = tracker.getParamsTracker();
            if (parameters.size() != 0) {
                String param = parameters.remove(0);
                System.out.println("replaced bytecode for Class.forName with param: " + param);
                replaceBytecode(mv, param);
                return;
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private void replaceBytecode(MethodVisitor mv, String className) {

        mv.visitCode();

        /* Create lookup object */
        Label label0 = new Label();
        mv.visitLabel(label0);
        mv.visitLineNumber(8, label0);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/invoke/MethodHandles", "lookup", "()Ljava/lang/invoke/MethodHandles$Lookup;", false);
        mv.visitVarInsn(ASTORE, 1);

        /* Ldc instruction and storing class object */
        Label label1 = new Label();
        mv.visitLabel(label1);
        mv.visitLineNumber(9, label1);
        mv.visitLdcInsn(Type.getObjectType(className.replace(".", "/")));
        mv.visitVarInsn(ASTORE, 2);

        /* Forcing clinit with ensureInitialized */
        Label label2 = new Label();
        mv.visitLabel(label2);
        mv.visitLineNumber(10, label2);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/invoke/MethodHandles$Lookup", "ensureInitialized", "(Ljava/lang/Class;)Ljava/lang/Class;", false);
        mv.visitInsn(POP);

        /* Ldc instruction */
        Label label3 = new Label();
        mv.visitLabel(label3);
        mv.visitLineNumber(22, label3);
        mv.visitLdcInsn(Type.getObjectType(className.replace(".", "/")));
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

}
