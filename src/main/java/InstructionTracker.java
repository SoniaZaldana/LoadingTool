/**
 * Represents whether an LDC instruction should be removed on account of being followed by a Class.forName call
 * that got transformed.
 */
public class InstructionTracker {
    private boolean nextInstructionForName;

    public InstructionTracker(boolean nextInstructionForName) {
        this.nextInstructionForName = nextInstructionForName;
    }

    public boolean isNextInstructionForName() {
        return this.nextInstructionForName;
    }
}
