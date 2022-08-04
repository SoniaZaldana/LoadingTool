import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.ASM9;

public class LoadMethodAdapter extends ClassVisitor {

    private String className;
    private List<String> knownClasses;

    public LoadMethodAdapter(ClassVisitor cv, String className, List<String> knownClasses) {
        super(ASM9, cv);
        this.cv = cv;
        this.className = className;
        this.knownClasses = knownClasses;
    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return new AdaptingMethodVisitor(mv, this.className, knownClasses);
    }
}
