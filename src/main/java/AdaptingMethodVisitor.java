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
    private List<String> parameters;


    public AdaptingMethodVisitor(MethodVisitor mv, List<String> parameters) {
        super(Opcodes.ASM9, mv);
        this.parameters = parameters;
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
            if (parameters.size() != 0) {
                String param = parameters.remove(0);
                System.out.println("replaced bytecode for Class.forName with param: " + param);
                replaceBytecodeWithLdc(mv, param);
                return;
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private void replaceBytecodeWithLdc(MethodVisitor mv, String className) {
        mv.visitCode();
        Label label0 = new Label();
        mv.visitLabel(label0);
        mv.visitLineNumber(10, label0);
        mv.visitLdcInsn(Type.getObjectType(className.replace(".", "/")));
        mv.visitMaxs(1, 0);
    }
}
