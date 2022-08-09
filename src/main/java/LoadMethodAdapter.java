import org.objectweb.asm.*;

import java.util.List;

import static org.objectweb.asm.Opcodes.ASM9;

public class LoadMethodAdapter extends ClassVisitor {

    private List<String> parameters;

    public LoadMethodAdapter(ClassVisitor cv, List<String> parameters) {
        super(ASM9, cv);
        this.cv = cv;
        this.parameters = parameters;
    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return new AdaptingMethodVisitor(mv, parameters);
    }
}
