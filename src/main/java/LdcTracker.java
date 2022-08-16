import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the parameters associated with Class.forName calls,
 * as well as whether LDC instructions should be removed
 */
public class LdcTracker {
    private List<InstructionTracker> ldcInstructionTracker;
    private List<String> parameterTracker;

    private List<InstructionTracker> loadInstructionTracker;

    public LdcTracker() {
        this.ldcInstructionTracker = new ArrayList<>();
        this.parameterTracker = new ArrayList<>();
        this.loadInstructionTracker = new ArrayList<>();
    }

    public List<InstructionTracker> getLdcInstructionTracker() {
        return this.ldcInstructionTracker;
    }

    public List<InstructionTracker> getLoadInstructionTracker() { return this.loadInstructionTracker; }

    public List<String> getParamsTracker() {
        return this.parameterTracker;
    }

    public void addParameter(String parameter) {
        parameterTracker.add(parameter);
    }

    public void addLdcInstruction(InstructionTracker tracker) {
        ldcInstructionTracker.add(tracker);
    }

    public void addLoadInstructionTracker(InstructionTracker tracker) {
        loadInstructionTracker.add(tracker);
    }

}
