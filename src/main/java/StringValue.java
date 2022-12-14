import org.objectweb.asm.tree.analysis.BasicValue;

import org.objectweb.asm.Type;

/**
 * Represents a constant string value propagating through the program.
 *
 * The StringValue can have a null contents which represents a String object that has not
 * had its contents set yet.
 */
public class StringValue extends BasicValue {
    private String contents;

    public StringValue() {
        super(Type.getObjectType("java/lang/String"));
        this.contents = null;
    }

    public StringValue(String contents) {
        super(Type.getObjectType("java/lang/String"));
        this.contents = contents;
    }

    public StringValue(StringValue v) {
        super(Type.getObjectType("java/lang/String"));
        this.contents = new String(v.getContents());
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        if (this.contents != null) {
            this.contents = null;
        } else {
            this.contents = contents;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof StringValue) {
            String ocontents = ((StringValue)o).contents;
            return (ocontents == contents) || (ocontents != null && contents != null && contents.equals(ocontents));
        }
        return false;
    }
}