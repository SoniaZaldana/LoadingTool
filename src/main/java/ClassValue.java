import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;

/**
 * Represents an instance of <code>java/lang/Class</code>
 *
 * The named class is an exact match (e.g. you know the class is specifically the
 * <code>java/lang/Class</code> for the named type and not a subclass)
 */
public class ClassValue extends BasicValue {
    private String name;

    public ClassValue(String name) {
        super(Type.getObjectType("java/lang/Class"));
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof ClassValue) {
            String oname = ((ClassValue)o).name;
            return (name == null && oname == null) || (oname != null && oname.equals(name));
        }
        return false;
    }
}