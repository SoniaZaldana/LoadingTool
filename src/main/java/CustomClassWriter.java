import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public byte[] changeLoadMethod(List<String> parameters) {
        loadMethodAdapter = new LoadMethodAdapter(writer, parameters);
        reader.accept(loadMethodAdapter, ClassReader.SKIP_FRAMES);
        return writer.toByteArray();
    }

    public void processParams() throws Exception {
        ClassReader cr = new ClassReader(className);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ConstantVisitor constantVisitor = new ConstantVisitor(cw, className);
        cr.accept(constantVisitor, ClassReader.SKIP_FRAMES);
        this.knownParams = constantVisitor.getParameters();
    }

    public List<String> getParameters() {
        return this.knownParams;
    }

}
