import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

public class CustomClassWriter {
    LoadMethodAdapter loadMethodAdapter;
    static String className = "Test";
    ClassReader reader;
    ClassWriter writer;

    public CustomClassWriter() {
        try {
            reader = new ClassReader(className);
            writer = new ClassWriter(reader, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] changeLoadMethod() {
        loadMethodAdapter = new LoadMethodAdapter(writer, className);
        reader.accept(loadMethodAdapter, 0);
        return writer.toByteArray();
    }

}
