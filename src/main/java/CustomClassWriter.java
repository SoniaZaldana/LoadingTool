import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.MethodInsnNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private List<String> changeNameFormat(List<String> knownClasses) {
        List<String> reformatted = new ArrayList<>();
        for (String clazz : knownClasses) {
            reformatted.add(clazz.replace("/","."));
        }
        return reformatted;
    }

    public byte[] changeLoadMethod(List<String> knownClasses) {
        loadMethodAdapter = new LoadMethodAdapter(writer, className, changeNameFormat(knownClasses));
        reader.accept(loadMethodAdapter, ClassReader.SKIP_FRAMES);
        return writer.toByteArray();
    }

    public List<String> getKnownClasses() throws Exception {
        ConstantVisitor constantVisitor = new ConstantVisitor(writer, className);
        ClassReader reader = new ClassReader(className);
        reader.accept(constantVisitor, ClassReader.SKIP_FRAMES);
        return constantVisitor.getClassReferences();

    }

}
