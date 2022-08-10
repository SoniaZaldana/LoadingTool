import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.io.IOException;

public class CustomClassWriter {
    LoadMethodAdapter loadMethodAdapter;
    String className;
    ClassReader reader;
    ClassWriter writer;
    List<String> knownParams;

    public CustomClassWriter(String className) {
        try {
            this.className = className;
            reader = new ClassReader(className);
            writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] changeLoadMethod(LdcTracker tracker) {
        loadMethodAdapter = new LoadMethodAdapter(writer, tracker);
        reader.accept(loadMethodAdapter, ClassReader.SKIP_FRAMES);
        return writer.toByteArray();
    }

    public LdcTracker getTracker() throws Exception {
        ClassReader cr = new ClassReader(className);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ConstantVisitor constantVisitor = new ConstantVisitor(cw, className);
        cr.accept(constantVisitor, ClassReader.SKIP_FRAMES);
        return constantVisitor.getTracker();
    }


}
