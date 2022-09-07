package jlink;

public class Test {

    public static void main(String[] args) throws Exception {

        /* Shouldn't transform as module isn't directly accessible in the module graph */
        Class<?> c3 = Class.forName("com.sonia.hello.HelloModules");
        System.out.println(c3);
    }

}