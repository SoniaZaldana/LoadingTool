import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the parameters associated with Class.forName calls,
 * as well as whether LDC instructions should be removed
 */
public class LdcTracker {
    private List<InstructionTracker> ldcInstructionTracker;
    private List<String> parameterTracker;

    public LdcTracker() {
        this.ldcInstructionTracker = new ArrayList<>();
        this.parameterTracker = new ArrayList<>();
    }

    public List<InstructionTracker> getLdcInstructionTracker() {
        return this.ldcInstructionTracker;
    }

    public List<String> getParamsTracker() {
        return this.parameterTracker;
    }

    public void addParameter(String parameter) {
        parameterTracker.add(parameter);
    }

    public void addLdcInstruction(InstructionTracker tracker) {
        ldcInstructionTracker.add(tracker);
    }

}
