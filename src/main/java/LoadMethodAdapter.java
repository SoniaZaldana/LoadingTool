import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class LoadMethodAdapter extends ClassVisitor {

    public LoadMethodAdapter(ClassVisitor cv) {
        super(ASM9, cv);
        this.cv = cv;
    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return new AdaptingMethodVisitor(mv);
    }
}
