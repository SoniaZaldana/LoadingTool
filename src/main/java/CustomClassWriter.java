import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.io.IOException;
import java.util.List;

public class CustomClassWriter {
    LoadMethodAdapter loadMethodAdapter;
    String className;
    ClassReader reader;
    ClassWriter writer;

    List<LdcTracker> ldcInstructions;

    List<String> parameters;

    public CustomClassWriter(String className) {
        try {
            this.className = className;
            reader = new ClassReader(className);
            writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] changeLoadMethod(List<String> parameters, List<LdcTracker> ldcTrackers) {
        loadMethodAdapter = new LoadMethodAdapter(writer, parameters, ldcTrackers);
        reader.accept(loadMethodAdapter, ClassReader.SKIP_FRAMES);
        return writer.toByteArray();
    }

    public void doAnalysis() throws Exception {
        ClassReader cr = new ClassReader(className);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ConstantVisitor constantVisitor = new ConstantVisitor(cw, className);
        cr.accept(constantVisitor, ClassReader.SKIP_FRAMES);
        this.parameters = constantVisitor.getParameters();
        this.ldcInstructions = constantVisitor.getLdcInstructions();
    }

    public List<LdcTracker> getLdcInstructions() {
        return this.ldcInstructions;
    }

    public List<String> getParameters() throws Exception{
        return this.parameters;
    }

}
