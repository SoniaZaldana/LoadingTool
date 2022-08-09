import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoadingTool {

    public static void main(String[] args) throws Exception {
        CustomClassWriter writer = new CustomClassWriter("Test");
        writer.processParams();
        List<String> parameters = writer.getParameters();

        byte[] bytes = writer.changeLoadMethod(parameters);
        Files.deleteIfExists(Paths.get("Test.class"));

        try (FileOutputStream fos = new FileOutputStream("Test.class")) {
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
