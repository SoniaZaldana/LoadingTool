import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the parameters associated with Class.forName calls,
 * as well as whether LDC instructions should be removed
 */
public class LdcTracker {
    private List<InstructionTracker> instructionTracker;
    private List<String> parameterTracker;

    public LdcTracker() {
        this.instructionTracker = new ArrayList<>();
        this.parameterTracker = new ArrayList<>();
    }

    public List<InstructionTracker> getInstructionTrackers() {
        return this.instructionTracker;
    }

    public List<String> getParameterTracker() {
        return this.parameterTracker;
    }

    public void addParameter(String parameter) {
        parameterTracker.add(parameter);
    }

    public void addInstructionTracker(InstructionTracker tracker) {
        instructionTracker.add(tracker);
    }

}
