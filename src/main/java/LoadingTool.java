import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoadingTool {

    public static void main(String[] args) throws Exception {
        CustomClassWriter writer = new CustomClassWriter("Test");
        writer.doAnalysis();
        List<String> parameters = writer.getParameters();
        List<LdcTracker> ldcTrackers = writer.getLdcInstructions();

        byte[] bytes = writer.changeLoadMethod(parameters, ldcTrackers);
        Files.deleteIfExists(Paths.get("Test.class"));

        try (FileOutputStream fos = new FileOutputStream("Test.class")) {
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
