package jlink;

public class Test {

    public static void main(String[] args) throws Exception {

        /* Shouldn't be transformed, as module can't access class*/
        Class<?> c1 = Class.forName("jdk.internal.misc.CDS");
        System.out.println(c1);

        /* Should transform */
        Class<?> c2 = Class.forName("java.lang.ref.Cleaner");
        System.out.println(c2);

        /* Shouldn't transform, as module can't access */
        Class<?> c3 = Class.forName("com.sonia.hello.HelloModules");
        System.out.println(c3);

        /* Should transform as module can access per dependency graph */
        Class<?> c4 = Class.forName("main.MainApp");
        System.out.println(c4);
    }

}