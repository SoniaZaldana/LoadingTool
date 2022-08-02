import org.objectweb.asm.util.ASMifier;

import java.io.IOException;

public class BytecodeProducer {
    public static void main(String[] args ) {
        try {
            ASMifier.main(new String[] {"Test.class"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
