
public class Test {


    public static void main(String[] args) throws Exception {

        Class<?> c1 = Class.forName("P");
        Class.forName("Q");

        Class<?> c2 = loadClassKnown();
        System.out.println(c2);

        Class<?> c3 = loadClassUnknown("java.util.Enumeration");
        System.out.println(c3);


    }

    public static Class<?> loadClassKnown() throws Exception {
        return Class.forName("java.util.BitSet");
    }

    public static Class<?> loadClassUnknown(String someClass) throws Exception {
        return Class.forName(someClass);
    }
}
