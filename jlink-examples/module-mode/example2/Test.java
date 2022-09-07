package jlink;

public class Test {

    public static void main(String[] args) throws Exception {

        /* Shouldn't transform as module isn't directly accessible in the module graph */
        Class<?> c4 = Class.forName("main.MainApp");
        System.out.println(c4);

        /* Should transform as module is directly accessible in the module graph */
        Class<?> c5 = Class.forName("com.sonia.hello.HelloModules");
        System.out.println(c5);
    }

}