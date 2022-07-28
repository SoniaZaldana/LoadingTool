import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class AdaptingMethodVisitor extends MethodVisitor implements Opcodes {

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
        if (opcode == INVOKESTATIC && owner.equals("java/lang/Class") && name.equals("forName")
                && descriptor.equals("(Ljava/lang/String;)Ljava/lang/Class;")) {

            // TODO add condition here to check if parameter is in constant pool

            mv.visitCode();
            Label label0 = new Label();
            mv.visitLabel(label0);
            mv.visitLineNumber(10, label0);
            mv.visitLdcInsn(Type.getType("LTest;")); // TODO change this
            mv.visitMaxs(1, 0);
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }




    public AdaptingMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }
}
