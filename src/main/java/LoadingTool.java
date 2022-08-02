import java.io.FileOutputStream;

public class LoadingTool {

    public static void main(String[] args) {
        CustomClassWriter writer = new CustomClassWriter();
        byte[] bytes = writer.changeLoadMethod();

        try(FileOutputStream fos = new FileOutputStream("Test.class")) {
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
