import org.objectweb.asm.*;


import static org.objectweb.asm.Opcodes.ASM9;

public class LoadMethodAdapter extends ClassVisitor {

    private LdcTracker tracker;

    public LoadMethodAdapter(ClassVisitor cv, LdcTracker tracker) {
        super(ASM9, cv);
        this.cv = cv;
        this.tracker = tracker;

    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return new AdaptingMethodVisitor(mv, tracker);
    }
}
