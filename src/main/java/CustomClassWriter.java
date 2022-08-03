import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

public class CustomClassWriter {
    LoadMethodAdapter loadMethodAdapter;
    String className;
    ClassReader reader;
    ClassWriter writer;

    public CustomClassWriter(String className) {
        try {
            this.className = className;
            reader = new ClassReader(className);
            writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] changeLoadMethod() {
        loadMethodAdapter = new LoadMethodAdapter(writer, className);
        reader.accept(loadMethodAdapter, ClassReader.SKIP_FRAMES);
        return writer.toByteArray();
    }

}
