import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoadingTool {

    public static void main(String[] args) throws Exception {
        CustomClassWriter writer = new CustomClassWriter("Test");
        LdcTracker tracker = writer.getTracker();
        byte[] bytes = writer.changeLoadMethod(tracker);
        Files.deleteIfExists(Paths.get("Test.class"));

        try (FileOutputStream fos = new FileOutputStream("Test.class")) {
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
