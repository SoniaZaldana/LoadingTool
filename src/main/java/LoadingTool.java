import org.objectweb.asm.tree.MethodInsnNode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoadingTool {

    public static void main(String[] args) throws Exception {
        CustomClassWriter writer = new CustomClassWriter("Test");
        List<String> classReferences = writer.getKnownClasses();

        byte[] bytes = writer.changeLoadMethod(classReferences);

        try {
            Files.deleteIfExists(Paths.get("Test.class"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream fos = new FileOutputStream("Test.class")) {
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
