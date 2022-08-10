public class Test {

    private static final String HASH = "java.util.HashMap";

    public static void main(String[] args) throws Exception {
        Class<?> c = Class.forName("java.util.Enumeration");
        System.out.println(c);

        Class<?> c2 = Class.forName("java.util.ArrayList");
        System.out.println(c2);

        Class<?> c3 = loadClassUnknown("java.util.Enumeration");
        System.out.println(c3);

        Class<?> c4 = Class.forName(HASH);
        System.out.println(c4);

        Class<?> c5 = loadClassKnown();
        System.out.println(c5);

        Class<?> c6 = Test.class;
        System.out.println(c6);


    }

    public static Class<?> loadClassKnown() throws Exception {
        return Class.forName("java.util.BitSet");
    }

    public static Class<?> loadClassUnknown(String someClass) throws Exception {
        return Class.forName(someClass);
    }
}
