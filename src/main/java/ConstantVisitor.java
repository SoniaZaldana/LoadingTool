import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;


/**
 * Fills out the data structure containing arguments for Class.forName
 * that are statically known constants.
 */
public class ConstantVisitor extends ClassVisitor {
    private String className;
    private LdcTracker tracker;
    private final String OWNER = "java/lang/Class";
    private final String NAME = "forName";
    private final String DESCRIPTOR = "(Ljava/lang/String;)Ljava/lang/Class;";

    public ConstantVisitor(ClassVisitor cv, String className) {
        super(Opcodes.ASM9, cv);
        this.className = className;
        this.tracker = new LdcTracker();
    }


    @Override
    public void visitEnd() {
        try {
            fillOutParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.visitEnd();

    }

    public LdcTracker getTracker() {
        return this.tracker;
    }

    private BasicValue getStackValue(int instructionIndex, int frameIndex, Frame<BasicValue>[] frames) throws AnalyzerException {
        Frame<BasicValue> f = frames[instructionIndex];
        if (f == null) {
            return null;
        }
        int top = f.getStackSize() - 1;
        return frameIndex <= top ? f.getStack(top - frameIndex) : null;
    }

    private void fillOutParameters() throws Exception {
        ClassReader cr = new ClassReader(className);
        ClassNode cn = new ClassNode(Opcodes.ASM9);
        cr.accept(cn, 0);

        for (MethodNode mn : cn.methods) {
            InterospectiveAnalyzer analyzer = new InterospectiveAnalyzer(new ReflectionInterpreter());
            analyzer.analyze(cn.name, mn);

            int i = -1;
            for (Frame<BasicValue> frame : analyzer.getFrames()) {
                i++;
                if (frame == null) continue;
                AbstractInsnNode n = mn.instructions.get(i);
                if (n instanceof MethodInsnNode) {
                    MethodInsnNode m = (MethodInsnNode) n;

                    /* Adding parameters to forName in order of analysis */
                    if (m.owner.equals(OWNER) && m.name.equals(NAME)
                            && m.desc.equals(DESCRIPTOR)) {
                        BasicValue arg = getStackValue(i, 0, analyzer.getFrames());
                        if (arg != null && arg instanceof StringValue && ((StringValue) arg).getContents() != null) {
                            String clazz = ((StringValue) arg).getContents();
                            System.out.println("reflected class parameter fetched from stack: " + clazz);
                            tracker.addParameter(clazz);
                        }
                    }
                } else if (n instanceof LdcInsnNode) {

                    /* Keep track of LDC instructions and whether they should be removed */
                    LdcInsnNode l = (LdcInsnNode) n;
                    AbstractInsnNode next = l.getNext();
                    if (next instanceof MethodInsnNode) {
                        MethodInsnNode nextInsn = (MethodInsnNode) next;
                        if (nextInsn.owner.equals(OWNER) && nextInsn.desc.equals(DESCRIPTOR)
                                && nextInsn.name.equals(NAME)) {
                            BasicValue arg = getStackValue(i+1, 0, analyzer.getFrames());
                            if (arg != null && arg instanceof StringValue
                                    && ((StringValue) arg).getContents() != null) {
                                tracker.addLdcInstruction(new InstructionTracker(true));
                                continue;
                            }
                        }
                    }
                    tracker.addLdcInstruction(new InstructionTracker(false));
                } else if (n instanceof VarInsnNode) {
                    AbstractInsnNode prev = n.getPrevious();
                    if (prev instanceof MethodInsnNode) {
                        MethodInsnNode prevInsn = (MethodInsnNode) prev;
                        if (prevInsn.owner.equals(OWNER) && prevInsn.desc.equals(DESCRIPTOR)
                                && prevInsn.name.equals(NAME)) {
                            BasicValue arg = getStackValue(i-1, 0, analyzer.getFrames());
                            if (arg != null && arg instanceof StringValue
                                    && ((StringValue) arg).getContents() != null) {

                                tracker.addLoadInstructionTracker(new InstructionTracker(true));
                                continue;
                            }
                        }
                    }
                    tracker.addLoadInstructionTracker(new InstructionTracker(false));
                }
            }
        }
    }


}
