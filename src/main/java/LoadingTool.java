import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoadingTool {

    public static void main(String[] args) {
        CustomClassWriter writer = new CustomClassWriter("Test");
        byte[] bytes = writer.changeLoadMethod();

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
