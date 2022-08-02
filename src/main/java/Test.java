public class Test {
    public static void main(String[] args) throws Exception {
        Class<?> c = Class.forName("Test");
        System.out.println(c);

        Class<?> c2 = loadClass();
        System.out.println(c2);
    }

    public static Class<?> loadClass() throws Exception {
        return Test.class;
    }
}
