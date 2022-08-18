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
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/invoke/MethodHandles", "lookup", "()Ljava/lang/invoke/MethodHandles$Lookup;", false);
        mv.visitLdcInsn(Type.getObjectType(className.replace(".", "/")));
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/invoke/MethodHandles$Lookup", "ensureInitialized", "(Ljava/lang/Class;)Ljava/lang/Class;", false);
        mv.visitMaxs(0, 1);
        mv.visitEnd();

    }
}
